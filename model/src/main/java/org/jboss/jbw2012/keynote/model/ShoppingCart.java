package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;
import java.util.List ;

import javax.persistence.ElementCollection ;
import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.OneToOne ;

@Entity
public class ShoppingCart implements Serializable
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -7959874999604354346L ;

    private Long id ;
    private List<OrderItem> orderItems ;
    private User buyer ;
    
    public ShoppingCart()
    {
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

    @ElementCollection
    public List<OrderItem> getOrderItems()
    {
        return orderItems ;
    }
    
    public void setOrderItems(final List<OrderItem> orderItems)
    {
        this.orderItems = orderItems ;
    }
    
    @OneToOne(mappedBy="shoppingCart", fetch=FetchType.LAZY)
    public User getBuyer()
    {
        return buyer ;
    }
    
    public void setBuyer(final User buyer)
    {
        this.buyer = buyer ;
    }
}
