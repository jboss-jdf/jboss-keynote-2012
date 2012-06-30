package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.math.BigDecimal ;
import java.util.List ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="shoppingCartSummary")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShoppingCartSummaryDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlElementRef
    private List<OrderItemSummaryDTO> orderItemSummaries ;
    @XmlAttribute
    private BigDecimal total ;
    
    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public ShoppingCartSummaryDTO()
    {
    }
    
    public ShoppingCartSummaryDTO(final String id, final List<OrderItemSummaryDTO> orderItemSummaries, final BigDecimal total)
    {
        this.id = id ;
        this.orderItemSummaries = orderItemSummaries ;
        this.total = total ;
    }
    
    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }

    public List<OrderItemSummaryDTO> getOrderItemSummaries()
    {
        return orderItemSummaries ;
    }
    
    public void setOrderItemSummaries(final List<OrderItemSummaryDTO> orderItemSummaries)
    {
        this.orderItemSummaries = orderItemSummaries ;
    }
    
    public BigDecimal getTotal()
    {
        return total ;
    }

    public void setTotal(final BigDecimal total)
    {
        this.total = total ;
    }
}
