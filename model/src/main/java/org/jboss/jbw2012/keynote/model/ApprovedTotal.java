package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;

import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.Id ;
import javax.persistence.OneToOne ;
import javax.persistence.Table ;

@Entity
@Table(name="ApprovedTotal")
public class ApprovedTotal implements Serializable
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 675291529392071333L ;

    private Long id ;
    private User buyer ;
    private long approved ;
    private long rejected ;
    
    public ApprovedTotal()
    {
    }
    
    public ApprovedTotal(final Long id, final User buyer)
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
    
    public long getApproved()
    {
        return approved ;
    }

    public void setApproved(final long approved)
    {
        this.approved = approved ;
    }
    
    public long getRejected()
    {
        return rejected ;
    }

    public void setRejected(final long rejected)
    {
        this.rejected = rejected ;
    }
}
