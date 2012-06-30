package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class InvalidTeamException extends JBossWorldException
{
    public InvalidTeamException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
