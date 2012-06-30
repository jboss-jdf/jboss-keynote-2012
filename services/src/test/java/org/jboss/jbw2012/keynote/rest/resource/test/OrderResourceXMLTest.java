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
public class OrderResourceXMLTest extends BaseResourceTest
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
        
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addSecondItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addThirdItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "xml/ShoppingCart_checkout_request.xml") ;
    }
    
    @Test
    public void workOrders()
        throws Exception
    {
        // check orders (two)
        // check open orders (two)
        checkOrders("xml/Order_firstOrders.xml") ;
        checkOpenOrders("xml/Order_firstOpenOrders.xml") ;
        checkProcessStatistics("xml/Order_firstProcessStatistics.xml") ;

        // assign order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/assign?userId=22", "xml/Order_assign.xml", "xml/Order_assign_response.xml") ;
        runXMLGetTest(USER_RESOURCE_PREFIX, "/22/assignedOrder", null, "xml/Order_assignedOrder_22_response.xml") ;
        
        // check orders (two)
        // check open orders (one)
        checkOrders("xml/Order_secondOrders.xml") ;
        checkOpenOrders("xml/Order_secondOpenOrders.xml") ;
        checkProcessStatistics("xml/Order_secondProcessStatistics.xml") ;

        // obtain next order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=24", "xml/Order_nextOrder.xml", "xml/Order_nextOrder_response.xml") ;
        runXMLGetTest(USER_RESOURCE_PREFIX, "/24/assignedOrder", null, "xml/Order_assignedOrder_24_response.xml") ;

        // check orders (two)
        // check open orders (none)
        checkOrders("xml/Order_thirdOrders.xml") ;
        checkOpenOrders("xml/Order_thirdOpenOrders.xml") ;
        checkProcessStatistics("xml/Order_thirdProcessStatistics.xml") ;
        
        // approve order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/approve?userId=22", "xml/Order_approve.xml") ;
        runXMLGetTest(USER_RESOURCE_PREFIX, "/22/assignedOrder", null) ;
        // reject order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/34/reject?userId=24&message=Our%20rejection%20message", "xml/Order_reject.xml") ;
        runXMLGetTest(USER_RESOURCE_PREFIX, "/24/assignedOrder", null) ;

        // check orders (two)
        // check open orders (none)
        checkOrders("xml/Order_fourthOrders.xml") ;
        checkOpenOrders("xml/Order_fourthOpenOrders.xml") ;
        checkProcessStatistics("xml/Order_fourthProcessStatistics.xml") ;

        // check no more orders
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=22", "xml/Order_nextOrder.xml") ;
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
    
    private void checkProcessStatistics(final String resource)
        throws Exception
    {
        runXMLGetTest(STATISTICS_RESOURCE_PREFIX, "/process", null, resource) ;
    }
}
