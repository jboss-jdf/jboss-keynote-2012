package org.jboss.jbw2012.keynote.admin.common.events;

import org.jboss.errai.common.client.api.annotations.Portable ;
import org.jboss.jbw2012.keynote.admin.common.AdminUIStoreStatus ;

@Portable
public class AdminUIStoreStatusEvent
{
    private AdminUIStoreStatus storeStatus ;
    
    public AdminUIStoreStatusEvent()
    {
    }
    
    public AdminUIStoreStatusEvent(final AdminUIStoreStatus storeStatus)
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
