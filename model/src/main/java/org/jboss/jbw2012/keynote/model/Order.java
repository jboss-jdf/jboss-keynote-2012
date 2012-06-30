package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;
import java.util.List ;

import javax.persistence.CascadeType ;
import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.ManyToOne ;
import javax.persistence.NamedQueries ;
import javax.persistence.NamedQuery ;
import javax.persistence.OneToMany ;
import javax.persistence.Table ;
import javax.persistence.Transient;

@Entity
@Table(name="Orders")
@NamedQueries({@NamedQuery(name="getOpenOrders", query="select order from Order order where order.assignee is null and order.buyer.id <> :id and order.taskTeam = :team"),
    @NamedQuery(name="getNextOrder", query="select order from Order order where order.assignee is null and order.buyer.id <> :id and order.taskTeam = :team")
})
public class Order implements Serializable
{
    private Long id ;
    private User buyer ;
    private Boolean approved ;
    private String rejectionMessage ;
    private List<OrderItem> orderItems ;
    private Boolean exceedsLimit ;
    private Boolean signedOff ;
    private User approver ;
    private User assignee ;
    private long total ;
    private Long processInstanceId ;
    private Long taskId ;
    private Team taskTeam ;
    
    public Order()
    {
    }
    
    public Order(final User buyer, final List<OrderItem> orderItems)
    {
        this.buyer = buyer ;
        this.orderItems = orderItems ;
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
    public User getBuyer()
    {
        return buyer ;
    }
    
    public void setBuyer(final User buyer)
    {
        this.buyer = buyer ;
    }
    
    public Boolean getApproved()
    {
        return approved ;
    }
    
    public void setApproved(final Boolean approved)
    {
        this.approved = approved ;
    }

    public String getRejectionMessage()
    {
        return rejectionMessage ;
    }
    
    public void setRejectionMessage(final String rejectionMessage)
    {
        this.rejectionMessage = rejectionMessage ;
    }
    
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    public List<OrderItem> getOrderItems()
    {
        return orderItems ;
    }
    
    public void setOrderItems(final List<OrderItem> orderItems)
    {
        this.orderItems = orderItems ;
    }
    
    public Boolean getExceedsLimit()
    {
        return exceedsLimit ;
    }
    
    public void setExceedsLimit(final Boolean exceedsLimit)
    {
        this.exceedsLimit = exceedsLimit ;
    }
    
    public Boolean getSignedOff()
    {
        return signedOff ;
    }
    
    public void setSignedOff(final Boolean signedOff)
    {
        this.signedOff = signedOff ;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    public User getApprover()
    {
        return approver ;
    }
    
    public void setApprover(final User approver)
    {
        this.approver = approver ;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    public User getAssignee()
    {
        return assignee ;
    }
    
    public void setAssignee(final User assignee)
    {
        this.assignee = assignee ;
    }
    
    public void setTotal(final long total)
    {
        this.total = total ;
    }
    
    public long getTotal()
    {
        return total ;
    }

    @Transient
    public Float getTotalFloat()
    {
    	return Float.valueOf(total / 100);
    }

    @Transient
    public String getBuyerStatus()
    {
        final CustomerStatus customerStatus = getBuyer().getCustomerStatus() ;
        return (customerStatus == null ? null : customerStatus.getDescription()) ;
    }
    
    public void setProcessInstanceId(final Long processInstanceId)
    {
        this.processInstanceId = processInstanceId ;
    }
    
    public Long getProcessInstanceId()
    {
        return processInstanceId ;
    }
    
    public void setTaskId(final Long taskId)
    {
        this.taskId = taskId ;
    }
    
    public Long getTaskId()
    {
        return taskId ;
    }
    
    public void setTaskTeam(final Team taskTeam)
    {
        this.taskTeam = taskTeam ;
    }
    
    public Team getTaskTeam()
    {
        return taskTeam ;
    }
}
