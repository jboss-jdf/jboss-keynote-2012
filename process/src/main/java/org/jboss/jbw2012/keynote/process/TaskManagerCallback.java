package org.jboss.jbw2012.keynote.process;

import java.util.List ;


public interface TaskManagerCallback
{
    public void expireTasks(final List<Long> expiredTasks) ;
}
