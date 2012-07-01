package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class InvalidCredentialsException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 7136379898016333750L ;

    public InvalidCredentialsException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
