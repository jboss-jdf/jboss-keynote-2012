package org.jboss.jbw2012.keynote.rest.resource;

import java.io.InputStream ;

import javax.inject.Singleton ;
import javax.ws.rs.GET ;
import javax.ws.rs.Path ;
import javax.ws.rs.PathParam ;
import javax.ws.rs.Produces ;

import org.jboss.jbw2012.keynote.utils.ClassLoaderUtils ;

@Path("/images")
@Singleton
public class ImagesResource
{
    @GET
    @Path("/{name}")
    @Produces("image/jpeg")
    public InputStream getImage(final @PathParam("name") String name)
    {
        return ClassLoaderUtils.getResourceAsStream("/images/" + name, getClass()) ;
    }
}
