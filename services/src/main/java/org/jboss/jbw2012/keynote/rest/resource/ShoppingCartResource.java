package org.jboss.jbw2012.keynote.rest.resource;

import java.util.Collections ;
import java.util.List ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.UriInfo ;

import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.OrderItem ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.process.ProcessController ;
import org.jboss.jbw2012.keynote.rest.resource.api.ShoppingCartAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemIdDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartSummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.Store ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Singleton
public class ShoppingCartResource implements ShoppingCartAPI
{
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @Inject
    private ProcessController processController ;

    @Inject
    private ShoppingCartProcess shoppingCartProcess ;
    
    @Context
    private UriInfo uriInfo ;
    
    @Transactional
    public ShoppingCartDTO getShoppingCart(final long id)
    {
        return dtoUtils.getShoppingCart(getVerifiedUser(id).getShoppingCart(), uriInfo.getBaseUri()) ;
    }
    
    @Transactional
    public ShoppingCartSummaryDTO getShoppingCartSummary(final long id)
    {
        return dtoUtils.getShoppingCartSummary(getVerifiedUser(id).getShoppingCart(), uriInfo.getBaseUri()) ;
    }
    
    @Transactional
    public void cancel(final long id)
    {
        final List<OrderItem> orderItems = Collections.emptyList() ;
        getVerifiedUser(id).getShoppingCart().setOrderItems(orderItems) ;
    }

    public OrderDTO checkout(final long id)
    {
        processController.lock() ;
        try
        {
            return shoppingCartProcess.checkout(id, uriInfo) ;
        }
        finally
        {
            processController.unlock();
        }
    }
    
    @Transactional
    public void addToShoppingCart(final long id, final ItemIdDTO itemId)
    {
        final long itemIdKey = Long.parseLong(itemId.getId()) ;
        modelUtils.addToCart(getVerifiedUser(id), getVerifiedItem(itemIdKey)) ;
    }

    @Transactional
    public void removeFromShoppingCart(final long id, final ItemIdDTO itemId)
    {
        final long itemIdKey = Long.parseLong(itemId.getId()) ;
        modelUtils.removeFromCart(getVerifiedUser(id), getVerifiedItem(itemIdKey)) ;
    }
    
    private Item getVerifiedItem(final long id)
    {
        final Item item = modelUtils.getItem(id) ;
        if (item != null)
        {
            return item ;
        }
        else
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_ITEM)) ;
        }
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
