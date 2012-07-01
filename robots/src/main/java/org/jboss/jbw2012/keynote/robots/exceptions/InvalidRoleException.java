package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class InvalidRoleException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -1042883449830552849L ;

    public InvalidRoleException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
