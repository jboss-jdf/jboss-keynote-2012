package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.math.BigDecimal ;
import java.util.List ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElement ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDTO
{
    @XmlID
    @XmlAttribute
    private String id ;
    @XmlElement
    private UserIdDTO buyer ;
    @XmlAttribute
    private Boolean approved ;
    @XmlElement
    private String rejectionMessage ;
    @XmlElementRef
    private List<OrderItemDTO> orderItems ;
    @XmlAttribute
    private Boolean exceedsLimit ;
    @XmlAttribute
    private Boolean signedOff ;
    @XmlElement
    private UserIdDTO approver ;
    @XmlElement
    private UserIdDTO assignee ;
    @XmlAttribute
    private BigDecimal total ;
    
    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public OrderDTO()
    {
    }
    
    public OrderDTO(final String id, final UserIdDTO buyer, final Boolean approved, final String rejectionMessage,
        final List<OrderItemDTO> orderItems, final Boolean exceedsLimit, final Boolean signedOff,
        final UserIdDTO approver, final UserIdDTO assignee, final BigDecimal total)
    {
        this.id = id ;
        this.buyer = buyer ;
        this.approved = approved ;
        this.rejectionMessage = rejectionMessage ;
        this.orderItems = orderItems ;
        this.exceedsLimit = exceedsLimit ;
        this.signedOff = signedOff ;
        this.approver = approver ;
        this.assignee = assignee ;
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

    public List<OrderItemDTO> getOrderItems()
    {
        return orderItems ;
    }
    
    public void setOrderItems(final List<OrderItemDTO> orderItems)
    {
        this.orderItems = orderItems ;
    }
    
    public UserIdDTO getBuyer()
    {
        return buyer ;
    }
    
    public void setBuyer(final UserIdDTO buyer)
    {
        this.buyer = buyer ;
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
    
    public UserIdDTO getApprover()
    {
        return approver ;
    }
    
    public void setApprover(final UserIdDTO approver)
    {
        this.approver = approver ;
    }
    
    public UserIdDTO getAssignee()
    {
        return assignee ;
    }
    
    public void setAssignee(final UserIdDTO assignee)
    {
        this.assignee = assignee ;
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
