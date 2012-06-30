package org.jboss.jbw2012.keynote.process;

import java.util.Map ;
import java.util.Map.Entry ;
import java.util.Set ;
import java.util.TreeMap ;
import java.util.concurrent.ConcurrentHashMap ;
import java.util.concurrent.atomic.AtomicLong ;
import java.util.concurrent.locks.Lock ;
import java.util.concurrent.locks.ReadWriteLock ;
import java.util.concurrent.locks.ReentrantReadWriteLock ;

import javax.annotation.Resource ;
import javax.inject.Singleton ;
import javax.transaction.RollbackException ;
import javax.transaction.Status ;
import javax.transaction.Synchronization ;
import javax.transaction.SystemException ;
import javax.transaction.Transaction ;
import javax.transaction.TransactionManager ;

import org.drools.runtime.process.NodeInstance ;
import org.drools.runtime.process.ProcessInstance ;
import org.jbpm.bpmn2.xml.XmlBPMNProcessDumper ;

@Singleton
public class ProcessStatisticsManager
{
    @Resource(mappedName="java:/TransactionManager")
    private TransactionManager tm ;
    
    private ReadWriteLock lock = new ReentrantReadWriteLock() ;
    private Lock updateLock = lock.readLock() ;
    private Lock queryLock = lock.writeLock() ;
    
    private final AtomicLong processStartedCounter = new AtomicLong() ;
    private final AtomicLong processCompletedCounter = new AtomicLong() ;
    private final ConcurrentHashMap<String, AtomicLong> nodeCounters = new ConcurrentHashMap<String, AtomicLong>() ;

    public void processStarted(final ProcessInstance processInstance)
    {
        registerSynchronization(new ProcessStartedSynchronization()) ;
    }
    
    public void processStarted(final NodeInstance nodeInstance)
    {
        registerSynchronization(new UpdateNodeCounterSynchronization(XmlBPMNProcessDumper.getUniqueNodeId(nodeInstance.getNode()), 1)) ;
    }

    public void processCompleted(final ProcessInstance processInstance)
    {
        registerSynchronization(new ProcessCompletedSynchronization()) ;
    }

    public void processCompleted(final NodeInstance nodeInstance)
    {
        registerSynchronization(new UpdateNodeCounterSynchronization(XmlBPMNProcessDumper.getUniqueNodeId(nodeInstance.getNode()), 1)) ;
    }
    
    public void humanTaskStarted(final NodeInstance nodeInstance)
    {
        registerSynchronization(new UpdateNodeCounterSynchronization(XmlBPMNProcessDumper.getUniqueNodeId(nodeInstance.getNode()), 1)) ;
    }
    
    public void humanTaskCompleted(final NodeInstance nodeInstance)
    {
        registerSynchronization(new UpdateNodeCounterSynchronization(XmlBPMNProcessDumper.getUniqueNodeId(nodeInstance.getNode()), -1)) ;
    }
 
    public ProcessStatistics getProcessStatistics()
    {
        queryLock.lock() ;
        try
        {
            return new ProcessStatistics(processStartedCounter.get(), processCompletedCounter.get(), getNodeCounters()) ;
        }
        finally
        {
            queryLock.unlock() ;
        }
    }
    
    private Map<String, Long> getNodeCounters()
    {
        if (nodeCounters.size() > 0)
        {
            final Map<String, Long> counters = new TreeMap<String, Long>() ;
            final Set<Entry<String, AtomicLong>> entries = nodeCounters.entrySet()  ;
            for(Entry<String, AtomicLong> entry: entries)
            {
                counters.put(entry.getKey(), entry.getValue().get()) ;
            }
            return counters ;
        }
        else
        {
            return null ;
        }
    }
            

    private void registerSynchronization(final Synchronization synchronization)
    {
        try
        {
            final Transaction tx = tm.getTransaction() ;
            tx.registerSynchronization(synchronization) ;
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
    
    private void updateCounter(final AtomicLong counter, final long count)
    {
        updateLock.lock();
        try
        {
            counter.addAndGet(count) ;
        }
        finally
        {
            updateLock.unlock() ;
        }
    }
    
    private class ProcessStartedSynchronization implements Synchronization
    {
        @Override
        public void beforeCompletion()
        {
        }

        @Override
        public void afterCompletion(final int status)
        {
            if (Status.STATUS_COMMITTED == status)
            {
                updateCounter(processStartedCounter, 1) ;
            }
        }
    }
    
    private class ProcessCompletedSynchronization implements Synchronization
    {
        @Override
        public void beforeCompletion()
        {
        }

        @Override
        public void afterCompletion(final int status)
        {
            if (Status.STATUS_COMMITTED == status)
            {
                updateCounter(processCompletedCounter, 1) ;
            }
        }
    }
    
    private class UpdateNodeCounterSynchronization implements Synchronization
    {
        private final String id ;
        private final long count ;
        
        UpdateNodeCounterSynchronization(final String id, final long count)
        {
            this.id = id ;
            this.count = count ;
        }
        
        @Override
        public void beforeCompletion()
        {
        }

        @Override
        public void afterCompletion(final int status)
        {
            if (Status.STATUS_COMMITTED == status)
            {
                final AtomicLong counter ;
                final AtomicLong currentCounter = nodeCounters.get(id) ;
                if (currentCounter == null)
                {
                    final AtomicLong newCounter = new AtomicLong() ;
                    final AtomicLong replaced = nodeCounters.putIfAbsent(id, newCounter) ;
                    counter = (replaced == null ? newCounter : replaced) ;
                }
                else
                {
                    counter = currentCounter ;
                }
                updateCounter(counter, count) ;
            }
        }
    }
}
