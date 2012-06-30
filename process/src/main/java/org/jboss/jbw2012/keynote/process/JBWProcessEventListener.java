package org.jboss.jbw2012.keynote.process;

import javax.inject.Inject ;

import org.drools.event.process.DefaultProcessEventListener ;
import org.drools.event.process.ProcessCompletedEvent ;
import org.drools.event.process.ProcessNodeLeftEvent ;
import org.drools.event.process.ProcessNodeTriggeredEvent ;
import org.drools.event.process.ProcessStartedEvent ;
import org.drools.runtime.process.NodeInstance ;
import org.jbpm.workflow.instance.node.EndNodeInstance ;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance ;
import org.jbpm.workflow.instance.node.StartNodeInstance ;

public class JBWProcessEventListener extends DefaultProcessEventListener
{
    @Inject
    private ProcessStatisticsManager manager ;
    
    @Override
    public void beforeProcessStarted(final ProcessStartedEvent event)
    {
        manager.processStarted(event.getProcessInstance()) ;
    }
    
    @Override
    public void afterProcessCompleted(final ProcessCompletedEvent event)
    {
        manager.processCompleted(event.getProcessInstance()) ;
    }
    
    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event)
    {
        final NodeInstance nodeInstance = event.getNodeInstance() ;
        if (nodeInstance instanceof HumanTaskNodeInstance)
        {
            manager.humanTaskStarted(nodeInstance) ;
        }
        else if (nodeInstance instanceof StartNodeInstance)
        {
            manager.processStarted(nodeInstance) ;
        }
    }
    
    @Override
    public void beforeNodeLeft(final ProcessNodeLeftEvent event)
    {
        final NodeInstance nodeInstance = event.getNodeInstance() ;
        if (nodeInstance instanceof HumanTaskNodeInstance)
        {
            manager.humanTaskCompleted(nodeInstance) ;
        }
        else if (nodeInstance instanceof EndNodeInstance)
        {
            manager.processCompleted(nodeInstance) ;
        }
    }
}
