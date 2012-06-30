package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MultivaluedMap ;
import javax.ws.rs.core.Response.Status ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.OrderResource ;
import org.jboss.jbw2012.keynote.rest.resource.ShoppingCartResource ;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl ;
import org.junit.Before ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class OrderResourceValidationXMLTest extends BaseResourceTest
{
    protected static final String ORDER_RESOURCE_PREFIX = RESOURCE_PREFIX + OrderResource.RESOURCE_PREFIX ;
    protected static final String SHOPPING_CART_RESOURCE_PREFIX = RESOURCE_PREFIX + ShoppingCartResource.RESOURCE_PREFIX ;
    
    private final MultivaluedMap<String, String> invalidUserParams = new MultivaluedMapImpl<String, String>() ;
    private final MultivaluedMap<String, String> buyerParams = new MultivaluedMapImpl<String, String>() ;
    private final MultivaluedMap<String, String> approver1Params = new MultivaluedMapImpl<String, String>() ;
    private final MultivaluedMap<String, String> approver2Params = new MultivaluedMapImpl<String, String>() ;
    
    @Before
    public void initialise()
        throws Exception
    {
        // user 20 - buyer
        // user 22 - approver 1
        // user 24 - approver 2
        // order 29 -assigned to approver 1
        // order 34 - unassigned
        
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/Order_addFirstItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/Order_addSecondItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "xml/Order_checkout_request.xml") ;
        
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/Order_addSecondItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/Order_addThirdItem.xml") ;
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "xml/Order_checkout_request.xml") ;
        
        invalidUserParams.add("userId", "999999") ;
        buyerParams.add("userId", "20") ;
        approver1Params.add("userId", "22") ;
        approver2Params.add("userId", "24") ;
        
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/assign", "xml/Order_assign.xml", approver1Params, "xml/Order_assign_response.xml") ;
    }
    
    @Test
    public void testValidationErrors()
        throws Exception
    {
        // test open with invalid user
        runXMLGetTest(ORDER_RESOURCE_PREFIX, "/open", invalidUserParams, "xml/Order_openInvalidUser.xml", Status.NOT_FOUND.getStatusCode()) ;
        
        // test next with invalid user
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder", "xml/Order_nextInvalidUser_request.xml", invalidUserParams, "xml/Order_nextInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
        // test next with order already assigned
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder", "xml/Order_nextAlreadyAssigned_request.xml", approver1Params, "xml/Order_nextAlreadyAssigned_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // test next with buyer
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder", "xml/Order_nextInvalidRole_request.xml", buyerParams, "xml/Order_nextInvalidRole_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        
        // test get with invalid order
        runXMLGetTest(ORDER_RESOURCE_PREFIX, "/999999", null, "xml/Order_getInvalidOrder.xml", Status.NOT_FOUND.getStatusCode()) ;
        
        // test assign with invalid order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/999999/assign", "xml/Order_assignInvalidOrder_request.xml", approver2Params, "xml/Order_assignInvalidOrder_response.xml", Status.NOT_FOUND.getStatusCode()) ;
        // test assign with invalid user
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/34/assign", "xml/Order_assignInvalidUser_request.xml", invalidUserParams, "xml/Order_assignInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
        // test assign with order already assigned
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/assign", "xml/Order_assignOrderAlreadyAssigned_request.xml", approver2Params, "xml/Order_assignOrderAlreadyAssigned_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // test assign with user having an order assigned
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/34/assign", "xml/Order_assignUserWithAssignedOrder_request.xml", approver1Params, "xml/Order_assignUserWithAssignedOrder_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // test assign with buyer
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/assign", "xml/Order_assignInvalidRole_request.xml", buyerParams, "xml/Order_assignInvalidRole_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        
        // test approve with invalid user
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/approve", "xml/Order_approveInvalidUser_request.xml", invalidUserParams, "xml/Order_approveInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
        // test approve when not assigned
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/34/approve", "xml/Order_approveNotAssigned_request.xml", approver2Params, "xml/Order_approveNotAssigned_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // test approve when order not assigned to current user
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/approve", "xml/Order_approveNotAssignedUser_request.xml", approver2Params, "xml/Order_approveNotAssignedUser_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // test approve with invalid order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/999999/approve", "xml/Order_approveInvalidOrder_request.xml", approver1Params, "xml/Order_approveInvalidOrder_response.xml", Status.NOT_FOUND.getStatusCode()) ;

        // test reject with invalid user
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/reject", "xml/Order_rejectInvalidUser_request.xml", invalidUserParams, "xml/Order_rejectInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
        // test reject when not assigned
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/34/reject", "xml/Order_rejectNotAssigned_request.xml", approver2Params, "xml/Order_rejectNotAssigned_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // test reject when order not assigned to current user
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/29/reject", "xml/Order_rejectNotAssignedUser_request.xml", approver2Params, "xml/Order_rejectNotAssignedUser_response.xml", Status.FORBIDDEN.getStatusCode()) ;
        // test reject with invalid order
        runXMLPutTest(ORDER_RESOURCE_PREFIX, "/999999/reject", "xml/Order_rejectInvalidOrder_request.xml", approver1Params, "xml/Order_rejectInvalidOrder_response.xml", Status.NOT_FOUND.getStatusCode()) ;
    }
}
