package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;

import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.Id ;
import javax.persistence.OneToOne ;
import javax.persistence.Table ;

@Entity
@Table(name="BuyerTotal")
public class BuyerTotal implements Serializable
{
    private Long id ;
    private User buyer ;
    private long totalBought ;
    
    public BuyerTotal()
    {
    }
    
    public BuyerTotal(final Long id, final User buyer)
    {
        this.id = id ;
        this.buyer = buyer ;
    }
    
    @Id
    public Long getId()
    {
        return id ;
    }

    public void setId(final Long id)
    {
        this.id = id ;
    }
    
    @OneToOne(fetch=FetchType.LAZY)
    public User getBuyer()
    {
        return buyer ;
    }
    
    public void setBuyer(final User buyer)
    {
        this.buyer = buyer ;
    }
    
    public long getTotalBought()
    {
        return totalBought ;
    }

    public void setTotalBought(final long totalBought)
    {
        this.totalBought = totalBought ;
    }
}
