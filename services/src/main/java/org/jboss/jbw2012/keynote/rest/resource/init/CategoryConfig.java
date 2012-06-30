package org.jboss.jbw2012.keynote.rest.resource.init;

import java.util.ArrayList ;
import java.util.List ;

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
public class CategoryConfig extends ParsingSupport
{
    private static final String ELEMENT_ITEM = "item" ;
    private static final String ATTRIBUTE_NAME = "name" ;
    
    private List<ItemConfig> itemConfigs = new ArrayList<ItemConfig>() ;
    private String name ;
    
    public CategoryConfig(final XMLStreamReader in)
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
        else
        {
            throw new XMLStreamException("Unexpected attribute name " + attributeName) ;
        }
    }

    @Override
    protected void putElement(XMLStreamReader in, QName elementName)
        throws XMLStreamException
    {
        final String name = elementName.getLocalPart() ;
        if (ELEMENT_ITEM.equals(name))
        {
            itemConfigs.add(new ItemConfig(in)) ;
        }
        else
        {
            throw new XMLStreamException("Unexpected element name " + elementName) ;
        }
    }
    
    public Category persist(final ModelUtils modelUtils)
    {
        final Category category = modelUtils.createCategory(name) ;
        
        final List<Item> items = new ArrayList<Item>() ;
        for(ItemConfig itemConfig: itemConfigs)
        {
            items.add(itemConfig.persist(modelUtils, category)) ;
        }
        category.setItems(items) ;
        return category ;
    }
}
