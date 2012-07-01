package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class StoreClosedException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 7626882897259010196L ;

    public StoreClosedException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
