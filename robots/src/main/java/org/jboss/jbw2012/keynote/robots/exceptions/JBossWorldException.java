package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

abstract class JBossWorldException extends RuntimeException
{
    private final ErrorType errorType ;
    
    public JBossWorldException(final ErrorType errorType, final String errorMessage)
    {
        super(errorMessage) ;
        this.errorType = errorType ;
    }

    public ErrorType getErrorType()
    {
        return errorType;
    }
}
