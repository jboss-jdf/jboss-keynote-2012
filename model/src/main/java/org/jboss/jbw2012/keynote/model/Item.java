package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;

import javax.persistence.Column ;
import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.ManyToOne ;

@Entity
public class Item implements Serializable
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 8481778755631902261L ;

    private Long id ;
    private Category category ;
    private String name ;
    private String imageURL ;
    private String thumbnailURL ;
    private String description ;
    private long price ;
    
    public Item()
    {
    }
    
    public Item(final Category category, final String name, final String imageURL,
        final String thumbnailURL, final String description, final long price)
    {
        this.category = category ;
        this.name = name ;
        this.imageURL = imageURL ;
        this.thumbnailURL = thumbnailURL ;
        this.description = description ;
        this.price = price ;
    }

    @Id
    @GeneratedValue
    public Long getId()
    {
        return id ;
    }

    public void setId(final Long id)
    {
        this.id = id ;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    public Category getCategory()
    {
        return category ;
    }
    
    public void setCategory(final Category category)
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
    
    @Column(length=1024)
    public String getDescription()
    {
        return description ;
    }
    
    public void setDescription(final String description)
    {
        this.description = description ;
    }
    
    public long getPrice()
    {
        return price ;
    }
    
    public void setPrice(final long price)
    {
        this.price = price ;
    }
}
