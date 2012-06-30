package org.jboss.jbw2012.keynote.admin.model.events;


public class AdminUserQueryEvent
{
    private final int start ;
    private final int length ;
    
    public AdminUserQueryEvent(final int start, final int length)
    {
        this.start = start ;
        this.length = length ;
    }

    public int getStart()
    {
        return start ;
    }

    public int getLength()
    {
        return length ;
    }
}
