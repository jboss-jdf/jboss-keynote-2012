package org.jboss.jbw2012.keynote.rest.resource.api;

import java.util.List ;

import javax.ws.rs.Consumes ;
import javax.ws.rs.GET ;
import javax.ws.rs.POST ;
import javax.ws.rs.Path ;
import javax.ws.rs.PathParam ;
import javax.ws.rs.Produces ;
import javax.ws.rs.QueryParam ;
import javax.ws.rs.core.MediaType ;

import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartSummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserCreationDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserIdDTO ;
import org.jboss.resteasy.links.AddLinks ;
import org.jboss.resteasy.links.LinkResource ;
import org.jboss.resteasy.links.LinkResources ;

@Path("/user")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface UserAPI
{
    public static final String RESOURCE_PREFIX = UserAPI.class.getAnnotation(Path.class).value().substring(1);
    
    @GET
    @Path("/")
    @AddLinks
    @LinkResources({@LinkResource(UserDTO.class), @LinkResource(UserIdDTO.class)})
    public List<UserDTO> getUsers(final @QueryParam("startPosition") int startPosition, final @QueryParam("maxResults") int maxResults) ;
    
    @POST()
    @Path("/")
    @AddLinks
    @LinkResources({@LinkResource(UserDTO.class), @LinkResource(UserIdDTO.class)})
    public UserIdDTO createUser(final UserCreationDTO userCreation) ;
    
    @GET
    @Path("/{id}")
    @AddLinks
    @LinkResources({@LinkResource(UserDTO.class), @LinkResource(UserIdDTO.class)})
    public UserDTO getUser(final @PathParam("id") long id) ;
    
    @GET
    @Path("/{id}/cart")
    @AddLinks
    @LinkResources({@LinkResource(value=UserDTO.class, rel="shoppingCart"), @LinkResource(value=UserIdDTO.class, rel="shoppingCart")})
    public ShoppingCartDTO getShoppingCart(final @PathParam("id") long id) ;
    
    @GET
    @Path("/{id}/cart")
    @AddLinks
    @Produces({"application/vnd.shoppingcartsummary+json", "application/vnd.shoppingcartsummary+xml"})
    @LinkResources({@LinkResource(value=UserDTO.class, rel="shoppingCartSummary"), @LinkResource(value=UserIdDTO.class, rel="shoppingCartSummary")})
    public ShoppingCartSummaryDTO getShoppingCartSummary(final @PathParam("id") long id) ;
    
    @GET
    @Path("/{id}/assignedOrder")
    @AddLinks
    @LinkResources({@LinkResource(value=UserDTO.class, rel="assignedOrder"), @LinkResource(value=UserIdDTO.class, rel="assignedOrder")})
    public OrderDTO getAssignedOrder(final @PathParam("id") long id) ;
}
