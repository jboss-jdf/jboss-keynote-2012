package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="orderItemSummary")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemSummaryDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlAttribute
    private String itemName ;
    @XmlAttribute
    private String imageURL ;
    @XmlAttribute
    private String thumbnailURL ;
    @XmlAttribute
    private long quantity ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public OrderItemSummaryDTO()
    {
    }
    
    public OrderItemSummaryDTO(final String id, final String itemName, final String imageURL, final String thumbnailURL, final long quantity)
    {
        this.id = id ;
        this.itemName = itemName ;
        this.imageURL = imageURL ;
        this.thumbnailURL = thumbnailURL ;
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
    
    public String getItemName()
    {
        return itemName ;
    }
    
    public void setItemName(final String itemName)
    {
        this.itemName = itemName ;
    }
    
    public String getImageURL()
    {
        return imageURL ;
    }
    
    public void setImageURL(final String imageURL)
    {
        this.imageURL = imageURL ;
    }
    
    public String getThumbnailURL()
    {
        return thumbnailURL ;
    }
    
    public void setThumbnailURL(final String thumbnailURL)
    {
        this.thumbnailURL = thumbnailURL ;
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
