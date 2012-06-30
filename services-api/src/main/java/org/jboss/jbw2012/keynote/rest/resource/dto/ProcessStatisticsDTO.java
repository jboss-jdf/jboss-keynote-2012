package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.util.Map ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlRootElement ;

@XmlRootElement(name="processStatistics")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessStatisticsDTO
{
    private long processesStarted ;
    private long processesCompleted ;
    private Map<String, Long> nodeCounts ;
    
    public ProcessStatisticsDTO()
    {
    }
    
    public ProcessStatisticsDTO(final long processesStarted,
        final long processesCompleted, final Map<String, Long> nodeCounts)
    {
        this.processesStarted = processesStarted ;
        this.processesCompleted = processesCompleted ;
        this.nodeCounts = nodeCounts ;
    }
    
    public long getProcessesStarted()
    {
        return processesStarted ;
    }
    
    public void setProcessesStarted(final long processesStarted)
    {
        this.processesStarted = processesStarted ;
    }
    
    public long getProcessesCompleted()
    {
        return processesCompleted ;
    }
    
    public void setProcessesCompleted(final long processesCompleted)
    {
        this.processesCompleted = processesCompleted ;
    }
    
    public Map<String, Long> getNodeCounts()
    {
        return nodeCounts ;
    }
    
    public void setNodeCounts(final Map<String, Long> nodeCounts)
    {
        this.nodeCounts = nodeCounts ;
    }
}
