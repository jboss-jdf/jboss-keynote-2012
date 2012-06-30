package org.jboss.jbw2012.keynote.admin.model.events;

import java.util.List ;

import org.jboss.jbw2012.keynote.admin.model.AdminUserInfo ;

public class AdminUserUpdateEvent
{
    private final int start ;
    private final List<AdminUserInfo> userInfo ;
    
    public AdminUserUpdateEvent(final int start, final List<AdminUserInfo> userInfo)
    {
        this.start = start ;
        this.userInfo = userInfo ;
    }

    public int getStart()
    {
        return start ;
    }
    
    public List<AdminUserInfo> getUserInfo()
    {
        return userInfo ;
    }
}
