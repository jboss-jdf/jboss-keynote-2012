package org.jboss.jbw2012.keynote.rest.resource ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.UriInfo ;

import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.rest.resource.api.ItemAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Transactional
@Singleton
public class ItemResource implements ItemAPI
{
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @Context
    private UriInfo uriInfo ;

    public ItemDTO getItem(final long id)
    {
        final Item item = modelUtils.getItem(id) ;
        if (item != null)
        {
            return dtoUtils.getItem(item, uriInfo.getBaseUri()) ;
        }
        throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_ITEM)) ;
    }
}
