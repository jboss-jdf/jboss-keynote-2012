package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class OrderAssignedToOtherException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -2033635839520905825L ;

    public OrderAssignedToOtherException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
