package org.jboss.jbw2012.keynote.rest.resource.test;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.ShoppingCartResource ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class ShoppingCartResourceUpdateCheckoutJSONTest extends BaseResourceTest
{
    protected static final String SHOPPING_CART_RESOURCE_PREFIX = RESOURCE_PREFIX + ShoppingCartResource.RESOURCE_PREFIX ;
    
    @Test
    public void updateAndCheckoutShoppingCart()
        throws Exception
    {
        cancelShoppingCart() ;
        populateShoppingCart() ;

        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/checkout", "json/ShoppingCart_checkout_request.json", "json/ShoppingCart_checkout_response.json") ;
    }
    
    private void cancelShoppingCart()
        throws Exception
    {
        runJSONDeleteTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", "json/ShoppingCart_cancel.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_empty_response.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_emptySummary_response.json", "application/vnd.shoppingcartsummary+json") ;
    }
    
    private void populateShoppingCart()
        throws Exception
    {
        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addFirstItem.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_first_response.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_firstSummary_response.json", "application/vnd.shoppingcartsummary+json") ;

        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addSecondItem.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_second_response.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_secondSummary_response.json", "application/vnd.shoppingcartsummary+json") ;

        runJSONPostTest(SHOPPING_CART_RESOURCE_PREFIX, "/20/additem", "json/ShoppingCart_addThirdItem.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_third_response.json") ;
        runJSONGetTest(SHOPPING_CART_RESOURCE_PREFIX, "/20", null, "json/ShoppingCart_thirdSummary_response.json", "application/vnd.shoppingcartsummary+json") ;
    }
}
