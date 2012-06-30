package org.jboss.jbw2012.keynote.errai.events ;

public class UserInfo
{
    private final Long id ;
    private final String name ;
    private final String team ;
    private final String role ;

    public UserInfo(final Long id, final String name, final String role,
        final String team)
    {
        this.id = id ;
        this.name = name ;
        this.role = role ;
        this.team = team ;
    }

    public Long getId()
    {
        return id ;
    }

    public String getName()
    {
        return name ;
    }

    public String getTeam()
    {
        return team ;
    }

    public String getRole()
    {
        return role ;
    }
}
