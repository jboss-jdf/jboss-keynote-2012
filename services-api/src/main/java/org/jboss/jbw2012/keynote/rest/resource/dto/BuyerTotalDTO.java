package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.math.BigDecimal ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlRootElement ;

@XmlRootElement(name="buyerTotal")
@XmlAccessorType(XmlAccessType.FIELD)
public class BuyerTotalDTO
{
    @XmlAttribute
    private BigDecimal bought ;
    @XmlAttribute
    private BigDecimal approved ;
    @XmlAttribute
    private BigDecimal rejected ;
    @XmlAttribute
    private BigDecimal awaitingApproval ;

    public BuyerTotalDTO()
    {
    }
    
    public BuyerTotalDTO(final BigDecimal bought, final BigDecimal approved, final BigDecimal rejected, final BigDecimal awaitingApproval)
    {
        this.bought = bought ;
        this.approved = approved ;
        this.rejected = rejected ;
        this.awaitingApproval = awaitingApproval ;
    }

    public BigDecimal getBought()
    {
        return bought ;
    }

    public void setBought(final BigDecimal bought)
    {
        this.bought = bought ;
    }

    public BigDecimal getApproved()
    {
        return approved ;
    }

    public void setApproved(final BigDecimal approved)
    {
        this.approved = approved ;
    }

    public BigDecimal getRejected()
    {
        return rejected ;
    }

    public void setRejected(final BigDecimal rejected)
    {
        this.rejected = rejected ;
    }

    public BigDecimal getAwaitingApproval()
    {
        return awaitingApproval ;
    }

    public void setAwaitingApproval(final BigDecimal awaitingApproval)
    {
        this.awaitingApproval = awaitingApproval ;
    }
}
