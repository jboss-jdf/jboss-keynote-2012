package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.util.Map ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="categoryMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryMapDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlAttribute
    private String name ;
    private Map<String, ItemDTO> items ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public CategoryMapDTO()
    {
    }
    
    public CategoryMapDTO(final String id, final String name, final Map<String, ItemDTO> items)
    {
        this.id = id ;
        this.name = name ;
        this.items = items ;
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
    
    public Map<String, ItemDTO> getItems()
    {
        return items ;
    }
    
    public void setItems(final Map<String, ItemDTO> items)
    {
        this.items = items ;
    }
}
