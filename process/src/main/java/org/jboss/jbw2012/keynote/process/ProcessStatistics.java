package org.jboss.jbw2012.keynote.process;

import java.util.Map ;

public class ProcessStatistics
{
    private final long processStartedCount ;
    private final long processCompletedCount ;
    private final Map<String, Long> nodeCounts ;

    ProcessStatistics(final long processStartedCount, final long processCompletedCount, final Map<String, Long> nodeCounts)
    {
        this.processStartedCount = processStartedCount ;
        this.processCompletedCount = processCompletedCount ;
        this.nodeCounts = nodeCounts ;
    }
    
    public long getProcessStartedCount()
    {
        return processStartedCount ;
    }
    
    public long getProcessCompletedCount()
    {
        return processCompletedCount ;
    }
    
    public Map<String, Long> getNodeCounts()
    {
        return nodeCounts ;
    }
}
