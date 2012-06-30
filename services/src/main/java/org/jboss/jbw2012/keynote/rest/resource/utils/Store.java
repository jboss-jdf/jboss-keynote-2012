package org.jboss.jbw2012.keynote.rest.resource.utils ;

public class Store
{
    private static final Store INSTANCE = new Store() ;
    
    private StoreStatus status ;
    
    public static Store getStore()
    {
        return INSTANCE ;
    }
    
    private Store()
    {
        status = StoreStatus.OPEN ;
    }
    
    public StoreStatus getStatus()
    {
        return status ;
    }
    
    public void setStatus(final StoreStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException("Must provide store status") ;
        }
        this.status = status ;
    }
}