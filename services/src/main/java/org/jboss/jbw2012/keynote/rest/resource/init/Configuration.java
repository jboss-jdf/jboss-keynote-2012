package org.jboss.jbw2012.keynote.rest.resource.init;

import java.util.ArrayList ;
import java.util.List ;

import javax.xml.namespace.QName ;
import javax.xml.stream.XMLStreamException ;
import javax.xml.stream.XMLStreamReader ;

import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.utils.xml.ParsingSupport ;

/**
 * Configuration parser for initial data.
 * @author kevin
 */
public class Configuration extends ParsingSupport
{
    private static final String ELEMENT_CATEGORY = "category" ;
    private static final String ELEMENT_USER = "user" ;
    
    private List<CategoryConfig> categoryConfigs = new ArrayList<CategoryConfig>() ;
    private List<UserConfig> userConfigs = new ArrayList<UserConfig>() ;
    
    public Configuration(final XMLStreamReader in)
        throws XMLStreamException
    {
        parse(in) ;
    }

    @Override
    protected void putElement(XMLStreamReader in, QName elementName)
        throws XMLStreamException
    {
        final String name = elementName.getLocalPart() ;
        if (ELEMENT_CATEGORY.equals(name))
        {
            categoryConfigs.add(new CategoryConfig(in)) ;
        }
        else if (ELEMENT_USER.equals(name))
        {
            userConfigs.add(new UserConfig(in)) ;
        }
        else
        {
            throw new XMLStreamException("Unexpected element name " + elementName) ;
        }
    }
    
    public void persist(final ModelUtils modelUtils)
    {
        for(CategoryConfig categoryConfig: categoryConfigs)
        {
            categoryConfig.persist(modelUtils) ;
        }
        
        for(UserConfig userConfig: userConfigs)
        {
            userConfig.persist(modelUtils) ;
        }
    }
}
