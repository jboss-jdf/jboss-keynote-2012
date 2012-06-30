package org.jboss.jbw2012.keynote.rest.resource.utils;

import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;
import java.util.concurrent.ConcurrentHashMap ;
import java.util.concurrent.Executors ;
import java.util.concurrent.ScheduledExecutorService ;
import java.util.concurrent.TimeUnit ;

import javax.annotation.Resource ;
import javax.enterprise.event.Event ;
import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.transaction.RollbackException ;
import javax.transaction.Status ;
import javax.transaction.Synchronization ;
import javax.transaction.SystemException ;
import javax.transaction.Transaction ;
import javax.transaction.TransactionManager ;

import org.jboss.jbw2012.keynote.admin.model.events.AdminUpdateBuyerCountEvent ;
import org.jboss.jbw2012.keynote.errai.events.UpdateRateEvent ;
import org.jboss.jbw2012.keynote.errai.events.UpdateTotal ;
import org.jboss.jbw2012.keynote.errai.events.UpdateTotalEvent ;
import org.jboss.jbw2012.keynote.errai.events.UserInfo ;
import org.jboss.jbw2012.keynote.errai.events.UserUpdateEvent ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;

@Singleton
public class UpdateManager
{
    @Resource(mappedName="java:/TransactionManager")
    private TransactionManager tm ;

    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private Event<UpdateTotalEvent> updateTotalEvent ;
    @Inject
    private Event<UserUpdateEvent> userUpdateEvent ;
    @Inject
    private Event<AdminUpdateBuyerCountEvent> userUpdateBuyerCountEvent ;
    @Inject
    private Event<UpdateRateEvent> updateRateEvent ;

    private volatile ScheduledExecutorService executorService ;
    
    private ConcurrentHashMap<Long, Long> dirtyUsers ;
    private ConcurrentHashMap<Long, Long> newUsers ;
    
    public void start()
    {
        if (executorService == null)
        {
            dirtyUsers = new ConcurrentHashMap<Long, Long>() ;
            newUsers = new ConcurrentHashMap<Long, Long>() ;
            
            executorService = Executors.newScheduledThreadPool(1) ;
            executorService.scheduleAtFixedRate(new UpdateCommand(), 1, 1, TimeUnit.SECONDS) ;
        }
    }
    
    public void stop()
    {
        executorService.shutdown() ;
        try
        {
            executorService.awaitTermination(1, TimeUnit.SECONDS) ;
        }
        catch (final InterruptedException ie) {} // ignore
        executorService = null ;
    }
    
    public void newUser(final User ... users)
    {
        try
        {
            final Transaction tx = tm.getTransaction() ;
            tx.registerSynchronization(new NewUsersSynchronization(users)) ;
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
    
    public void markDirty(final long orderTotal, final Team team, final User ... users)
    {
        try
        {
            final Transaction tx = tm.getTransaction() ;
            tx.registerSynchronization(new MarkDirtySynchronization(orderTotal, team, users)) ;
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
    
    @Transactional
    public List<UpdateTotal> getUpdateTotals(final ConcurrentHashMap<Long, Long> users)
    {
        final List<UpdateTotal> updateTotals = new ArrayList<UpdateTotal>() ;
        final Iterator<Long> userIdIter = users.keySet().iterator()  ;
        while(userIdIter.hasNext())
        {
            final Long id = userIdIter.next() ;
            userIdIter.remove() ;
            final User user = modelUtils.getUser(id) ;
            if (user != null)
            {
                updateTotals.add(new UpdateTotal(id, user.getBuyerTotal().getTotalBought(), user.getApprovedTotal().getApproved(), user.getApprovedTotal().getRejected())) ;
            }
        }
        return updateTotals ;
    }
    
    public List<Long> getNewUserIds(final ConcurrentHashMap<Long, Long> users)
    {
        final List<Long> userIds = new ArrayList<Long>() ;
        final Iterator<Long> userIdIter = users.keySet().iterator()  ;
        while(userIdIter.hasNext())
        {
            userIds.add(userIdIter.next()) ;
            userIdIter.remove() ;
        }
        return userIds ;
    }
    
    @Transactional
    public List<UserInfo> getNewUsers(final List<Long> userIds)
    {
        final List<UserInfo> userInfos = new ArrayList<UserInfo>() ;
        for(Long id: userIds)
        {
            final User user = modelUtils.getUser(id) ;
            if (user != null)
            {
                userInfos.add(new UserInfo(id, user.getName(), user.getRole().name(), user.getTeam().name())) ;
            }
        }
        return userInfos ;
    }
    
    private class MarkDirtySynchronization implements Synchronization
    {
        private final long orderTotal ;
        private final Team team ;
        private final User[] users ;
        
        MarkDirtySynchronization(final long orderTotal, final Team team, final User[] users)
        {
            this.orderTotal = orderTotal ;
            this.team = team ;
            this.users = users ;
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
                final ConcurrentHashMap<Long, Long> dirtyUsers = UpdateManager.this.dirtyUsers ;
                if (dirtyUsers != null)
                {
                    for(User user: users)
                    {
                        dirtyUsers.put(user.getId(), user.getId()) ;
                    }
                }
                if (orderTotal > 0)
                {
                    try
                    {
                        updateRateEvent.fire(new UpdateRateEvent(team.name(), orderTotal)) ;
                    }
                    catch (final Throwable th)
                    {
                        th.printStackTrace() ;
                    }
                }
            }
        }
    }
    
    private class NewUsersSynchronization implements Synchronization
    {
        private final User[] users ;
        
        NewUsersSynchronization(final User[] users)
        {
            this.users = users ;
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
                final ConcurrentHashMap<Long, Long> newUsers = UpdateManager.this.newUsers ;
                if (newUsers != null)
                {
                    for(User user: users)
                    {
                        newUsers.put(user.getId(), user.getId()) ;
                    }
                }
            }
        }
    }
    
    private class UpdateCommand implements Runnable
    {
        public void run()
        {
            final ConcurrentHashMap<Long, Long> dirtyUsers = UpdateManager.this.dirtyUsers ;
            final ConcurrentHashMap<Long, Long> newUsers = UpdateManager.this.newUsers ;

            final List<UpdateTotal> updateTotals ;
            if ((dirtyUsers != null) && (dirtyUsers.size() > 0))
            {
                updateTotals = getUpdateTotals(dirtyUsers) ;
            }
            else
            {
                updateTotals = null ;
            }
            
            if ((newUsers != null) && (newUsers.size() > 0))
            {
                final List<Long> userIds = getNewUserIds(newUsers) ;
                final List<UserInfo> userInfos = getNewUsers(userIds) ;
                try
                {
                    userUpdateEvent.fire(new UserUpdateEvent(userInfos)) ;
                }
                catch (final Throwable th)
                {
                    th.printStackTrace() ;
                }
                final int newBuyerCount = userIds.size() ;
                try
                {
                    userUpdateBuyerCountEvent.fire(new AdminUpdateBuyerCountEvent(newBuyerCount)) ;
                }
                catch (final Throwable th)
                {
                    th.printStackTrace() ;
                }
            }
            
            if (updateTotals != null)
            {
                try
                {
                    updateTotalEvent.fire(new UpdateTotalEvent(updateTotals)) ;
                }
                catch (final Throwable th)
                {
                    th.printStackTrace() ;
                }
            }
        }
    }
}
