package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MultivaluedMap ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
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
public class OrderResourceJSONTest extends BaseResourceTest
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
        
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addSecondItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addThirdItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "json/ShoppingCart_checkout_request.json") ;
    }
    
    @Test
    public void workOrders()
        throws Exception
    {
        // check orders (two)
        // check open orders (two)
        checkOrders("json/Order_firstOrders.json") ;
        checkOpenOrders("json/Order_firstOpenOrders.json") ;
        checkProcessStatistics("json/Order_firstProcessStatistics.json") ;

        // assign order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/assign?userId=22", "json/Order_assign.json", "json/Order_assign_response.json") ;
        runJSONGetTest(USER_RESOURCE_PREFIX, "/22/assignedOrder", null, "json/Order_assignedOrder_22_response.json") ;
        
        // check orders (two)
        // check open orders (one)
        checkOrders("json/Order_secondOrders.json") ;
        checkOpenOrders("json/Order_secondOpenOrders.json") ;
        checkProcessStatistics("json/Order_secondProcessStatistics.json") ;

        // obtain next order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=24", "json/Order_nextOrder.json", "json/Order_nextOrder_response.json") ;
        runJSONGetTest(USER_RESOURCE_PREFIX, "/24/assignedOrder", null, "json/Order_assignedOrder_24_response.json") ;

        // check orders (two)
        // check open orders (none)
        checkOrders("json/Order_thirdOrders.json") ;
        checkOpenOrders("json/Order_thirdOpenOrders.json") ;
        checkProcessStatistics("json/Order_thirdProcessStatistics.json") ;
        
        // approve order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/approve?userId=22", "json/Order_approve.json") ;
        runJSONGetTest(USER_RESOURCE_PREFIX, "/22/assignedOrder", null) ;
        // reject order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/34/reject?userId=24&message=Our%20rejection%20message", "json/Order_reject.json") ;
        runJSONGetTest(USER_RESOURCE_PREFIX, "/24/assignedOrder", null) ;

        // check orders (two)
        // check open orders (none)
        checkOrders("json/Order_fourthOrders.json") ;
        checkOpenOrders("json/Order_fourthOpenOrders.json") ;
        checkProcessStatistics("json/Order_fourthProcessStatistics.json") ;

        // check no more orders
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=22", "json/Order_nextOrder.json") ;
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
    
    private void checkProcessStatistics(final String resource)
        throws Exception
    {
        runJSONGetTest(STATISTICS_RESOURCE_PREFIX, "/process", null, resource) ;
    }
}
