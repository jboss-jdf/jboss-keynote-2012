package org.jboss.jbw2012.keynote.rest.resource;

import java.util.List ;
import java.util.concurrent.atomic.AtomicInteger ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.UriInfo ;

import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.Role ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.rest.resource.api.UserAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartSummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserCreationDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserIdDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.UpdateManager ;
import org.jboss.jbw2012.keynote.rest.retry.HibernateRetryHandler ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Transactional(retryHandlers={HibernateRetryHandler.class})
@Singleton
public class UserResource implements UserAPI
{
    private static final String VP_SECRET_PASSWORD = "letmein" ;
    private static final String APPROVER_SECRET_PASSWORD = "letmein" ;
    
    private static final int MAX_BUYER = 26 ;
    
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @Inject
    private UpdateManager updateManager ;
    
    @Context
    private UriInfo uriInfo ;
    
    public List<UserDTO> getUsers(final int startPosition, final int maxResults)
    {
        return dtoUtils.getUsers(modelUtils.getUsers(startPosition, maxResults)) ;
    }
    
    public UserIdDTO createUser(final UserCreationDTO userCreation)
    {
        if (userCreation.getRole() == null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.MISSING_ROLE)) ;
        }
        final Role role = Role.valueOf(userCreation.getRole()) ;
        final Team team = (userCreation.getTeam() != null ? Team.valueOf(userCreation.getTeam()) : null) ;
        final String password = userCreation.getPassword() ;
        switch(role)
        {
            case VP:
                if (!VP_SECRET_PASSWORD.equals(password))
                {
                    throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.INVALID_CREDENTIALS)) ;
                }
                
                return dtoUtils.getUserId(modelUtils.createUser(userCreation.getName(), role, Team.VP)) ;
            case APPROVER:
                if (Team.VP.equals(team) || (team == null))
                {
                    throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.INVALID_TEAM)) ;
                }
    
                if (!APPROVER_SECRET_PASSWORD.equals(password))
                {
                    throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.INVALID_CREDENTIALS)) ;
                }
                
                return dtoUtils.getUserId(modelUtils.createUser(userCreation.getName(), role, team)) ;
            case BUYER:
                if (Team.VP.equals(team) || (team == null))
                {
                    throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.INVALID_TEAM)) ;
                }
                
                final User newUser = modelUtils.createUser(userCreation.getName(), role, team) ;
                updateManager.newUser(newUser) ;
                return dtoUtils.getUserId(newUser) ;
            default:
                throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.UNSUPPORTED_ROLE)) ;
        }
    }
    
    public UserDTO getUser(final long id)
    {
        return dtoUtils.getUser(getVerifiedUser(id)) ;
    }
    
    public ShoppingCartDTO getShoppingCart(final long id)
    {
        return dtoUtils.getShoppingCart(getVerifiedUser(id).getShoppingCart(), uriInfo.getBaseUri()) ;
    }
    
    public ShoppingCartSummaryDTO getShoppingCartSummary(final long id)
    {
        return dtoUtils.getShoppingCartSummary(getVerifiedUser(id).getShoppingCart(), uriInfo.getBaseUri()) ;
    }
    
    public OrderDTO getAssignedOrder(final long id)
    {
        final Order assignedOrder = getVerifiedUser(id).getAssignedOrder() ;
        return (assignedOrder == null ? null : dtoUtils.getOrder(assignedOrder, uriInfo.getBaseUri())) ;
    }
    
    private User getVerifiedUser(final long id)
    {
        final User user = modelUtils.getUser(id) ;
        if (user != null)
        {
            return user ;
        }
        else
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_USER)) ;
        }
    }
}
