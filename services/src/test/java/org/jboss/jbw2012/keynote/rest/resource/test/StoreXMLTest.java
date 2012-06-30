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
public class StoreXMLTest extends BaseResourceTest
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
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/Store_createBuyerEastUser_request.xml", "xml/Store_createBuyerEastUser_response.xml") ; // 28
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/Store_createBuyerWestUser_request.xml", "xml/Store_createBuyerWestUser_response.xml") ; // 30
        // create two approvers (east/west)
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/Store_createApproverEastUser_request.xml", "xml/Store_createApproverEastUser_response.xml") ; // 32
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/Store_createApproverWestUser_request.xml", "xml/Store_createApproverWestUser_response.xml") ; // 34
        
        final MultivaluedMap<String, String> storeParams = new MultivaluedMapImpl<String, String>() ;
        final MultivaluedMap<String, String> vipParams = new MultivaluedMapImpl<String, String>() ;
        vipParams.putSingle("vip", "true") ;
        
        // store open
        storeParams.putSingle("status", StoreStatus.OPEN.name()) ;
        runXMLGetTest(TEST_RESOURCE_PREFIX, "/store", storeParams, "xml/Store_storeOpen_response.xml") ;

        // add to both shopping carts
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "xml/Store_addItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "xml/Store_addItem.xml") ;
        
        // checkout
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutEast1_response.xml") ; // 37
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutWest1_response.xml") ; // 40
        
        // assign orders
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=32", "xml/Store_nextOrder.xml", "xml/Store_eastNextOrder1_response.xml") ;
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/40/assign?userId=34", "xml/Store_assign.xml", "xml/Store_westAssign1_response.xml") ;
        
        // approve two orders
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/37/approve?userId=32", "xml/Store_approve.xml") ;
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/40/reject?userId=34&message=Our%20rejection%20message", "xml/Store_reject.xml") ;
        
        // add to both shopping carts
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "xml/Store_addItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "xml/Store_addItem.xml") ;
        // store vip_only
        storeParams.putSingle("status", StoreStatus.VIP_ONLY.name()) ;
        runXMLGetTest(TEST_RESOURCE_PREFIX, "/store", storeParams, "xml/Store_storeVipOnly_response.xml") ;
        
        // add to both shopping carts, should fail
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "xml/Store_addItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "xml/Store_addItem.xml") ;
        
        // checkout, both fail
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutEast2_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutWest2_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        
        // enable vip on users
        vipParams.putSingle("userId", "28") ;
        runXMLGetTest(TEST_RESOURCE_PREFIX, "/vip", vipParams, "xml/Store_eastBuyerVip_response.xml") ;
        vipParams.putSingle("userId", "30") ;
        runXMLGetTest(TEST_RESOURCE_PREFIX, "/vip", vipParams, "xml/Store_westBuyerVip_response.xml") ;
        
        // add to shopping cart
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "xml/Store_addItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "xml/Store_addItem.xml") ;
        
        // checkout
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutEast3_response.xml") ; // 45
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutWest3_response.xml") ; // 48
        
        // assign orders
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=32", "xml/Store_nextOrder.xml", "xml/Store_eastNextOrder2_response.xml") ;
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/48/assign?userId=34", "xml/Store_assign.xml", "xml/Store_westAssign2_response.xml") ;
        
        // approve order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/45/approve?userId=32", "xml/Store_approve.xml") ;
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/48/reject?userId=34&message=Our%20rejection%20message", "xml/Store_reject.xml") ;
        
        // add to both shopping carts
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "xml/Store_addItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "xml/Store_addItem.xml") ;

        // checkout
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutEast4_response.xml") ; // 53
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutWest4_response.xml") ; // 56
        
        // assign one to approver
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=32", "xml/Store_nextOrder.xml", "xml/Store_eastNextOrder3_response.xml") ;
        
        // store closed
        storeParams.putSingle("status", StoreStatus.CLOSED.name()) ;
        runXMLGetTest(TEST_RESOURCE_PREFIX, "/store", storeParams, "xml/Store_storeClosed_response.xml") ;
        
        // add to both shopping carts, should fail
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/additem", "xml/Store_addItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/additem", "xml/Store_addItem.xml") ;
        
        // checkout, both fail
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/28/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutEast5_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/30/checkout", "xml/Store_checkout.xml", "xml/Store_checkoutWest5_response.xml", Status.FORBIDDEN.getStatusCode()) ;

        // approve/reject assigned order, should fail
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/53/approve?userId=32", "xml/Store_approve.xml", "xml/Store_approveClosed.xml", Status.FORBIDDEN.getStatusCode()) ;
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/53/reject?userId=32&message=Our%20rejection%20message", "xml/Store_reject.xml", "xml/Store_rejectClosed.xml", Status.FORBIDDEN.getStatusCode()) ;
        // next order should fail
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder?userId=34", "xml/Store_nextOrder.xml", "xml/Store_westNextOrder4_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // assign order should fail
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/56/assign?userId=34", "xml/Store_assign.xml", "xml/Store_westAssign4_response.xml", Status.FORBIDDEN.getStatusCode()) ;
    }
}
