package org.jboss.jbw2012.keynote.rest.resource.dto ;

import java.math.BigDecimal ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlRootElement ;

@XmlRootElement(name="approverTotal")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApproverTotalDTO
{
    @XmlAttribute
    private BigDecimal approved ;
    @XmlAttribute
    private BigDecimal rejected ;

    public ApproverTotalDTO()
    {
    }
    
    public ApproverTotalDTO(final BigDecimal approved, final BigDecimal rejected)
    {
        this.approved = approved ;
        this.rejected = rejected ;
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
}
