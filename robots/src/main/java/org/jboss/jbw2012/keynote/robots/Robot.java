package org.jboss.jbw2012.keynote.robots;

import java.util.concurrent.ConcurrentLinkedQueue ;
import java.util.concurrent.Executor ;
import java.util.concurrent.Executors ;
import java.util.concurrent.atomic.AtomicBoolean ;
import java.util.concurrent.atomic.AtomicInteger ;
import java.util.concurrent.atomic.AtomicLong ;

abstract class Robot implements RobotPanelListener
{
    private final Team team ;
    
    private RobotListener listener ;
    private String serverURL ;
    
    private final Executor executor = Executors.newSingleThreadExecutor() ;
    
    private final AtomicBoolean terminate = new AtomicBoolean(true) ;
    
    private final ConcurrentLinkedQueue<RobotThread> threads = new ConcurrentLinkedQueue<RobotThread>() ;
    private final UpdateThreadTask updateThreadTask = new UpdateThreadTask() ;

    private final AtomicLong invocationCount = new AtomicLong() ;
    private final AtomicInteger terminateCount = new AtomicInteger() ;
    
    private volatile int delay ;
    private volatile int threadCount ;
    
    Robot(final Team team)
    {
        this.team = team ;
    }

    void setRobotListener(final RobotListener listener)
    {
        this.listener = listener ;
    }
    
    Team getTeam()
    {
        return team ;
    }

    String getServerURL()
    {
        return serverURL ;
    }
    
    void setServerURL(final String serverURL)
    {
        this.serverURL = serverURL ;
    }

    void start()
    {
        terminate.set(false) ;
        terminateCount.set(0) ;
        invocationCount.set(0) ;
        notifyInvocationCount() ;
        executor.execute(updateThreadTask) ;
    }

    void stop()
    {
        terminate.set(true) ;
    }

    // Not called on event thread
    void waitUntilStopped()
    {
        do
        {
            final RobotThread thread = threads.peek() ;
            if (thread == null)
            {
                return ;
            }
            
            try
            {
                thread.join() ;
            }
            catch (final InterruptedException ie) {} // ignore
        }
        while(true) ;
    }

    @Override
    public void updateDelay(final int delay)
    {
        this.delay = delay*10 ;
    }
    
    void delay()
    {
        try
        {
            Thread.sleep(delay) ;
        }
        catch (final InterruptedException ie) {}
    }
    
    void longDelay()
    {
        try
        {
            Thread.sleep(delay * 5) ;
        }
        catch (final InterruptedException ie) {}
    }

    @Override
    public void updateThreadCount(final int threadCount)
    {
        this.threadCount = threadCount ;
        if (!terminate.get())
        {
            executor.execute(updateThreadTask) ;
        }
    }
    
    synchronized void notifyRobotThreadCount()
    {
        if (listener != null)
        {
            listener.notifyRobotThreadCount(threads.size()) ;
        }
    }
    
    void incrementRobotInvocationCount()
    {
        invocationCount.incrementAndGet() ;
        notifyInvocationCount() ;
    }
    
    
    synchronized void notifyInvocationCount()
    {
        if (listener != null)
        {
            listener.notifyRobotInvocationCount(invocationCount.get()) ;
        }
    }
    
    boolean okayToContinue()
    {
        while (!terminate.get())
        {
            final int count = terminateCount.get() ;
            if (count <= 0)
            {
                return true ;
            }
            else if (terminateCount.compareAndSet(count, count-1))
            {
                break ;
            }
        }
        return false ;
    }
    
    public abstract void executeRobot() ;
    
    private class UpdateThreadTask implements Runnable
    {
        public void run()
        {
            while (!terminate.get())
            {
                final int current = threads.size() ;
                final int threadCount = Robot.this.threadCount ;
                if (current < threadCount)
                {
                    RobotThread robotThread = new RobotThread() ;
                    threads.add(robotThread) ;
                    robotThread.start() ;
                    notifyRobotThreadCount() ;
                }
                else
                {
                    if (current > threadCount)
                    {
                        terminateCount.set(current - threadCount) ;
                    }
                    break ;
                }
            }
        }
    }

    private class RobotThread extends Thread
    {
        public void run()
        {
            try
            {
                executeRobot() ;
            }
            finally
            {
                threads.remove(this) ;
                notifyRobotThreadCount() ;
            }
        }
    }
}
