package org.jboss.jbw2012.keynote.rest.resource.api;

import java.util.List ;

import javax.ws.rs.GET ;
import javax.ws.rs.PUT ;
import javax.ws.rs.Path ;
import javax.ws.rs.PathParam ;
import javax.ws.rs.Produces ;
import javax.ws.rs.QueryParam ;
import javax.ws.rs.core.MediaType ;

import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.resteasy.links.AddLinks ;
import org.jboss.resteasy.links.LinkResource ;

@Path("/order")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface OrderAPI
{
    public static final String RESOURCE_PREFIX = OrderAPI.class.getAnnotation(Path.class).value().substring(1);

    @GET
    @Path("/")
    @AddLinks
    @LinkResource(value=OrderDTO.class, rel="orders")
    public List<OrderDTO> getOrders(final @QueryParam("startPosition") int startPosition, final @QueryParam("maxResults") int maxResults) ;
    
    @GET
    @Path("/open")
    @LinkResource(value=OrderDTO.class, rel="openOrders")
    @AddLinks
    public List<OrderDTO> getOpenOrders(final @QueryParam("userId") long userId, final @QueryParam("startPosition") int startPosition, final @QueryParam("maxResults") int maxResults) ;
    
    @PUT
    @Path("/nextOrder")
    @AddLinks
    @LinkResource(rel="nextOrder")
    public OrderDTO nextOrder(final @QueryParam("userId") long userId) ;
    
    @GET
    @Path("/{orderId}")
    @AddLinks
    @LinkResource
    public OrderDTO getOrder(final @PathParam("orderId") long orderId) ;
    
    @PUT
    @Path("/{orderId}/assign")
    @AddLinks
    @LinkResource(rel="assignOrder")
    public OrderDTO assignOrder(final @PathParam("orderId") long orderId, final @QueryParam("userId") long userId) ;
    
    @PUT
    @Path("/{orderId}/approve")
    @AddLinks
    @LinkResource(value=OrderDTO.class, rel="approve")
    public void approveOrder(final @PathParam("orderId") long orderId, final @QueryParam("userId") long userId) ;
    
    @PUT
    @Path("/{orderId}/reject")
    @AddLinks
    @LinkResource(value=OrderDTO.class, rel="reject")
    public void rejectOrder(final @PathParam("orderId") long orderId, final @QueryParam("userId") long userId, final @QueryParam("message") String message) ;
}
