package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MediaType ;
import javax.ws.rs.core.Response.Status ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.CategoryResource ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class CategoryResourceTest extends BaseResourceTest
{
    protected static final String CATEGORY_RESOURCE_PREFIX = RESOURCE_PREFIX + CategoryResource.RESOURCE_PREFIX ;
    
    @Test
    public void testGetCategoriesSummaryAsXML()
        throws Exception
    {
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "", null, "xml/Category_getCategoriesSummary.xml", "application/vnd.categorysummary+xml") ;
    }
    
    @Test
    public void testGetCategoriesAsXML()
        throws Exception
    {
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "", null, "xml/Category_getCategories.xml") ;
    }
    
    @Test
    public void testGetCategoriesMapAsXML()
        throws Exception
    {
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "", null, "xml/Category_getCategoriesMap.xml", "application/vnd.categorymap+xml") ;
    }

    @Test
    public void testGetCategoryAsXML()
        throws Exception
    {
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/1", null, "xml/Category_getCategory_1.xml") ;
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/7", null, "xml/Category_getCategory_7.xml") ;
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/13", null, "xml/Category_getCategory_13.xml") ;
    }

    @Test
    public void testGetCategoryItemsAsXML()
        throws Exception
    {
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/1/items", null, "xml/Category_getCategoryItems_1.xml") ;
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/7/items", null, "xml/Category_getCategoryItems_7.xml") ;
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/13/items", null, "xml/Category_getCategoryItems_13.xml") ;
    }

    @Test
    public void testGetInvalidCategoryAsXML()
        throws Exception
    {
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/999999", null, "xml/Category_getInvalidCategory.xml", MediaType.APPLICATION_XML, Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testGetInvalidCategoryItemsAsXML()
        throws Exception
    {
        runXMLGetTest(CATEGORY_RESOURCE_PREFIX, "/999999/items", null, "xml/Category_getInvalidCategoryItems.xml", MediaType.APPLICATION_XML, Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void testGetCategoriesSummaryAsJSON()
        throws Exception
    {
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "", null, "json/Category_getCategoriesSummary.json", "application/vnd.categorysummary+json") ;
    }
    
    @Test
    public void testGetCategoriesAsJSON()
        throws Exception
    {
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "", null, "json/Category_getCategories.json") ;
    }
    
    @Test
    public void testGetCategoriesMapAsJSON()
        throws Exception
    {
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "", null, "json/Category_getCategoriesMap.json", "application/vnd.categorymap+json") ;
    }

    @Test
    public void testGetCategoryAsJSON()
        throws Exception
    {
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/1", null, "json/Category_getCategory_1.json") ;
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/7", null, "json/Category_getCategory_7.json") ;
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/13", null, "json/Category_getCategory_13.json") ;
    }

    @Test
    public void testGetCategoryItemsAsJSON()
        throws Exception
    {
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/1/items", null, "json/Category_getCategoryItems_1.json") ;
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/7/items", null, "json/Category_getCategoryItems_7.json") ;
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/13/items", null, "json/Category_getCategoryItems_13.json") ;
    }

    @Test
    public void testGetInvalidCategoryAsJSON()
        throws Exception
    {
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/999999", null, "json/Category_getInvalidCategory.json", MediaType.APPLICATION_JSON, Status.NOT_FOUND.getStatusCode()) ;
    }

    @Test
    public void testGetInvalidCategoryItemsAsJSON()
        throws Exception
    {
        runJSONGetTest(CATEGORY_RESOURCE_PREFIX, "/999999/items", null, "json/Category_getInvalidCategoryItems.json", MediaType.APPLICATION_JSON, Status.NOT_FOUND.getStatusCode()) ;
    }
}
