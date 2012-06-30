package org.jboss.jbw2012.keynote.errai.events ;


public class UpdateRateEvent
{
    private final String team ;
    private final long rate ;

    public UpdateRateEvent(final String team, final long rate)
    {
        this.team = team ;
        this.rate = rate ;
    }

    public String getTeam()
    {
        return team ;
    }

    public long getRate()
    {
        return rate ;
    }
}
