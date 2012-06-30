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
public class OrderResourceUnassignJSONTest extends BaseResourceTest
{
    protected static final String USER_RESOURCE_PREFIX = RESOURCE_PREFIX + UserResource.RESOURCE_PREFIX ;
    protected static final String ORDER_RESOURCE_PREFIX = RESOURCE_PREFIX + OrderResource.RESOURCE_PREFIX ;
    protected static final String SHOPPING_CART_RESOURCE_PREFIX = RESOURCE_PREFIX + ShoppingCartResource.RESOURCE_PREFIX ;
    protected static final String STATISTICS_RESOURCE_PREFIX = RESOURCE_PREFIX + StatisticsResource.RESOURCE_PREFIX ;
    
    @Before
    public void initialise()
        throws Exception
    {
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addFirstItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addSecondItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "json/ShoppingCart_checkout_request.json") ;
    }
    
    @Test
    public void workOrders()
        throws Exception
    {
        checkOrders("json/Order_unassignedFirstOrders.json") ;
        checkOpenOrders("json/Order_unassignedFirstOpenOrders.json") ;

        // assign order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/assign?userId=22", "json/Order_unassigned_assign.json", "json/Order_unassigned_assign_response.json") ;
        
        checkOrders("json/Order_unassignedSecondOrders.json") ;
        checkOpenOrders("json/Order_unassignedSecondOpenOrders.json") ;

        Thread.sleep(JBWTaskManagerConfiguration.EXPIRY_TIME + 10000) ;
        
        checkOrders("json/Order_unassignedThirdOrders.json") ;
        checkOpenOrders("json/Order_unassignedThirdOpenOrders.json") ;
    }
    
    private void checkOrders(final String resource)
        throws Exception
    {
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl<String, String>() ;
        queryParams.add("startPosition", "0") ;
        queryParams.add("maxResults", "10") ;
        
        runJSONGetTest(ORDER_RESOURCE_PREFIX, "/", queryParams, resource) ;
    }
    
    private void checkOpenOrders(final String resource)
        throws Exception
    {
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl<String, String>() ;
        queryParams.add("startPosition", "0") ;
        queryParams.add("maxResults", "10") ;
        queryParams.add("userId", "22") ;
        
        runJSONGetTest(ORDER_RESOURCE_PREFIX, "/open", queryParams, resource) ;
    }
}
