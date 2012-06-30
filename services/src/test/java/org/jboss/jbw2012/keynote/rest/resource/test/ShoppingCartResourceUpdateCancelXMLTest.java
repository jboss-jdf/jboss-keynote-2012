package org.jboss.jbw2012.keynote.rest.resource.test;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.ShoppingCartResource ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class ShoppingCartResourceUpdateCancelXMLTest extends BaseResourceTest
{
    protected static final String SHOPPING_CART_RESOURCE_PREFIX = RESOURCE_PREFIX + ShoppingCartResource.RESOURCE_PREFIX ;
    
    @Test
    public void updateAndCancelShoppingCart()
        throws Exception
    {
        cancelShoppingCart() ;
        populateShoppingCart() ;

        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/removeitem", "xml/ShoppingCart_removeFirstItem.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_fourth_response.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_fourthSummary_response.xml", "application/vnd.shoppingcartsummary+xml") ;

        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/removeitem", "xml/ShoppingCart_removeSecondItem.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_fifth_response.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_fifthSummary_response.xml", "application/vnd.shoppingcartsummary+xml") ;

        cancelShoppingCart() ;
    }
    
    private void cancelShoppingCart()
        throws Exception
    {
        runXMLDeleteTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", "xml/ShoppingCart_cancel.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_empty_response.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_emptySummary_response.xml", "application/vnd.shoppingcartsummary+xml") ;
    }
    
    private void populateShoppingCart()
        throws Exception
    {
        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addFirstItem.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_first_response.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_firstSummary_response.xml", "application/vnd.shoppingcartsummary+xml") ;

        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addSecondItem.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_second_response.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_secondSummary_response.xml", "application/vnd.shoppingcartsummary+xml") ;

        runXMLPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "xml/ShoppingCart_addThirdItem.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_third_response.xml") ;
        runXMLGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "xml/ShoppingCart_thirdSummary_response.xml", "application/vnd.shoppingcartsummary+xml") ;
    }
}
