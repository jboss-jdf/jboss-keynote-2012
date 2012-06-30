package org.jboss.jbw2012.keynote.admin.model.events;

public class AdminUserVIPUpdateEvent
{
    private final String id ;
    private final boolean vip ;

    public AdminUserVIPUpdateEvent(final String id, final boolean vip)
    {
        this.id = id ;
        this.vip = vip ;
    }

    public String getId()
    {
        return id ;
    }

    public boolean isVip()
    {
        return vip ;
    }
}
