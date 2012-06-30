package org.jboss.jbw2012.keynote.rest.resource;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.GET ;
import javax.ws.rs.Path ;
import javax.ws.rs.Produces ;
import javax.ws.rs.QueryParam ;
import javax.ws.rs.core.MediaType ;

import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.rest.resource.dto.StoreStatusDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.VipStatusDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.Store ;
import org.jboss.jbw2012.keynote.rest.resource.utils.StoreStatus ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Path("/test")
@Singleton
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class TestResource
{
    public static final String RESOURCE_PREFIX = TestResource.class.getAnnotation(Path.class).value().substring(1);
    
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @GET
    @Path("/store")
    public StoreStatusDTO storeStatus(final @QueryParam("status") StoreStatusDTO status)
    {
        if (status != null)
        {
            final String storeStatus = status.getStoreStatus() ;
            if (storeStatus == null)
            {
                throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_STORE_STATUS)) ;
            }
            try
            {
                Store.getStore().setStatus(StoreStatus.valueOf(storeStatus)) ;
            }
            catch (final IllegalArgumentException iae)
            {
                throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_STORE_STATUS)) ;
            }
        }
        return dtoUtils.getStoreStatus(Store.getStore().getStatus()) ;
    }
    
    @GET
    @Path("/vip")
    @Transactional
    public VipStatusDTO vipStatus(final @QueryParam("userId") Long userId, final @QueryParam("vip") Boolean vip)
    {
        if (userId == null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_USER)) ;
        }
        
        final User user = modelUtils.getUser(userId) ;
        if (user == null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_USER)) ;
        }
        
        if (vip != null)
        {
            user.setVip(vip) ;
        }
        
        return dtoUtils.getVipStatus(user.isVip()) ;
    }
}
