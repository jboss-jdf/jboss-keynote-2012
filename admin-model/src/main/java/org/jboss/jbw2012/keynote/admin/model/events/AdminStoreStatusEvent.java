package org.jboss.jbw2012.keynote.admin.model.events;

import org.jboss.jbw2012.keynote.admin.model.AdminStoreStatus ;


public class AdminStoreStatusEvent
{
    private final AdminStoreStatus storeStatus ;
    
    public AdminStoreStatusEvent(final AdminStoreStatus storeStatus)
    {
        this.storeStatus = storeStatus ;
    }

    public AdminStoreStatus getStoreStatus()
    {
        return storeStatus ;
    }
}
