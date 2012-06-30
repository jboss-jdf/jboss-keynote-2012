package org.jboss.jbw2012.keynote.admin.model;


public class AdminUserInfo
{
    private final String name ;
    private final String id ;
    private final boolean vip ;
    
    public AdminUserInfo(final String name, final String id, final boolean vip)
    {
        this.name = name ;
        this.id = id ;
        this.vip = vip ;
    }

    public String getName()
    {
        return name ;
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
