package org.jboss.jbw2012.keynote.rest.resource.dto.errors ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlRootElement ;


@XmlRootElement(name="errorMessage")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorMessage
{
    @XmlAttribute
    private String message ;
    @XmlAttribute
    private String type ;
    
    public ErrorMessage()
    {
    }
    
    public ErrorMessage(final ErrorType errorType)
    {
        this.message = errorType.getMessage() ;
        this.type = errorType.getType() ;
    }
    
    public String getMessage()
    {
        return message ;
    }

    public void setMessage(final String message)
    {
        this.message = message ;
    }
    
    public String getType()
    {
        return type ;
    }

    public void setType(final String type)
    {
        this.type = type ;
    }
}