package org.jboss.jbw2012.keynote.admin.common.events;

import org.jboss.errai.common.client.api.annotations.Portable ;

@Portable
public class AdminUIUserQueryEvent
{
    private int start ;
    private int length ;
    
    public AdminUIUserQueryEvent()
    {
    }
    
    public AdminUIUserQueryEvent(final int start, final int length)
    {
        this.start = start ;
        this.length = length ;
    }

    public int getStart()
    {
        return start ;
    }

    public void setStart(final int start)
    {
        this.start = start ;
    }

    public int getLength()
    {
        return length ;
    }

    public void setLength(final int length)
    {
        this.length = length ;
    }
}
