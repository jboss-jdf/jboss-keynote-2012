package org.jboss.jbw2012.keynote.errai.events ;

import java.util.List ;

public class UpdateTotalEvent
{
    private final List<UpdateTotal> updateTotals ;

    public UpdateTotalEvent(final List<UpdateTotal> updateTotals)
    {
        this.updateTotals = updateTotals ;
    }

    public List<UpdateTotal> getUpdateTotals()
    {
        return updateTotals ;
    }
}
