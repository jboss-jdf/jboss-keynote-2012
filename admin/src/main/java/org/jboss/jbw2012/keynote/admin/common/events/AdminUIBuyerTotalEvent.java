package org.jboss.jbw2012.keynote.admin.common.events;

import org.jboss.errai.common.client.api.annotations.Portable ;

@Portable
public class AdminUIBuyerTotalEvent
{
    private int total ;

    public AdminUIBuyerTotalEvent()
    {
    }

    public AdminUIBuyerTotalEvent(final int total)
    {
        this.total = total ;
    }
    
    public int getTotal()
    {
        return total ;
    }

    public void setTotal(int total)
    {
        this.total = total ;
    }
}
