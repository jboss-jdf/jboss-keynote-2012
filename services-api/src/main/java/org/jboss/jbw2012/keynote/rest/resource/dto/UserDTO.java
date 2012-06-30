package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElement ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlID ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO
{
    @XmlID
    @XmlAttribute
    private String id ;

    @XmlAttribute
    private String name ;
    @XmlAttribute
    private String team ;
    @XmlAttribute
    private String role ;
    @XmlElement
    private BuyerTotalDTO buyerTotal ;
    @XmlElement
    private ApproverTotalDTO approverTotal ;
    
    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public UserDTO()
    {
    }
    
    public UserDTO(final String id, final String name, final String role, final String team,
        final BuyerTotalDTO buyerTotal, final ApproverTotalDTO approverTotal)
    {
        this.id = id ;
        this.name = name ;
        this.role = role ;
        this.team = team ;
        this.buyerTotal = buyerTotal ;
        this.approverTotal = approverTotal ;
    }
    
    public String getId()
    {
        return id ;
    }

    public void setId(final String id)
    {
        this.id = id ;
    }
    
    public String getName()
    {
        return name ;
    }
    
    public void setName(final String name)
    {
        this.name = name ;
    }
    
    public String getRole()
    {
        return role ;
    }
    
    public void setRole(final String role)
    {
        this.role = role ;
    }
    
    public String getTeam()
    {
        return team ;
    }
    
    public void setTeam(final String team)
    {
        this.team = team ;
    }
    
    public BuyerTotalDTO getBuyerTotal()
    {
        return buyerTotal ;
    }
    
    public void setBuyerTotal(final BuyerTotalDTO buyerTotal)
    {
        this.buyerTotal = buyerTotal ;
    }
    
    public ApproverTotalDTO getApproverTotal()
    {
        return approverTotal ;
    }
    
    public void setApproverTotal(final ApproverTotalDTO approverTotal)
    {
        this.approverTotal = approverTotal ;
    }
}