package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MediaType ;
import javax.ws.rs.core.MultivaluedMap ;
import javax.ws.rs.core.Response.Status ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.UserResource ;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class UserResourceTest extends BaseResourceTest
{
    protected static final String USER_RESOURCE_PREFIX = RESOURCE_PREFIX + UserResource.RESOURCE_PREFIX ;
    
    @Test
    public void testGetUsersAsXML()
        throws Exception
    {
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl<String, String>() ;
        queryParams.add("startPosition", "0") ;
        queryParams.add("maxResults", "10") ;
        
        runXMLGetTest(USER_RESOURCE_PREFIX, "/", queryParams, "xml/User_getUsers.xml") ;
    }
    
    @Test
    public void testGetUserAsXML()
        throws Exception
    {
        runXMLGetTest(USER_RESOURCE_PREFIX, "/20", null, "xml/User_getUser_20.xml") ;
        runXMLGetTest(USER_RESOURCE_PREFIX, "/22", null, "xml/User_getUser_22.xml") ;
    }

    @Test
    public void testGetUserCartAsXML()
        throws Exception
    {
        runXMLGetTest(USER_RESOURCE_PREFIX, "/20/cart", null, "xml/User_getUserCart_20.xml") ;
        runXMLGetTest(USER_RESOURCE_PREFIX, "/22/cart", null, "xml/User_getUserCart_22.xml") ;
    }

    @Test
    public void testGetUserCartSummaryAsXML()
        throws Exception
    {
        runXMLGetTest(USER_RESOURCE_PREFIX, "/20/cart", null, "xml/User_getUserCartSummary_20.xml", "application/vnd.shoppingcartsummary+xml") ;
        runXMLGetTest(USER_RESOURCE_PREFIX, "/22/cart", null, "xml/User_getUserCartSummary_22.xml", "application/vnd.shoppingcartsummary+xml") ;
    }
    
    @Test
    public void testGetInvalidUserAsXML()
        throws Exception
    {
        runXMLGetTest(USER_RESOURCE_PREFIX, "/999999", null, "xml/User_getInvalidUser_response.xml", MediaType.APPLICATION_XML, Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void getGetInvalidUserCartAsXML()
        throws Exception
    {
        runXMLGetTest(USER_RESOURCE_PREFIX, "/999999/cart", null, "xml/User_getInvalidUserCart_response.xml", MediaType.APPLICATION_XML, Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void getGetInvalidUserCartSummaryAsXML()
        throws Exception
    {
        runXMLGetTest(USER_RESOURCE_PREFIX, "/999999/cart", null, "xml/User_getInvalidUserCartSummary_response.xml", "application/vnd.shoppingcartsummary+xml", Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void getGetInvalidUserAssignOrderAsXML()
        throws Exception
    {
        runXMLGetTest(USER_RESOURCE_PREFIX, "/999999/assignedOrder", null, "xml/User_getInvalidUserAssignedOrder_response.xml", MediaType.APPLICATION_XML, Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testCreateBuyerWithInvalidTeamAsXML()
        throws Exception
    {
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/User_createBuyerInvalidTeam_request.xml", "xml/User_createBuyerInvalidTeam_response.xml", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateApproverWithInvalidTeamAsXML()
        throws Exception
    {
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/User_createApproverInvalidTeam_request.xml", "xml/User_createApproverInvalidTeam_response.xml", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateApproverWithInvalidPasswordAsXML()
        throws Exception
    {
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/User_createApproverInvalidPassword_request.xml", "xml/User_createApproverInvalidPassword_response.xml", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateVPWithInvalidPasswordAsXML()
        throws Exception
    {
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/User_createVPInvalidPassword_request.xml", "xml/User_createVPInvalidPassword_response.xml", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateMissingRoleAsXML()
        throws Exception
    {
        runXMLPostTest(USER_RESOURCE_PREFIX, "/", "xml/User_createUserMissingRole_request.xml", "xml/User_createUserMissingRole_response.xml", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testGetUsersAsJSON()
        throws Exception
    {
        final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl<String, String>() ;
        queryParams.add("startPosition", "0") ;
        queryParams.add("maxResults", "10") ;

        runJSONGetTest(USER_RESOURCE_PREFIX, "/", queryParams, "json/User_getUsers.json") ;
    }
    
    @Test
    public void testGetUserAsJSON()
        throws Exception
    {
        runJSONGetTest(USER_RESOURCE_PREFIX, "/20", null, "json/User_getUser_20.json") ;
        runJSONGetTest(USER_RESOURCE_PREFIX, "/22", null, "json/User_getUser_22.json") ;
    }
    
    @Test
    public void testGetUserCartAsJSON()
        throws Exception
    {
        runJSONGetTest(USER_RESOURCE_PREFIX, "/20/cart", null, "json/User_getUserCart_20.json") ;
        runJSONGetTest(USER_RESOURCE_PREFIX, "/22/cart", null, "json/User_getUserCart_22.json") ;
    }

    @Test
    public void testGetUserCartSummaryAsJSON()
        throws Exception
    {
        runJSONGetTest(USER_RESOURCE_PREFIX, "/20/cart", null, "json/User_getUserCartSummary_20.json", "application/vnd.shoppingcartsummary+json") ;
        runJSONGetTest(USER_RESOURCE_PREFIX, "/22/cart", null, "json/User_getUserCartSummary_22.json", "application/vnd.shoppingcartsummary+json") ;
    }
    
    @Test
    public void testGetInvalidUserAsJSON()
        throws Exception
    {
        runJSONGetTest(USER_RESOURCE_PREFIX, "/999999", null, "json/User_getInvalidUser_response.json", MediaType.APPLICATION_JSON, Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void getGetInvalidUserCartAsJSON()
        throws Exception
    {
        runJSONGetTest(USER_RESOURCE_PREFIX, "/999999/cart", null, "json/User_getInvalidUserCart_response.json", MediaType.APPLICATION_JSON, Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void getGetInvalidUserCartSummaryAsJSON()
        throws Exception
    {
        runJSONGetTest(USER_RESOURCE_PREFIX, "/999999/cart", null, "json/User_getInvalidUserCartSummary_response.json", "application/vnd.shoppingcartsummary+json", Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void getGetInvalidUserAssignOrderAsJSON()
        throws Exception
    {
        runJSONGetTest(USER_RESOURCE_PREFIX, "/999999/assignedOrder", null, "json/User_getInvalidUserAssignedOrder_response.json", MediaType.APPLICATION_JSON, Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testCreateBuyerWithInvalidTeamAsJSON()
        throws Exception
    {
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/User_createBuyerInvalidTeam_request.json", "json/User_createBuyerInvalidTeam_response.json", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateApproverWithInvalidTeamAsJSON()
        throws Exception
    {
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/User_createApproverInvalidTeam_request.json", "json/User_createApproverInvalidTeam_response.json", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateApproverWithInvalidPasswordAsJSON()
        throws Exception
    {
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/User_createApproverInvalidPassword_request.json", "json/User_createApproverInvalidPassword_response.json", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateVPWithInvalidPasswordAsJSON()
        throws Exception
    {
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/User_createVPInvalidPassword_request.json", "json/User_createVPInvalidPassword_response.json", Status.FORBIDDEN.getStatusCode()) ;
    }
    
    @Test
    public void testCreateMissingRoleAsJSON()
        throws Exception
    {
        runJSONPostTest(USER_RESOURCE_PREFIX, "/", "json/User_createUserMissingRole_request.json", "json/User_createUserMissingRole_response.json", Status.FORBIDDEN.getStatusCode()) ;
    }
}
