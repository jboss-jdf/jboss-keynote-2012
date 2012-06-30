package org.jboss.jbw2012.keynote.rest.resource.test;

import javax.ws.rs.core.MediaType ;
import javax.ws.rs.core.Response.Status ;

import org.jboss.arquillian.container.test.api.RunAsClient ;
import org.jboss.arquillian.junit.Arquillian ;
import org.jboss.jbw2012.keynote.rest.resource.ItemResource ;
import org.junit.Test ;
import org.junit.runner.RunWith ;

@RunWith(Arquillian.class)
@RunAsClient
public class ItemResourceTest extends BaseResourceTest
{
    protected static final String ITEM_RESOURCE_PREFIX = RESOURCE_PREFIX + ItemResource.RESOURCE_PREFIX ;
    
    @Test
    public void testGetItemAsXML()
        throws Exception
    {
        runXMLGetTest(ITEM_RESOURCE_PREFIX, "/2", null, "xml/Item_getItem_2.xml") ;
        runXMLGetTest(ITEM_RESOURCE_PREFIX, "/8", null, "xml/Item_getItem_8.xml") ;
        runXMLGetTest(ITEM_RESOURCE_PREFIX, "/14", null, "xml/Item_getItem_14.xml") ;
    }
    
    @Test
    public void testGetInvalidItemAsXML()
        throws Exception
    {
        runXMLGetTest(ITEM_RESOURCE_PREFIX, "/999999", null, "xml/Item_getInvalidItem.xml", MediaType.APPLICATION_XML, Status.NOT_FOUND.getStatusCode()) ;
    }
    
    @Test
    public void testGetItemAsJSON()
        throws Exception
    {
        runJSONGetTest(ITEM_RESOURCE_PREFIX, "/2", null, "json/Item_getItem_2.json") ;
        runJSONGetTest(ITEM_RESOURCE_PREFIX, "/8", null, "json/Item_getItem_8.json") ;
        runJSONGetTest(ITEM_RESOURCE_PREFIX, "/14", null, "json/Item_getItem_14.json") ;
    }
    
    @Test
    public void testGetInvalidItemAsJSON()
        throws Exception
    {
        runJSONGetTest(ITEM_RESOURCE_PREFIX, "/999999", null, "json/Item_getInvalidItem.json", MediaType.APPLICATION_JSON, Status.NOT_FOUND.getStatusCode()) ;
    }
}
