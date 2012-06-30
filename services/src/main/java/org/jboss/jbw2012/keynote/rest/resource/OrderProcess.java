package org.jboss.jbw2012.keynote.rest.resource;

import java.util.Collections ;
import java.util.List ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.ws.rs.core.UriInfo ;

import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.Role ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.process.ProcessController ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.errors.ErrorType ;
import org.jboss.jbw2012.keynote.rest.resource.utils.DTOUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.JaxRSUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.Store ;
import org.jboss.jbw2012.keynote.rest.resource.utils.StoreStatus ;
import org.jboss.jbw2012.keynote.rest.resource.utils.UpdateManager ;
import org.jboss.jbw2012.keynote.rest.retry.HibernateRetryHandler ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.resteasy.spi.NoLogWebApplicationException ;

@Transactional(retryHandlers={HibernateRetryHandler.class})
@Singleton
public class OrderProcess
{
    @Inject
    private ModelUtils modelUtils ;
    
    @Inject
    private DTOUtils dtoUtils ;
    
    @Inject
    private ProcessController processController ;
    
    @Inject
    private UpdateManager updateManager ;
    
    public List<OrderDTO> getOpenOrders(final long userId, final int startPosition, final int maxResults, final UriInfo uriInfo)
    {
        checkStoreOpen() ;
        final User approver = getVerifiedUser(userId) ;
        final List<Order> openOrders = processController.getOpenOrders(approver, startPosition, maxResults) ;
        if ((openOrders != null) && (openOrders.size() > 0))
        {
            return dtoUtils.getOrders(openOrders, uriInfo.getBaseUri()) ;
        }
        else
        {
            return Collections.emptyList() ;
        }
    }
    
    public OrderDTO nextOrder(final long userId, final UriInfo uriInfo)
    {
        checkStoreOpen() ;
        final User approver = getVerifiedUser(userId) ;
        if (approver.getAssignedOrder() != null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.USER_ALREADY_ASSIGNEE)) ;
        }
        
        final Role userRole = approver.getRole() ;
        if ((userRole != Role.APPROVER) && (userRole != Role.VP))
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.INVALID_ROLE)) ;
        }
        
        final Order order = processController.getNextOrder(approver) ;
        if (order != null)
        {
            return dtoUtils.getOrder(order, uriInfo.getBaseUri()) ;
        }
        else
        {
            return null ;
        }
    }
    
    public OrderDTO assignOrder(final long orderId, final long userId, final UriInfo uriInfo)
    {
        checkStoreOpen() ;
        final Order order = getVerifiedOrder(orderId) ;
        final User user = getVerifiedUser(userId) ;
        
        if (user.getAssignedOrder() != null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.USER_ALREADY_ASSIGNEE)) ;
        }
        
        final Role userRole = user.getRole() ;
        if (userRole == Role.APPROVER)
        {
            if (user.getTeam() != order.getBuyer().getTeam())
            {
                throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.INVALID_TEAM)) ;
            }
        }
        else if (userRole != Role.VP)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.INVALID_ROLE)) ;
        }
        
        if (order.getAssignee() != null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.ORDER_ALREADY_ASSIGNED)) ;
        }
        else
        {
            processController.assign(user, order) ;
            
            return dtoUtils.getOrder(order, uriInfo.getBaseUri()) ;
        }
    }
    
    public void approveOrder(final long orderId, final long userId)
    {
        checkStoreOpen() ;
        final User user = getVerifiedUser(userId) ;
        final Order order = getVerifiedOrder(orderId) ;
        
        final User assignee = order.getAssignee() ;
        if (assignee == null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.UNASSIGNED_ORDER)) ;
        }
        else if (!assignee.getId().equals(user.getId()))
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.ORDER_ASSIGNED_TO_OTHER)) ;
        }
        
        final Role role = user.getRole() ;
        if (Role.APPROVER == role)
        {
            approve(order, user, true, null) ;
        }
        else if (Role.VP == role)
        {
            signOff(order, user, true, null) ;
        }
    }
    
    public void rejectOrder(final long orderId, final long userId, final String message)
    {
        checkStoreOpen() ;
        final User user = getVerifiedUser(userId) ;
        final Order order = getVerifiedOrder(orderId) ;
        
        final User assignee = order.getAssignee() ;
        if (assignee == null)
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.UNASSIGNED_ORDER)) ;
        }
        else if (!assignee.getId().equals(user.getId()))
        {
            throw new NoLogWebApplicationException(JaxRSUtils.forbidden(ErrorType.ORDER_ASSIGNED_TO_OTHER)) ;
        }
        
        final Role role = user.getRole() ;
        if (Role.APPROVER == role)
        {
            approve(order, user, false, message) ;
        }
        else if (Role.VP == role)
        {
            signOff(order, user, false, message) ;
        }
    }
    
    private void approve(final Order order, final User approver, final boolean approved, final String message)
    {
        if (!approved || (Boolean.TRUE != order.getExceedsLimit()))
        {
            final User buyer = order.getBuyer() ;
            final long total = (approved ? order.getTotal() : 0) ;
            updateManager.markDirty(total, buyer.getTeam(), buyer) ;
        }
        processController.approve(approver, order, approved, message) ;
    }
    
    private void signOff(final Order order, final User vp, final boolean approved, final String message)
    {
        final User buyer = order.getBuyer() ;
        final long total = (approved ? order.getTotal() : 0) ;
        updateManager.markDirty(total, buyer.getTeam(), buyer) ;
        processController.signOff(vp, order, approved, message) ;
    }
    
    private User getVerifiedUser(final long id)
    {
        final User user = modelUtils.getUser(id) ;
        if (user != null)
        {
            return user ;
        }
        else
        {
            throw new NoLogWebApplicationException(JaxRSUtils.notFound(ErrorType.INVALID_USER)) ;
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
