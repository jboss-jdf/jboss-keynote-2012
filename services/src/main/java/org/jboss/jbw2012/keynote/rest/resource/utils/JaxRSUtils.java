package org.jboss.jbw2012.keynote.rest.resource.utils;

import javax.ws.rs.core.Response ;
import javax.ws.rs.core.Response.Status ;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorMessage ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;


public class JaxRSUtils
{
    public static Response getResponse(final Status status)
    {
        return Response.status(status).build() ;
    }

    public static Response getResponse(final Status status, final ErrorType errorType)
    {
        return Response.status(status).entity(new ErrorMessage(errorType)).build() ;
    }
    
    public static Response badRequest()
    {
        return getResponse(Status.BAD_REQUEST) ;
    }
    
    public static Response badRequest(final ErrorType errorType)
    {
        return getResponse(Status.BAD_REQUEST, errorType) ;
    }
    
    public static Response forbidden()
    {
        return getResponse(Status.FORBIDDEN) ;
    }
    
    public static Response forbidden(final ErrorType errorType)
    {
        return getResponse(Status.FORBIDDEN, errorType) ;
    }
    
    public static Response notFound()
    {
        return getResponse(Status.NOT_FOUND) ;
    }
    
    public static Response notFound(final ErrorType errorType)
    {
        return getResponse(Status.NOT_FOUND, errorType) ;
    }
    
    public static Response conflict()
    {
        return getResponse(Status.CONFLICT) ;
    }
    
    public static Response conflict(final ErrorType errorType)
    {
        return getResponse(Status.CONFLICT, errorType) ;
    }
}
