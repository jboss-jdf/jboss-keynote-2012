package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class OrderAlreadyAssignedException extends JBossWorldException
{
    public OrderAlreadyAssignedException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
