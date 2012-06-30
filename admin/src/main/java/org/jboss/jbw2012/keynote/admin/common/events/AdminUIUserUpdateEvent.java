package org.jboss.jbw2012.keynote.admin.common.events;

import java.util.List ;

import org.jboss.errai.common.client.api.annotations.Portable ;
import org.jboss.jbw2012.keynote.admin.common.AdminUIUserInfo ;

@Portable
public class AdminUIUserUpdateEvent
{
    private int start ;
    private List<AdminUIUserInfo> userInfo ;
    
    public AdminUIUserUpdateEvent()
    {
    }
    
    public AdminUIUserUpdateEvent(final int start, final List<AdminUIUserInfo> userInfo)
    {
        this.start = start ;
        this.userInfo = userInfo ;
    }

    public int getStart()
    {
        return start ;
    }

    public void setStart(final int start)
    {
        this.start = start ;
    }
    
    public List<AdminUIUserInfo> getUserInfo()
    {
        return userInfo ;
    }

    public void setUserInfo(final List<AdminUIUserInfo> userInfo)
    {
        this.userInfo = userInfo ;
    }
}
