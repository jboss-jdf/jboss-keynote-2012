package org.jboss.jbw2012.keynote.robots.exceptions;


public class ServerSideException extends RuntimeException
{
    public ServerSideException(final String errorMessage)
    {
        super(errorMessage) ;
    }
}
