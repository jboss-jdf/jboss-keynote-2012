package org.jboss.jbw2012.keynote.rest.admin;

import java.util.ArrayList ;
import java.util.List ;

import javax.annotation.Resource ;
import javax.enterprise.event.Event ;
import javax.enterprise.event.Observes ;
import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.transaction.RollbackException ;
import javax.transaction.Status ;
import javax.transaction.Synchronization ;
import javax.transaction.SystemException ;
import javax.transaction.Transaction ;
import javax.transaction.TransactionManager ;

import org.jboss.jbw2012.keynote.admin.model.AdminUserInfo ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminUserQueryEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminUserUpdateEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminUserVIPUpdateEvent ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.rest.retry.HibernateRetryHandler ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;

@Singleton
public class AdminUserService
{
    @Resource(mappedName="java:/TransactionManager")
    private TransactionManager tm ;

    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private Event<AdminUserUpdateEvent> adminUserUpdateEvent ;
    
    @Transactional
    public void userQuery(final @Observes AdminUserQueryEvent event)
    {
        final List<User> users = modelUtils.getBuyers(event.getStart(), event.getLength()) ;
        final List<AdminUserInfo> userInfo = new ArrayList<AdminUserInfo>() ;
        for(User user: users)
        {
            userInfo.add(new AdminUserInfo(user.getName(), user.getId().toString(), user.isVip())) ;
        }
        try
        {
            final Transaction tx = tm.getTransaction() ;
            tx.registerSynchronization(new UserUpdateSynchronization(event.getStart(), userInfo)) ;
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
    
    @Transactional(retryHandlers={HibernateRetryHandler.class})
    public void userVIP(final @Observes AdminUserVIPUpdateEvent event)
    {
        final User user ;
        try
        {
            user = modelUtils.getUser(Long.parseLong(event.getId())) ;
        }
        catch (final NumberFormatException nfe)
        {
            return ;
        }
        user.setVip(event.isVip()) ;
    }

    private class UserUpdateSynchronization implements Synchronization
    {
        private final int start ;
        private final List<AdminUserInfo> userInfo ;
        
        UserUpdateSynchronization(final int start, final List<AdminUserInfo> userInfo)
        {
            this.start = start ;
            this.userInfo = userInfo ;
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
                adminUserUpdateEvent.fire(new AdminUserUpdateEvent(start, userInfo)) ;
            }
        }
    }
}
