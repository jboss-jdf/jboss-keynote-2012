package org.jboss.jbw2012.keynote.admin.model.events;

import org.jboss.jbw2012.keynote.admin.model.AdminStoreStatus ;


public class AdminStoreStatusChangeEvent
{
    private final AdminStoreStatus storeStatus ;
    
    public AdminStoreStatusChangeEvent(final AdminStoreStatus storeStatus)
    {
        this.storeStatus = storeStatus ;
    }

    public AdminStoreStatus getStoreStatus()
    {
        return storeStatus ;
    }
}
