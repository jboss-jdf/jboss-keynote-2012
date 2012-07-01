package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class InvalidStoreStatusException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 7036949862300172856L ;

    public InvalidStoreStatusException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
