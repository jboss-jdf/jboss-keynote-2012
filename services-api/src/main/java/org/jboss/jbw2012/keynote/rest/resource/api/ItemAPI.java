package org.jboss.jbw2012.keynote.rest.resource.api ;

import javax.ws.rs.GET ;
import javax.ws.rs.Path ;
import javax.ws.rs.PathParam ;
import javax.ws.rs.Produces ;
import javax.ws.rs.core.MediaType ;

import org.jboss.jbw2012.keynote.rest.resource.dto.ItemDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemIdDTO ;
import org.jboss.resteasy.links.AddLinks ;
import org.jboss.resteasy.links.LinkResource ;
import org.jboss.resteasy.links.LinkResources ;

@Path("/item")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface ItemAPI
{
    public static final String RESOURCE_PREFIX = ItemAPI.class.getAnnotation(Path.class).value().substring(1);

    @GET
    @Path("/{id}")
    @AddLinks
    @LinkResources({@LinkResource(ItemDTO.class), @LinkResource(ItemIdDTO.class)})
    public ItemDTO getItem(final @PathParam("id") long id) ;
}
