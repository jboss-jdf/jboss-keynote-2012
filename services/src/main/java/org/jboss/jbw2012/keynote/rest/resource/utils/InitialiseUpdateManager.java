package org.jboss.jbw2012.keynote.rest.resource.utils;

import javax.annotation.PostConstruct ;
import javax.annotation.PreDestroy ;
import javax.ejb.Singleton ;
import javax.ejb.Startup ;
import javax.inject.Inject ;

@Singleton
@Startup
public class InitialiseUpdateManager
{
    @Inject
    private UpdateManager updateManager ;

    @PostConstruct
    public void start()
    {
        updateManager.start() ;
    }
    
    @PreDestroy
    public void stop()
    {
        updateManager.stop() ;
    }
}
