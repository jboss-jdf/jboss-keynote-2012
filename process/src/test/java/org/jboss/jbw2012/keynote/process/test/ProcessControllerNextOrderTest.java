package org.jboss.jbw2012.keynote.process.test ;

import org.jboss.arquillian.junit.Arquillian ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
public class ProcessControllerNextOrderTest extends BaseProcessControllerTest
{
    @Test
    public void workProcessWithNextOrder()
        throws Exception
    {
        startProcess() ;
        
        verifyFirstOpenOrder() ;
        
        assignNextOrder() ;
        
        approveOrder(true) ;
    }
}