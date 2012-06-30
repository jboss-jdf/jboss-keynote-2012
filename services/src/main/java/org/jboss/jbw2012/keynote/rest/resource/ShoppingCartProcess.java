package org.jboss.jbw2012.keynote.rest.resource;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.core.UriInfo ;

import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.process.ProcessController ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.Store ;
import org.jboss.jbw2012.keynote.rest.resource.utils.UpdateManager ;
import org.jboss.jbw2012.keynote.rest.retry.HibernateRetryHandler ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Transactional(retryHandlers={HibernateRetryHandler.class})
@Singleton
public class ShoppingCartProcess
{
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @Inject
    private ProcessController processController ;

    @Inject
    private UpdateManager updateManager ;
    
    public OrderDTO checkout(final long id, final UriInfo uriInfo)
    {
        final User buyer = getVerifiedUser(id) ;
        final Order order = modelUtils.checkout(buyer) ;
        processController.startProcess(order) ;
        updateManager.markDirty(0, buyer.getTeam(), buyer) ;
        return dtoUtils.getOrder(order, uriInfo.getBaseUri()) ;
        
    }
    
    private User getVerifiedUser(final long id)
    {
        final User user = modelUtils.getUser(id) ;
        if (user != null)
        {
            switch(Store.getStore().getStatus())
            {
                case VIP_ONLY:
                    if (user.isVip())
                    {
                        break ;
                    }
                    throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.STORE_VIP_ONLY)) ;
                case CLOSED:
                    throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.STORE_CLOSED)) ;
            }
            return user ;
        }
        else
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_USER)) ;
        }
    }
}
