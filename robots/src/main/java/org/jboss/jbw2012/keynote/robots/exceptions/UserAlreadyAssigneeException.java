package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class UserAlreadyAssigneeException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 2806913252210397424L ;

    public UserAlreadyAssigneeException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
