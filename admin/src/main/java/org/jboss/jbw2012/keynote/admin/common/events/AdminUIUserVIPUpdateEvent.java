package org.jboss.jbw2012.keynote.admin.common.events;

import org.jboss.errai.common.client.api.annotations.Portable ;

@Portable
public class AdminUIUserVIPUpdateEvent
{
    private String id ;
    private boolean vip ;

    public AdminUIUserVIPUpdateEvent()
    {
    }

    public AdminUIUserVIPUpdateEvent(final String id, final boolean vip)
    {
        this.id = id ;
        this.vip = vip ;
    }
    
    public String getId()
    {
        return id ;
    }

    public void setId(String id)
    {
        this.id = id ;
    }

    public boolean isVip()
    {
        return vip ;
    }

    public void setVip(boolean vip)
    {
        this.vip = vip ;
    }
}
