package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MultivaluedMap ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.process.JBWTaskManagerConfiguration ;
import org.jboss.jbw2012.keynote.rest.resource.OrderResource ;
import org.jboss.jbw2012.keynote.rest.resource.ShoppingCartResource ;
import org.jboss.jbw2012.keynote.rest.resource.StatisticsResource ;
import org.jboss.jbw2012.keynote.rest.resource.UserResource ;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl ;
import org.junit.Before ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class OrderResourceUnassignXMLTest extends BaseResourceTest
{
    protected static final String USER_RESOURCE_PREFIX = RESOURCE_PREFIX + UserResource.RESOURCE_PREFIX ;
    protected static final String ORDER_RESOURCE_PREFIX = RESOURCE_PREFIX + OrderResource.RESOURCE_PREFIX ;
    protected static final String SHOPPING_CART_RESOURCE_PREFIX = RESOURCE_PREFIX + ShoppingCartResource.RESOURCE_PREFIX ;
    protected static final String STATISTICS_RESOURCE_PREFIX = RESOURCE_PREFIX + StatisticsResource.RESOURCE_PREFIX ;
    
    @Before
    public void initialise()
        throws Exception
    {
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addFirstItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addSecondItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "xml/ShoppingCart_checkout_request.xml") ;
    }
    
    @Test
    public void workOrders()
        throws Exception
    {
        checkOrders("xml/Order_unassignedFirstOrders.xml") ;
        checkOpenOrders("xml/Order_unassignedFirstOpenOrders.xml") ;

        // assign order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/assign?userId=22", "xml/Order_unassigned_assign.xml", "xml/Order_unassigned_assign_response.xml") ;
        
        checkOrders("xml/Order_unassignedSecondOrders.xml") ;
        checkOpenOrders("xml/Order_unassignedSecondOpenOrders.xml") ;

        Thread.sleep(JBWTaskManagerConfiguration.EXPIRY_TIME + 10000) ;
        
        checkOrders("xml/Order_unassignedThirdOrders.xml") ;
        checkOpenOrders("xml/Order_unassignedThirdOpenOrders.xml") ;
    }
    
    private void checkOrders(final String resource)
        throws Exception
    {
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl<String, String>() ;
        queryParams.add("startPosition", "0") ;
        queryParams.add("maxResults", "10") ;
        
        runXMLGetTest(ORDER_RESOURCE_PREFIX, "/", queryParams, resource) ;
    }
    
    private void checkOpenOrders(final String resource)
        throws Exception
    {
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl<String, String>() ;
        queryParams.add("startPosition", "0") ;
        queryParams.add("maxResults", "10") ;
        queryParams.add("userId", "22") ;
        
        runXMLGetTest(ORDER_RESOURCE_PREFIX, "/open", queryParams, resource) ;
    }
}
