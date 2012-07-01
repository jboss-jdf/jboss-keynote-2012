package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class InvalidItemException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -3510162156172431778L ;

    public InvalidItemException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
