package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlRootElement ;

@XmlRootElement(name="vipStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class VipStatusDTO
{
    @XmlAttribute
    private boolean vipStatus ;
    
    public VipStatusDTO()
    {
    }
    
    public VipStatusDTO(final boolean vipStatus)
    {
        this.vipStatus = vipStatus ;
    }

    public boolean getVipStatus()
    {
        return vipStatus ;
    }

    public void setVipStatus(final boolean vipStatus)
    {
        this.vipStatus = vipStatus ;
    }
}
