package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.util.List ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="category")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlAttribute
    private String name ;
    @XmlElementRef
    private List<ItemDTO> items ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public CategoryDTO()
    {
    }
    
    public CategoryDTO(final String id, final String name, final List<ItemDTO> items)
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
    
    public List<ItemDTO> getItems()
    {
        return items ;
    }
    
    public void setItems(final List<ItemDTO> items)
    {
        this.items = items ;
    }
}
