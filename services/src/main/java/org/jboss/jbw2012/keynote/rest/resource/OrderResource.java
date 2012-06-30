package org.jboss.jbw2012.keynote.rest.resource;

import java.util.List ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.UriInfo ;

import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.process.ProcessController ;
import org.jboss.jbw2012.keynote.rest.resource.api.OrderAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.Store ;
import org.jboss.jbw2012.keynote.rest.resource.utils.StoreStatus ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Singleton
public class OrderResource implements OrderAPI
{
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @Inject
    private ProcessController processController ;
    
    @Inject
    private OrderProcess orderProcess ;
    
    @Context
    private UriInfo uriInfo ;
    
    @Transactional
    public List<OrderDTO> getOrders(final int startPosition, final int maxResults)
    {
        checkStoreOpen() ;
        return dtoUtils.getOrders(modelUtils.getOrders(startPosition, maxResults), uriInfo.getBaseUri()) ;
    }
    
    public List<OrderDTO> getOpenOrders(final long userId, final int startPosition, final int maxResults)
    {
        processController.lock() ;
        try
        {
            return orderProcess.getOpenOrders(userId, startPosition, maxResults, uriInfo) ;
        }
        finally
        {
            processController.unlock() ;
        }
    }
    
    public OrderDTO nextOrder(final long userId)
    {
        processController.lock() ;
        try
        {
            return orderProcess.nextOrder(userId, uriInfo) ;
        }
        finally
        {
            processController.unlock() ;
        }
    }
    
    @Transactional
    public OrderDTO getOrder(final long orderId)
    {
        checkStoreOpen() ;
        return dtoUtils.getOrder(getVerifiedOrder(orderId), uriInfo.getBaseUri()) ;
    }
    
    public OrderDTO assignOrder(final long orderId, final long userId)
    {
        processController.lock() ;
        try
        {
            return orderProcess.assignOrder(orderId, userId, uriInfo) ;
        }
        finally
        {
            processController.unlock() ;
        }
    }
    
    public void approveOrder(final long orderId, final long userId)
    {
        processController.lock() ;
        try
        {
            orderProcess.approveOrder(orderId, userId) ;
        }
        finally
        {
            processController.unlock() ;
        }
    }
    
    public void rejectOrder(final long orderId, final long userId, final String message)
    {
        processController.lock() ;
        try
        {
            orderProcess.rejectOrder(orderId, userId, message) ;
        }
        finally
        {
            processController.unlock() ;
        }
    }
    
    private Order getVerifiedOrder(final long orderId)
    {
        final Order order = modelUtils.getOrder(orderId) ;
        if (order != null)
        {
            return order ;
        }
        else
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_ORDER)) ;
        }
    }
    
    private void checkStoreOpen()
    {
        if (Store.getStore().getStatus() == StoreStatus.CLOSED)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.STORE_CLOSED)) ;
        }
    }
}
