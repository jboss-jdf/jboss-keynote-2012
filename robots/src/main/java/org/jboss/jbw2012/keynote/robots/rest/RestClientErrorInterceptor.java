package org.jboss.jbw2012.keynote.robots.rest;

import java.io.IOException ;

import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorMessage ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidCategoryException ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidCredentialsException ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidItemException ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidOrderException ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidRoleException ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidStoreStatusException ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidTeamException ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidUserException ;
import org.jboss.jbw2012.keynote.robots.exceptions.MissingRoleException ;
import org.jboss.jbw2012.keynote.robots.exceptions.OrderAlreadyAssignedException ;
import org.jboss.jbw2012.keynote.robots.exceptions.OrderAssignedToOtherException ;
import org.jboss.jbw2012.keynote.robots.exceptions.ServerSideException ;
import org.jboss.jbw2012.keynote.robots.exceptions.StoreClosedException ;
import org.jboss.jbw2012.keynote.robots.exceptions.UnassignedOrderException ;
import org.jboss.jbw2012.keynote.robots.exceptions.UnsupportedRoleException ;
import org.jboss.jbw2012.keynote.robots.exceptions.UserAlreadyAssigneeException ;
import org.jboss.resteasy.client.ClientResponse ;
import org.jboss.resteasy.client.ClientResponseFailure ;
import org.jboss.resteasy.client.core.BaseClientResponse ;
import org.jboss.resteasy.client.core.ClientErrorInterceptor ;

class RestClientErrorInterceptor implements ClientErrorInterceptor
{
    @Override
    public void handle(final ClientResponse<?> response)
        throws RuntimeException
    {
        try
        {
            final BaseClientResponse<?> baseClientResponse = (BaseClientResponse<?>)response ;
            baseClientResponse.getStreamFactory().getInputStream().reset() ;
            
            try
            {
                final ErrorMessage errorMessage = response.getEntity(ErrorMessage.class)  ;
                final ErrorType type = ErrorType.valueOf(errorMessage.getType()) ;
                final String message = errorMessage.getMessage() ;
                switch(type)
                {
                    case INVALID_CATEGORY:
                        throw new InvalidCategoryException(type, message) ;
                    case INVALID_CREDENTIALS:
                        throw new InvalidCredentialsException(type, message) ;
                    case INVALID_ITEM:
                        throw new InvalidItemException(type, message) ;
                    case INVALID_ORDER:
                        throw new InvalidOrderException(type, message) ;
                    case INVALID_ROLE:
                        throw new InvalidRoleException(type, message) ;
                    case INVALID_STORE_STATUS:
                        throw new InvalidStoreStatusException(type, message) ;
                    case INVALID_TEAM:
                        throw new InvalidTeamException(type, message) ;
                    case INVALID_USER:
                        throw new InvalidUserException(type, message) ;
                    case MISSING_ROLE:
                        throw new MissingRoleException(type, message) ;
                    case ORDER_ALREADY_ASSIGNED:
                        throw new OrderAlreadyAssignedException(type, message) ;
                    case ORDER_ASSIGNED_TO_OTHER:
                        throw new OrderAssignedToOtherException(type, message) ;
                    case STORE_CLOSED:
                        throw new StoreClosedException(type, message) ;
                    case UNASSIGNED_ORDER:
                        throw new UnassignedOrderException(type, message) ;
                    case UNSUPPORTED_ROLE:
                        throw new UnsupportedRoleException(type, message) ;
                    case USER_ALREADY_ASSIGNEE:
                        throw new UserAlreadyAssigneeException(type, message) ;
                }
            }
            catch (final ClientResponseFailure crf)
            {
                baseClientResponse.getStreamFactory().getInputStream().reset() ;
                final String errorMessage = response.getEntity(String.class) ;
                throw new ServerSideException(errorMessage) ;
            }
        }
        catch (final IOException ioe) {} // ignore
    }
}
