package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.util.List ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="shoppingCart")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShoppingCartDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlElementRef
    private List<OrderItemDTO> orderItems ;
    
    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public ShoppingCartDTO()
    {
    }
    
    public ShoppingCartDTO(final String id, final List<OrderItemDTO> orderItems)
    {
        this.id = id ;
        this.orderItems = orderItems ;
    }
    
    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }

    public List<OrderItemDTO> getOrderItems()
    {
        return orderItems ;
    }
    
    public void setOrderItems(final List<OrderItemDTO> orderItems)
    {
        this.orderItems = orderItems ;
    }
}
