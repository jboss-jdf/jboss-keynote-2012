package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MultivaluedMap ;
import javax.ws.rs.core.Response.Status ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.OrderResource ;
import org.jboss.jbw2012.keynote.rest.resource.ShoppingCartResource ;
import org.jboss.jbw2012.keynote.rest.resource.TestResource ;
import org.jboss.jbw2012.keynote.rest.resource.UserResource ;
import org.jboss.jbw2012.keynote.rest.resource.utils.StoreStatus ;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class StoreJSONTest extends BaseResourceTest
{
    protected static final String USER_RESOURCE_PREFIX = RESOURCE_PREFIX + UserResource.RESOURCE_PREFIX ;
    protected static final String SHOPPING_CART_RESOURCE_PREFIX = RESOURCE_PREFIX + ShoppingCartResource.RESOURCE_PREFIX ;
    protected static final String ORDER_RESOURCE_PREFIX = RESOURCE_PREFIX + OrderResource.RESOURCE_PREFIX ;
    protected static final String TEST_RESOURCE_PREFIX = RESOURCE_PREFIX + TestResource.RESOURCE_PREFIX ;
    
    
    @Test
    public void testStoreClosure()
        throws Exception
    {
        // create two buyers (east/west)
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/Store_createBuyerEastUser_request.json", "json/Store_createBuyerEastUser_response.json") ; // 28
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/Store_createBuyerWestUser_request.json", "json/Store_createBuyerWestUser_response.json") ; // 30
        // create two approvers (east/west)
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/Store_createApproverEastUser_request.json", "json/Store_createApproverEastUser_response.json") ; // 32
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/Store_createApproverWestUser_request.json", "json/Store_createApproverWestUser_response.json") ; // 34
        
        final MultivaluedMap<String, String> storeParams = new MultivaluedMapImpl<String, String>() ;
        final MultivaluedMap<String, String> vipParams = new MultivaluedMapImpl<String, String>() ;
        vipParams.putSingle("vip", "true") ;
        
        // store open
        storeParams.putSingle("status", StoreStatus.OPEN.name()) ;
        runJSONGetTest(TEST_RESOURCE_PREFIX, "/store", storeParams, "json/Store_storeOpen_response.json") ;

        // add to both shopping carts
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "json/Store_addItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "json/Store_addItem.json") ;
        
        // checkout
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "json/Store_checkout.json", "json/Store_checkoutEast1_response.json") ; // 37
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "json/Store_checkout.json", "json/Store_checkoutWest1_response.json") ; // 40
        
        // assign orders
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=32", "json/Store_nextOrder.json", "json/Store_eastNextOrder1_response.json") ;
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/40/assign?userId=34", "json/Store_assign.json", "json/Store_westAssign1_response.json") ;
        
        // approve two orders
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/37/approve?userId=32", "json/Store_approve.json") ;
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/40/reject?userId=34&message=Our%20rejection%20message", "json/Store_reject.json") ;
        
        // add to both shopping carts
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "json/Store_addItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "json/Store_addItem.json") ;
        // store vip_only
        storeParams.putSingle("status", StoreStatus.VIP_ONLY.name()) ;
        runJSONGetTest(TEST_RESOURCE_PREFIX, "/store", storeParams, "json/Store_storeVipOnly_response.json") ;
        
        // add to both shopping carts, should fail
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "json/Store_addItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "json/Store_addItem.json") ;
        
        // checkout, both fail
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "json/Store_checkout.json", "json/Store_checkoutEast2_response.json", Status.FORBIDDEN.getStatusCode()) ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "json/Store_checkout.json", "json/Store_checkoutWest2_response.json", Status.FORBIDDEN.getStatusCode()) ;
        
        // enable vip on users
        vipParams.putSingle("userId", "28") ;
        runJSONGetTest(TEST_RESOURCE_PREFIX, "/vip", vipParams, "json/Store_eastBuyerVip_response.json") ;
        vipParams.putSingle("userId", "30") ;
        runJSONGetTest(TEST_RESOURCE_PREFIX, "/vip", vipParams, "json/Store_westBuyerVip_response.json") ;
        
        // add to shopping cart
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "json/Store_addItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "json/Store_addItem.json") ;
        
        // checkout
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "json/Store_checkout.json", "json/Store_checkoutEast3_response.json") ; // 45
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "json/Store_checkout.json", "json/Store_checkoutWest3_response.json") ; // 48
        
        // assign orders
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=32", "json/Store_nextOrder.json", "json/Store_eastNextOrder2_response.json") ;
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/48/assign?userId=34", "json/Store_assign.json", "json/Store_westAssign2_response.json") ;
        
        // approve order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/45/approve?userId=32", "json/Store_approve.json") ;
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/48/reject?userId=34&message=Our%20rejection%20message", "json/Store_reject.json") ;
        
        // add to both shopping carts
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "json/Store_addItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "json/Store_addItem.json") ;

        // checkout
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "json/Store_checkout.json", "json/Store_checkoutEast4_response.json") ; // 53
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "json/Store_checkout.json", "json/Store_checkoutWest4_response.json") ; // 56
        
        // assign one to approver
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=32", "json/Store_nextOrder.json", "json/Store_eastNextOrder3_response.json") ;
        
        // store closed
        storeParams.putSingle("status", StoreStatus.CLOSED.name()) ;
        runJSONGetTest(TEST_RESOURCE_PREFIX, "/store", storeParams, "json/Store_storeClosed_response.json") ;
        
        // add to both shopping carts, should fail
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "json/Store_addItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "json/Store_addItem.json") ;
        
        // checkout, both fail
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "json/Store_checkout.json", "json/Store_checkoutEast5_response.json", Status.FORBIDDEN.getStatusCode()) ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "json/Store_checkout.json", "json/Store_checkoutWest5_response.json", Status.FORBIDDEN.getStatusCode()) ;

        // approve/reject assigned order, should fail
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/53/approve?userId=32", "json/Store_approve.json", "json/Store_approveClosed.json", Status.FORBIDDEN.getStatusCode()) ;
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/53/reject?userId=32&message=Our%20rejection%20message", "json/Store_reject.json", "json/Store_rejectClosed.json", Status.FORBIDDEN.getStatusCode()) ;
        // next order should fail
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=34", "json/Store_nextOrder.json", "json/Store_westNextOrder4_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // assign order should fail
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/56/assign?userId=34", "json/Store_assign.json", "json/Store_westAssign4_response.json", Status.FORBIDDEN.getStatusCode()) ;
    }
}
