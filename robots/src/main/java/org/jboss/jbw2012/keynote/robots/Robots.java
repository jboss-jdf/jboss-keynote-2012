package org.jboss.jbw2012.keynote.robots;

import org.jboss.jbw2012.keynote.rest.resource.api.StatisticsAPI ;
import org.jboss.jbw2012.keynote.robots.rest.RestClient ;

class Robots
{
    private final BuyerRobot eastBuyerRobot = new BuyerRobot(Team.EAST) ;
    private final BuyerRobot westBuyerRobot = new BuyerRobot(Team.WEST) ;
    private final ApproverRobot eastApproverRobot = new ApproverRobot(Team.EAST) ;
    private final ApproverRobot westApproverRobot = new ApproverRobot(Team.WEST) ;
    private final VPRobot vpRobot = new VPRobot() ;
    
    Robot getEastBuyer()
    {
        return eastBuyerRobot ;
    }

    Robot getWestBuyer()
    {
        return westBuyerRobot ;
    }
    
    Robot getEastApprover()
    {
        return eastApproverRobot ;
    }

    Robot getWestApprover()
    {
        return westApproverRobot ;
    }

    Robot getVP()
    {
        return vpRobot ;
    }

    boolean validateServerURL(final String url)
    {
        final StatisticsAPI statisticsAPI = RestClient.getRestClient().getStatisticsAPI(url) ;
        try
        {
            statisticsAPI.getProcessStatistics() ;
            return true ;
        }
        catch (final Exception ex)
        {
            return false ;
        }
    }

    void setServerURL(final String serverURL)
    {
        eastBuyerRobot.setServerURL(serverURL) ;
        westBuyerRobot.setServerURL(serverURL) ;
        eastApproverRobot.setServerURL(serverURL) ;
        westApproverRobot.setServerURL(serverURL) ;
        vpRobot.setServerURL(serverURL) ;
    }

    void start()
    {
        eastBuyerRobot.start() ;
        westBuyerRobot.start() ;
        eastApproverRobot.start() ;
        westApproverRobot.start() ;
        vpRobot.start() ;
    }

    void stop()
    {
        eastBuyerRobot.stop() ;
        westBuyerRobot.stop() ;
        eastApproverRobot.stop() ;
        westApproverRobot.stop() ;
        vpRobot.stop() ;
    }

    // Not called on event thread
    void waitUntilStopped()
    {
        eastBuyerRobot.waitUntilStopped() ;
        westBuyerRobot.waitUntilStopped() ;
        eastApproverRobot.waitUntilStopped() ;
        westApproverRobot.waitUntilStopped() ;
        vpRobot.waitUntilStopped() ;
    }
}
