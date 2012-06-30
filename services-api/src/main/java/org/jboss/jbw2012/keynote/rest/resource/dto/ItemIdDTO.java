package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="itemId")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemIdDTO
{
    @XmlID
    @XmlAttribute
    private String id ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public ItemIdDTO()
    {
    }
    
    public ItemIdDTO(final String id)
    {
        this.id = id ;
    }

    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }
}
