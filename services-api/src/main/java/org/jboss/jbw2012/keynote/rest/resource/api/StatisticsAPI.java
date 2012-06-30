package org.jboss.jbw2012.keynote.rest.resource.api;

import javax.ws.rs.GET ;
import javax.ws.rs.Path ;
import javax.ws.rs.Produces ;
import javax.ws.rs.core.MediaType ;

import org.jboss.jbw2012.keynote.rest.resource.dto.ProcessStatisticsDTO ;

@Path("/statistics")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface StatisticsAPI
{
    public static final String RESOURCE_PREFIX = StatisticsAPI.class.getAnnotation(Path.class).value().substring(1);

    @GET
    @Path("/process")
    public ProcessStatisticsDTO getProcessStatistics() ;
}
