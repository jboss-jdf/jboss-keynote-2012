package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class InvalidUserException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 3220669438972024477L ;

    public InvalidUserException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
