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
public class OrderResourceValidationJSONTest extends BaseResourceTest
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
        
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/Order_addFirstItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/Order_addSecondItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "json/Order_checkout_request.json") ;
        
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/Order_addSecondItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/Order_addThirdItem.json") ;
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "json/Order_checkout_request.json") ;
        
        invalidUserParams.add("userId", "999999") ;
        buyerParams.add("userId", "20") ;
        approver1Params.add("userId", "22") ;
        approver2Params.add("userId", "24") ;
        
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/assign", "json/Order_assign.json", approver1Params, "json/Order_assign_response.json") ;
    }
    
    @Test
    public void testValidationErrors()
        throws Exception
    {
        // test open with invalid user
        runJSONGetTest(ORDER_RESOURCE_PREFIX, "/open", invalidUserParams, "json/Order_openInvalidUser.json", Status.NOT_FOUND.getStatusCode()) ;
        
        // test next with invalid user
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder", "json/Order_nextInvalidUser_request.json", invalidUserParams, "json/Order_nextInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
        // test next with order already assigned
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder", "json/Order_nextAlreadyAssigned_request.json", approver1Params, "json/Order_nextAlreadyAssigned_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // test next with buyer
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/nextOrder", "json/Order_nextInvalidRole_request.json", buyerParams, "json/Order_nextInvalidRole_response.json", Status.FORBIDDEN.getStatusCode()) ;
        
        // test get with invalid order
        runJSONGetTest(ORDER_RESOURCE_PREFIX, "/999999", null, "json/Order_getInvalidOrder.json", Status.NOT_FOUND.getStatusCode()) ;
        
        // test assign with invalid order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/999999/assign", "json/Order_assignInvalidOrder_request.json", approver2Params, "json/Order_assignInvalidOrder_response.json", Status.NOT_FOUND.getStatusCode()) ;
        // test assign with invalid user
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/34/assign", "json/Order_assignInvalidUser_request.json", invalidUserParams, "json/Order_assignInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
        // test assign with order already assigned
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/assign", "json/Order_assignOrderAlreadyAssigned_request.json", approver2Params, "json/Order_assignOrderAlreadyAssigned_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // test assign with user having an order assigned
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/34/assign", "json/Order_assignUserWithAssignedOrder_request.json", approver1Params, "json/Order_assignUserWithAssignedOrder_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // test assign with buyer
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/assign", "json/Order_assignInvalidRole_request.json", buyerParams, "json/Order_assignInvalidRole_response.json", Status.FORBIDDEN.getStatusCode()) ;
        
        // test approve with invalid user
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/approve", "json/Order_approveInvalidUser_request.json", invalidUserParams, "json/Order_approveInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
        // test approve when not assigned
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/34/approve", "json/Order_approveNotAssigned_request.json", approver2Params, "json/Order_approveNotAssigned_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // test approve when order not assigned to current user
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/approve", "json/Order_approveNotAssignedUser_request.json", approver2Params, "json/Order_approveNotAssignedUser_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // test approve with invalid order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/999999/approve", "json/Order_approveInvalidOrder_request.json", approver1Params, "json/Order_approveInvalidOrder_response.json", Status.NOT_FOUND.getStatusCode()) ;

        // test reject with invalid user
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/reject", "json/Order_rejectInvalidUser_request.json", invalidUserParams, "json/Order_rejectInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
        // test reject when not assigned
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/34/reject", "json/Order_rejectNotAssigned_request.json", approver2Params, "json/Order_rejectNotAssigned_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // test reject when order not assigned to current user
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/29/reject", "json/Order_rejectNotAssignedUser_request.json", approver2Params, "json/Order_rejectNotAssignedUser_response.json", Status.FORBIDDEN.getStatusCode()) ;
        // test reject with invalid order
        runJSONPutTest(ORDER_RESOURCE_PREFIX, "/999999/reject", "json/Order_rejectInvalidOrder_request.json", approver1Params, "json/Order_rejectInvalidOrder_response.json", Status.NOT_FOUND.getStatusCode()) ;
    }
}
