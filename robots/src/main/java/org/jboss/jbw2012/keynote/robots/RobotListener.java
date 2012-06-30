package org.jboss.jbw2012.keynote.robots;

interface RobotListener
{
    public void notifyRobotThreadCount(final int threadCount) ;
    public void notifyRobotInvocationCount(final long invocationCount) ;
}
