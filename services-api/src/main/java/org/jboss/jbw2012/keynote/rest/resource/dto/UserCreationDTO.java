package org.jboss.jbw2012.keynote.rest.resource.dto ;

import javax.xml.bind.annotation.XmlAccessType ;
import javax.xml.bind.annotation.XmlAccessorType ;
import javax.xml.bind.annotation.XmlAttribute ;
import javax.xml.bind.annotation.XmlElementRef ;
import javax.xml.bind.annotation.XmlRootElement ;

import org.jboss.resteasy.links.RESTServiceDiscovery ;

@XmlRootElement(name="userCreation")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserCreationDTO
{
    @XmlAttribute
    private String name ;
    @XmlAttribute
    private String team ;
    @XmlAttribute
    private String role ;
    @XmlAttribute
    private String password ;

    @SuppressWarnings("unused")
    @XmlElementRef
    private RESTServiceDiscovery links ;
    
    public UserCreationDTO()
    {
    }
    
    public UserCreationDTO(final String name, final String role, final String team, final String password)
    {
        this.name = name ;
        this.role = role ;
        this.team = team ;
        this.password = password ;
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
    
    public String getPassword()
    {
        return password ;
    }
    
    public void setPassword(final String password)
    {
        this.password = password ;
    }
}
