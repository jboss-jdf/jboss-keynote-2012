package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="userId")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserIdDTO
{
    @XmlID
    @XmlAttribute
    private String id ;

    @XmlAttribute
    private String name ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public UserIdDTO()
    {
    }
    
    public UserIdDTO(final String id, final String name)
    {
        this.id = id ;
        this.name = name ;
    }
    
    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }
    
    public String getName()
    {
        return name ;
    }
    
    public void setName(final String name)
    {
        this.name = name ;
    }
}
