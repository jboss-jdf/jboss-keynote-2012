package org.jboss.jbw2012.keynote.process;


public class JBWTaskManagerConfiguration implements TaskManagerConfiguration
{
    public static final long EXPIRY_TIME = 180000L ; // 3 minutes

    public long getExpiryTime()
    {
        return EXPIRY_TIME ;
    }
}
