package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlRootElement ;

@XmlRootElement(name="storeStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class StoreStatusDTO
{
    @XmlAttribute
    private String storeStatus ;
    
    public StoreStatusDTO()
    {
    }
    
    public StoreStatusDTO(final String storeStatus)
    {
        this.storeStatus = storeStatus ;
    }

    public String getStoreStatus()
    {
        return storeStatus ;
    }

    public void setStoreStatus(final String storeStatus)
    {
        this.storeStatus = storeStatus ;
    }
}
