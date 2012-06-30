package org.jboss.jbw2012.keynote.rest.resource.api;

import javax.ws.rs.Consumes ;
import javax.ws.rs.DELETE ;
import javax.ws.rs.GET ;
import javax.ws.rs.POST ;
import javax.ws.rs.Path ;
import javax.ws.rs.PathParam ;
import javax.ws.rs.Produces ;
import javax.ws.rs.core.MediaType ;

import org.jboss.jbw2012.keynote.rest.resource.dto.ItemIdDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartSummaryDTO ;
import org.jboss.resteasy.links.AddLinks ;
import org.jboss.resteasy.links.LinkResource ;
import org.jboss.resteasy.links.LinkResources ;

@Path("/cart")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface ShoppingCartAPI
{
    public static final String RESOURCE_PREFIX = ShoppingCartAPI.class.getAnnotation(Path.class).value().substring(1);

    @GET
    @Path("/{id}")
    @AddLinks
    @LinkResources({@LinkResource(ShoppingCartDTO.class), @LinkResource(ShoppingCartSummaryDTO.class)})
    public ShoppingCartDTO getShoppingCart(final @PathParam("id") long id) ;
    
    @GET
    @Path("/{id}")
    @AddLinks
    @Produces({"application/vnd.shoppingcartsummary+json", "application/vnd.shoppingcartsummary+xml"})
    @LinkResources({@LinkResource(value=ShoppingCartDTO.class, rel="summary"), @LinkResource(value=ShoppingCartSummaryDTO.class, rel="summary")})
    public ShoppingCartSummaryDTO getShoppingCartSummary(final @PathParam("id") long id) ;
    
    @DELETE
    @Path("/{id}")
    @LinkResources({@LinkResource(value=ShoppingCartDTO.class, rel="cancelShoppingCart"), @LinkResource(value=ShoppingCartSummaryDTO.class, rel="cancelShoppingCart")})
    public void cancel(final @PathParam("id") long id) ;

    @POST
    @Path("/{id}/checkout")
    @AddLinks
    @LinkResources({@LinkResource(value=ShoppingCartDTO.class, rel="checkoutShoppingCart"), @LinkResource(value=ShoppingCartSummaryDTO.class, rel="checkoutShoppingCart")})
    public OrderDTO checkout(final @PathParam("id") long id) ;
    
    @POST()
    @Path("/{id}/additem")
    @LinkResources({@LinkResource(value=ShoppingCartDTO.class, rel="addToShoppingCart"), @LinkResource(value=ShoppingCartSummaryDTO.class, rel="addToShoppingCart")})
    public void addToShoppingCart(final @PathParam("id") long id, final ItemIdDTO itemId) ;

    @POST
    @Path("/{id}/removeitem")
    @LinkResources({@LinkResource(value=ShoppingCartDTO.class, rel="removeFromShoppingCart"), @LinkResource(value=ShoppingCartSummaryDTO.class, rel="removeFromShoppingCart")})
    public void removeFromShoppingCart(final @PathParam("id") long id, final ItemIdDTO itemId) ;
}
