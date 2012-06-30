package org.jboss.jbw2012.keynote.admin.common.events;

import org.jboss.errai.common.client.api.annotations.Portable ;
import org.jboss.jbw2012.keynote.admin.common.AdminUIStoreStatus ;

@Portable
public class AdminUIStoreStatusChangeEvent
{
    private AdminUIStoreStatus storeStatus ;
    
    public AdminUIStoreStatusChangeEvent()
    {
    }
    
    public AdminUIStoreStatusChangeEvent(final AdminUIStoreStatus storeStatus)
    {
        this.storeStatus = storeStatus ;
    }

    public AdminUIStoreStatus getStoreStatus()
    {
        return storeStatus ;
    }

    public void setStoreStatus(final AdminUIStoreStatus storeStatus)
    {
        this.storeStatus = storeStatus ;
    }
}
