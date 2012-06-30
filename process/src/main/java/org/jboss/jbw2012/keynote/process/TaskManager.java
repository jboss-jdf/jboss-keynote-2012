package org.jboss.jbw2012.keynote.process;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.LinkedHashMap ;
import java.util.List ;
import java.util.Map.Entry ;
import java.util.concurrent.ConcurrentHashMap ;
import java.util.concurrent.Executors ;
import java.util.concurrent.ScheduledExecutorService ;
import java.util.concurrent.TimeUnit ;

import javax.annotation.PostConstruct ;
import javax.annotation.Resource ;
import javax.enterprise.context.Dependent ;
import javax.inject.Inject ;
import javax.transaction.RollbackException ;
import javax.transaction.Status ;
import javax.transaction.Synchronization ;
import javax.transaction.SystemException ;
import javax.transaction.Transaction ;
import javax.transaction.TransactionManager ;

import org.jbpm.task.Task ;

@Dependent
public class TaskManager implements Runnable
{
    @Resource(mappedName="java:/TransactionManager")
    private TransactionManager tm ;
    
    private ConcurrentHashMap<Transaction, TaskManagerSynchronization> synchronizationMap = new ConcurrentHashMap<Transaction, TaskManagerSynchronization>() ;
    private TaskManagerCallback taskManagerCallback ;
    private final LinkedHashMap<Long, Long> taskExpiry = new LinkedHashMap<Long, Long>() ;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1) ;
    
    @Inject
    private TaskManagerConfiguration configuration ;
    
    private long expiryTime ;
    
    @PostConstruct
    public void init()
    {
        expiryTime = configuration.getExpiryTime() ;
    }
    
    public void setTaskManagerCallback(final TaskManagerCallback taskManagerCallback)
    {
        this.taskManagerCallback = taskManagerCallback ;
        scheduledExecutorService.schedule(this, expiryTime, TimeUnit.MILLISECONDS)  ;
    }

    public void shutdown()
    {
        scheduledExecutorService.shutdown() ;
        try
        {
            scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS) ;
        }
        catch (final InterruptedException ie) {} // ignore
    }

    public void startTask(final Task task)
    {
        getSynchronization().startTask(task.getId()) ;
    }

    public void completeTask(final Task task)
    {
        getSynchronization().completeTask(task.getId()) ;
    }

    private synchronized void removeTasks(final List<Long> completedTaskIds)
    {
        for(Long completedTaskId: completedTaskIds)
        {
            taskExpiry.remove(completedTaskId) ;
        }
    }

    private synchronized void addTasks(final List<Long> startTaskIds, final long expiry)
    {
        for(Long startTaskId: startTaskIds)
        {
            taskExpiry.put(startTaskId, expiry) ;
        }
    }

    @Override
    public void run()
    {
        long delay = expiryTime ;
        final long now = System.currentTimeMillis() ;
        final List<Long> expiredTasks = new ArrayList<Long>() ;
        synchronized(this)
        {
            final Iterator<Entry<Long, Long>> entryIter = taskExpiry.entrySet().iterator() ;
            while(entryIter.hasNext())
            {
                final Entry<Long, Long> entry = entryIter.next() ;
                final long expiry = entry.getValue() ;
                if (expiry <= now)
                {
                    expiredTasks.add(entry.getKey()) ;
                    entryIter.remove() ;
                }
                else
                {
                    final long newDelay = expiry - now ;
                    if (newDelay < delay)
                    {
                        delay = newDelay ;
                    }
                    break ;
                }
            }
        }
        if (expiredTasks.size() > 0)
        {
            taskManagerCallback.expireTasks(expiredTasks) ;
        }
        scheduledExecutorService.schedule(this, delay, TimeUnit.MILLISECONDS) ;
    }

    private TaskManagerSynchronization getSynchronization()
    {
        try
        {
            final Transaction tx = tm.getTransaction() ;
            final TaskManagerSynchronization synch = synchronizationMap.get(tx) ;
            if (synch != null)
            {
                return synch ;
            }
            else
            {
                final TaskManagerSynchronization newSynch = new TaskManagerSynchronization(tx) ;
                tx.registerSynchronization(newSynch) ;
                synchronizationMap.put(tx, newSynch) ;
                return newSynch ;
            }
        }
        catch (final RollbackException re)
        {
            throw new IllegalStateException("Transaction management exception", re) ;
        }
        catch (final SystemException se)
        {
            throw new IllegalStateException("Transaction management exception", se) ;
        }
    }

    private class TaskManagerSynchronization implements Synchronization
    {
        private List<Long> completedTaskIds ;
        private List<Long> startTaskIds ;
        private final Transaction tx ;
        
        TaskManagerSynchronization(final Transaction tx)
        {
            this.tx = tx ;
        }
        
        public void completeTask(final Long completedTaskId)
        {
            if (completedTaskIds == null)
            {
                completedTaskIds = new ArrayList<Long>() ;
            }
            completedTaskIds.add(completedTaskId) ;
        }

        public void startTask(final Long startTaskId)
        {
            if (startTaskIds == null)
            {
                startTaskIds = new ArrayList<Long>() ;
            }
            startTaskIds.add(startTaskId) ;
        }

        @Override
        public void beforeCompletion()
        {
        }

        @Override
        public void afterCompletion(final int status)
        {
            synchronizationMap.remove(tx) ;
            if (Status.STATUS_COMMITTED == status)
            {
                if (completedTaskIds != null)
                {
                    removeTasks(completedTaskIds) ;
                }
                if (startTaskIds != null)
                {
                    addTasks(startTaskIds, System.currentTimeMillis() + expiryTime) ;
                }
            }
        }
    }
}
