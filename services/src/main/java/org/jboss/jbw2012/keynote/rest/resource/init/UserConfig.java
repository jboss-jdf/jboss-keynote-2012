package org.jboss.jbw2012.keynote.rest.resource.init;

import javax.xml.namespace.QName ;
import javax.xml.stream.XMLStreamException ;
import javax.xml.stream.XMLStreamReader ;

import org.jboss.jbw2012.keynote.model.Role ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.utils.xml.ParsingSupport ;

/**
 * Configuration parser for initial configuration data.
 * @author kevin
 */
public class UserConfig extends ParsingSupport
{
    private static final String ATTRIBUTE_NAME = "name" ;
    private static final String ATTRIBUTE_ROLE = "role" ;
    private static final String ATTRIBUTE_TEAM = "team" ;
    
    private String name ;
    private Role role ;
    private Team team ;
    
    public UserConfig(final XMLStreamReader in)
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
        else if (ATTRIBUTE_ROLE.equals(name))
        {
            this.role = Role.valueOf(attributeValue) ;
        }
        else if (ATTRIBUTE_TEAM.equals(name))
        {
            this.team = Team.valueOf(attributeValue) ;
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
    
    public User persist(final ModelUtils modelUtils)
    {
        return modelUtils.createUser(name, role, team) ;
    }
}
