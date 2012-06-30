package org.jboss.jbw2012.keynote.admin.server;

import java.util.ArrayList ;
import java.util.List ;
import java.util.concurrent.atomic.AtomicInteger ;

import javax.enterprise.context.ApplicationScoped ;
import javax.enterprise.event.Event ;
import javax.enterprise.event.Observes ;
import javax.inject.Inject ;

import org.jboss.jbw2012.keynote.admin.common.AdminUIUserInfo ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIBuyerTotalEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIConnectedEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIUserQueryEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIUserUpdateEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIUserVIPUpdateEvent ;
import org.jboss.jbw2012.keynote.admin.model.AdminUserInfo ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminUpdateBuyerCountEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminUserQueryEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminUserUpdateEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminUserVIPUpdateEvent ;

@ApplicationScoped
public class AdminUIUserService
{
    private AtomicInteger buyerTotal = new AtomicInteger() ;
    
    @Inject
    private Event<AdminUIBuyerTotalEvent> adminUIBuyerTotalEvent ;
    @Inject
    private Event<AdminUserQueryEvent> adminUserQueryEvent ;
    @Inject
    private Event<AdminUIUserUpdateEvent> adminUIUserUpdateEvent ;
    @Inject
    private Event<AdminUserVIPUpdateEvent> adminUserVIPUpdateEvent ;
    
    public void connected(final @Observes AdminUIConnectedEvent event)
    {
        adminUIBuyerTotalEvent.fire(new AdminUIBuyerTotalEvent(buyerTotal.get())) ;
    }
    
    public void updateBuyerCount(final @Observes AdminUpdateBuyerCountEvent event)
    {
        final int total = buyerTotal.addAndGet(event.getBuyerCount())  ;
        adminUIBuyerTotalEvent.fire(new AdminUIBuyerTotalEvent(total)) ;
    }
    
    public void userQuery(final @Observes AdminUIUserQueryEvent event)
    {
        adminUserQueryEvent.fire(new AdminUserQueryEvent(event.getStart(), event.getLength())) ;
    }
    
    public void userUpdate(final @Observes AdminUserUpdateEvent event)
    {
        adminUIUserUpdateEvent.fire(new AdminUIUserUpdateEvent(event.getStart(), getUserInfo(event.getUserInfo()))) ;
    }
    
    public void userVIP(final @Observes AdminUIUserVIPUpdateEvent event)
    {
        adminUserVIPUpdateEvent.fire(new AdminUserVIPUpdateEvent(event.getId(), event.isVip())) ;
    }

    private List<AdminUIUserInfo> getUserInfo(final List<AdminUserInfo> users)
    {
        final List<AdminUIUserInfo> result = new ArrayList<AdminUIUserInfo>() ;
        for(AdminUserInfo user: users)
        {
            result.add(new AdminUIUserInfo(user.getName(), user.getId(), user.isVip())) ;
        }
        return result ;
    }
}
