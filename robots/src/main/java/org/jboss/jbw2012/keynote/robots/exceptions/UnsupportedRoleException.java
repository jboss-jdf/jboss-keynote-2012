package org.jboss.jbw2012.keynote.robots.exceptions;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;

public class UnsupportedRoleException extends JBossWorldException
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -6026288442072567436L ;

    public UnsupportedRoleException(final ErrorType errorType, final String errorMessage)
    {
        super(errorType, errorMessage) ;
    }
}
