package org.jboss.jbw2012.keynote.model ;

public enum CustomerStatus
{
    BRONZE("Bronze"), SILVER("Silver"), GOLD("Gold") ;
    
    private final String description ;
    
    private CustomerStatus(final String description)
    {
        this.description = description ;
    }

    public String getDescription()
    {
        return description ;
    }
}
