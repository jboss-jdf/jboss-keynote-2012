package org.jboss.jbw2012.keynote.process;

import org.drools.runtime.process.WorkItem ;
import org.drools.runtime.process.WorkItemHandler ;
import org.drools.runtime.process.WorkItemManager ;


public class LogWorkItemHandler implements WorkItemHandler
{
    @Override
    public void executeWorkItem(final WorkItem workItem, final WorkItemManager manager)
    {
        final String message = (String) workItem.getParameter("Message") ;
        if (message != null)
        {
            // KEV log this to the server.log
            System.out.println(message) ;
        }
        manager.completeWorkItem(workItem.getId(), null) ;
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager)
    {
        manager.abortWorkItem(workItem.getId()) ;
    }

}
