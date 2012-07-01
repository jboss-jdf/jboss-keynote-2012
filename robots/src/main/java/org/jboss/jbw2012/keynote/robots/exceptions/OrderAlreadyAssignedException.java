package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class OrderAlreadyAssignedException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -7581990419451575456L ;

    public OrderAlreadyAssignedException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
