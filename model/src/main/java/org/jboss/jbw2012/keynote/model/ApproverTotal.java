package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;

import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.Id ;
import javax.persistence.OneToOne ;
import javax.persistence.Table ;

@Entity
@Table(name="ApproverTotal")
public class ApproverTotal implements Serializable
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -7970093684235141792L ;

    private Long id ;
    private User approver ;
    private long approved ;
    private long rejected ;
    
    public ApproverTotal()
    {
    }
    
    public ApproverTotal(final Long id, final User approver)
    {
        this.id = id ;
        this.approver = approver ;
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
    public User getApprover()
    {
        return approver ;
    }
    
    public void setApprover(final User approver)
    {
        this.approver = approver ;
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
