package org.jboss.jbw2012.keynote.robots ;


enum Team
{
    EAST("East"), WEST("West"), VP ;

    private final String description ;
    
    Team()
    {
        this(null) ;
    }
    
    Team(final String description)
    {
        this.description = description ;
    }
    
    public String description()
    {
        return (description == null ? name() : description) ;
    }
}
