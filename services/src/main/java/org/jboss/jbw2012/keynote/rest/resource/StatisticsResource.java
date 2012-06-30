package org.jboss.jbw2012.keynote.rest.resource;

import javax.inject.Inject ;
import javax.inject.Singleton ;

import org.jboss.jbw2012.keynote.process.ProcessStatisticsManager ;
import org.jboss.jbw2012.keynote.rest.resource.api.StatisticsAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ProcessStatisticsDTO ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;

@Singleton
public class StatisticsResource implements StatisticsAPI
{
    @Inject
    private DTOUtils dtoUtils ;

    @Inject
    private ProcessStatisticsManager processStatisticsManager ;
    
    public ProcessStatisticsDTO getProcessStatistics()
    {
        return dtoUtils.getProcessStatistics(processStatisticsManager.getProcessStatistics()) ;
    }
}
