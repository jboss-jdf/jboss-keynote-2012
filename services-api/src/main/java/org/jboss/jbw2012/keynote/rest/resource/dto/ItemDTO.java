package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.math.BigDecimal ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElement ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlElement(name="category")
    private CategorySummaryDTO category ;
    @XmlAttribute
    private String name ;
    @XmlAttribute
    private String imageURL ;
    @XmlAttribute
    private String thumbnailURL ;
    @XmlElement
    private String description ;
    @XmlAttribute
    private BigDecimal price ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public ItemDTO()
    {
    }
    
    public ItemDTO(final String id, final CategorySummaryDTO category, final String name,
        final String imageURL, final String thumbnailURL, final String description, final BigDecimal price)
    {
        this.id = id ;
        this.category = category ;
        this.name = name ;
        this.imageURL = imageURL ;
        this.thumbnailURL = thumbnailURL ;
        this.description = description ;
        this.price = price ;
    }

    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }
    
    public CategorySummaryDTO getCategory()
    {
        return category ;
    }
    
    public void setCategory(final CategorySummaryDTO category)
    {
        this.category = category ;
    }
    
    public String getName()
    {
        return name ;
    }
    
    public void setName(final String name)
    {
        this.name = name ;
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
    
    public String getDescription()
    {
        return description ;
    }
    
    public void setDescription(final String description)
    {
        this.description = description ;
    }
    
    public BigDecimal getPrice()
    {
        return price ;
    }
    
    public void setPrice(final BigDecimal price)
    {
        this.price = price ;
    }
}
