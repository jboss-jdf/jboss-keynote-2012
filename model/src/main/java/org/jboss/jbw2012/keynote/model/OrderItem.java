package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;

import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.ManyToOne ;

@Entity
public class OrderItem implements Serializable
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -7838482870345361515L ;

    private Long id ;
    private Item item ;
    private long quantity ;
    
    public OrderItem()
    {
    }
    
    public OrderItem(final Item item, final long quantity)
    {
        this.item = item ;
        this.quantity = quantity ;
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
    public Item getItem()
    {
        return item ;
    }
    
    public void setItem(final Item item)
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
