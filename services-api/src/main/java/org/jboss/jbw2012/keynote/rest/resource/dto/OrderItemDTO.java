package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElement ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="orderItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlElement
    private ItemDTO item ;
    @XmlAttribute
    private long quantity ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public OrderItemDTO()
    {
    }
    
    public OrderItemDTO(final String id, final ItemDTO item, final long quantity)
    {
        this.id = id ;
        this.item = item ;
        this.quantity = quantity ;
    }
    
    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }
    
    public ItemDTO getItem()
    {
        return item ;
    }
    
    public void setItem(final ItemDTO item)
    {
        this.item = item ;
    }
    
    public long getQuantity()
    {
        return quantity ;
    }
    
    public void setQuantity(final long quantity)
    {
        this.quantity = quantity ;
    }
}
