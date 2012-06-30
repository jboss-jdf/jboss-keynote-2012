package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MediaType ;
import javax.ws.rs.core.Response.Status ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.ShoppingCartResource ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class ShoppingCartResourceTest extends BaseResourceTest
{
    protected static final String SHOPPING_CART_RESOURCE_PREFIX = RESOURCE_PREFIX + ShoppingCartResource.RESOURCE_PREFIX ;

    @Test
    public void testGetCartAsXML()
        throws Exception
    {
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_getCart_20.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/22", null, "xml/ShoppingCart_getCart_22.xml") ;
    }

    @Test
    public void testGetCartSummaryAsXML()
        throws Exception
    {
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_getCartSummary_20.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/22", null, "xml/ShoppingCart_getCartSummary_22.xml") ;
    }

    @Test
    public void testGetCartInvalidUserAsXML()
        throws Exception
    {
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999", null, "xml/ShoppingCart_getCartInvalidUser_response.xml", MediaType.APPLICATION_XML, Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testGetCartSummaryInvalidUserAsXML()
        throws Exception
    {
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999", null, "xml/ShoppingCart_getCartSummaryInvalidUser_response.xml", "application/vnd.shoppingcartsummary+xml", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testDeleteCartInvalidUserAsXML()
        throws Exception
    {
        runXMLDeleteTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999", "xml/ShoppingCart_deleteCartInvalidUser_request.xml", "xml/ShoppingCart_deleteCartInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testCheckoutCartInvalidUserAsXML()
        throws Exception
    {
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999/checkout", "xml/ShoppingCart_checkoutInvalidUser_request.xml", "xml/ShoppingCart_checkoutInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testAddItemInvalidUserAsXML()
        throws Exception
    {
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999/additem", "xml/ShoppingCart_addItemInvalidUser_request.xml", "xml/ShoppingCart_addItemInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testAddItemInvalidItemAsXML()
        throws Exception
    {
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addItemInvalidItem_request.xml", "xml/ShoppingCart_addItemInvalidItem_response.xml", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testRemoveItemInvalidUserAsXML()
        throws Exception
    {
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999/removeitem", "xml/ShoppingCart_removeItemInvalidUser_request.xml", "xml/ShoppingCart_removeItemInvalidUser_response.xml", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testRemoveItemInvalidItemAsXML()
        throws Exception
    {
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/removeitem", "xml/ShoppingCart_removeItemInvalidItem_request.xml", "xml/ShoppingCart_removeItemInvalidItem_response.xml", Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void testGetCartAsJSON()
        throws Exception
    {
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_getCart_20.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/22", null, "json/ShoppingCart_getCart_22.json") ;
    }

    @Test
    public void testGetCartSummaryAsJSON()
        throws Exception
    {
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_getCartSummary_20.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/22", null, "json/ShoppingCart_getCartSummary_22.json") ;
    }

    @Test
    public void testGetCartInvalidUserAsJSON()
        throws Exception
    {
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999", null, "json/ShoppingCart_getCartInvalidUser_response.json", MediaType.APPLICATION_JSON, Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testGetCartSummaryInvalidUserAsJSON()
        throws Exception
    {
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999", null, "json/ShoppingCart_getCartSummaryInvalidUser_response.json", "application/vnd.shoppingcartsummary+json", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testDeleteCartInvalidUserAsJSON()
        throws Exception
    {
        runJSONDeleteTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999", "json/ShoppingCart_deleteCartInvalidUser_request.json", "json/ShoppingCart_deleteCartInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testCheckoutCartInvalidUserAsJSON()
        throws Exception
    {
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999/checkout", "json/ShoppingCart_checkoutInvalidUser_request.json", "json/ShoppingCart_checkoutInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testAddItemInvalidUserAsJSON()
        throws Exception
    {
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999/additem", "json/ShoppingCart_addItemInvalidUser_request.json", "json/ShoppingCart_addItemInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testAddItemInvalidItemAsJSON()
        throws Exception
    {
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addItemInvalidItem_request.json", "json/ShoppingCart_addItemInvalidItem_response.json", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testRemoveItemInvalidUserAsJSON()
        throws Exception
    {
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/999999/removeitem", "json/ShoppingCart_removeItemInvalidUser_request.json", "json/ShoppingCart_removeItemInvalidUser_response.json", Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testRemoveItemInvalidItemAsJSON()
        throws Exception
    {
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/removeitem", "json/ShoppingCart_removeItemInvalidItem_request.json", "json/ShoppingCart_removeItemInvalidItem_response.json", Status.NOT_FOUND.getStatusCode()) ;
    }
}
