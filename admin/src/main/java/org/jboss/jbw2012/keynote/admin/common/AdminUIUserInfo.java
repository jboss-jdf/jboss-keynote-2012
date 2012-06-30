package org.jboss.jbw2012.keynote.admin.common;

import org.jboss.errai.common.client.api.annotations.Portable ;

@Portable
public class AdminUIUserInfo
{
    private String name ;
    private String id ;
    private boolean vip ;
    
    public AdminUIUserInfo()
    {
    }

    public AdminUIUserInfo(final String name, final String id, final boolean vip)
    {
        this.name = name ;
        this.id = id ;
        this.vip = vip ;
    }

    public String getName()
    {
        return name ;
    }

    public void setName(final String name)
    {
        this.name = name ;
    }

    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }

    public boolean isVip()
    {
        return vip ;
    }

    public void setVip(final boolean vip)
    {
        this.vip = vip ;
    }
}
