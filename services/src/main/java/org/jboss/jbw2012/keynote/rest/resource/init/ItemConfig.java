package org.jboss.jbw2012.keynote.rest.resource.init;

import java.math.BigDecimal ;

import javax.xml.namespace.QName ;
import javax.xml.stream.XMLStreamException ;
import javax.xml.stream.XMLStreamReader ;

import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.utils.xml.ParsingSupport ;

/**
 * Configuration parser for initial configuration data.
 * @author kevin
 */
public class ItemConfig extends ParsingSupport
{
    private static final String ATTRIBUTE_NAME = "name" ;
    private static final String ATTRIBUTE_IMAGE_URL = "imageURL" ;
    private static final String ATTRIBUTE_THUMBNAIL_URL = "thumbnailURL" ;
    private static final String ATTRIBUTE_DESCRIPTION = "description" ;
    private static final String ATTRIBUTE_PRICE = "price" ;
    
    private static final BigDecimal HUNDRED = new BigDecimal(100) ;
    
    private String name ;
    private String imageURL ;
    private String thumbnailURL ;
    private String description ;
    private long price ;
    
    public ItemConfig(final XMLStreamReader in)
        throws XMLStreamException
    {
        parse(in) ;
    }

    @Override
    protected void putAttribute(XMLStreamReader in, QName attributeName,
        String attributeValue) throws XMLStreamException
    {
        final String name = attributeName.getLocalPart() ;
        if (ATTRIBUTE_NAME.equals(name))
        {
            this.name = attributeValue.trim() ;
        }
        else if (ATTRIBUTE_IMAGE_URL.equals(name))
        {
            this.imageURL = attributeValue.trim() ;
        }
        else if (ATTRIBUTE_THUMBNAIL_URL.equals(name))
        {
            this.thumbnailURL = attributeValue.trim() ;
        }
        else if (ATTRIBUTE_DESCRIPTION.equals(name))
        {
            this.description = attributeValue.trim() ;
        }
        else if (ATTRIBUTE_PRICE.equals(name))
        {
            this.price = (new BigDecimal(attributeValue.trim()).multiply(HUNDRED)).longValue() ;
        }
        else
        {
            throw new XMLStreamException("Unexpected attribute name " + attributeName) ;
        }
    }

    @Override
    protected void putElement(XMLStreamReader in, QName elementName)
        throws XMLStreamException
    {
        throw new XMLStreamException("Unexpected element name " + elementName) ;
    }
    
    public Item persist(final ModelUtils modelUtils, final Category category)
    {
        return modelUtils.createItem(category, name, imageURL, thumbnailURL, description, price) ;
    }
}
