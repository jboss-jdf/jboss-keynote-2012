package org.jboss.jbw2012.keynote.process.test ;

import org.jboss.arquillian.junit.Arquillian ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
public class ProcessControllerExpiredAssignmentTest extends BaseProcessControllerTest
{
    @Test
    public void workProcessWithExpiringAssignment()
        throws Exception
    {
        startProcess() ;
        
        verifyFirstOpenOrder() ;
        
        assignNextOrder() ;
        
        Thread.sleep(TestTaskManagerConfiguration.EXPIRY_TIME + 10000) ;
        
        verifyFirstOpenOrder() ;
        
        assignNextOrder() ;
    }
}