package org.jboss.jbw2012.keynote.process.test;

import org.jboss.jbw2012.keynote.process.TaskManagerConfiguration ;


public class TestTaskManagerConfiguration implements TaskManagerConfiguration
{
    public static final long EXPIRY_TIME = 20000 ;

    public long getExpiryTime()
    {
        return EXPIRY_TIME ;
    }
}
