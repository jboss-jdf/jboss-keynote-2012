package org.jboss.jbw2012.keynote.rest.resource.utils ;

import java.math.BigDecimal ;
import java.math.RoundingMode ;
import java.net.URI ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.Map ;
import java.util.TreeMap ;

import javax.inject.Singleton ;

import org.jboss.jbw2012.keynote.model.ApprovedTotal ;
import org.jboss.jbw2012.keynote.model.ApproverTotal ;
import org.jboss.jbw2012.keynote.model.BuyerTotal ;
import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.OrderItem ;
import org.jboss.jbw2012.keynote.model.ShoppingCart ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.process.ProcessStatistics ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ApproverTotalDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.BuyerTotalDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryMapDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategorySummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderItemDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderItemSummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ProcessStatisticsDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ShoppingCartSummaryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.StoreStatusDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserIdDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.VipStatusDTO ;

@Singleton
public class DTOUtils
{
    private static BigDecimal HUNDRED = new BigDecimal(100) ;
    
    public Map<String, CategoryMapDTO> getCategoriesMap(final List<Category> categories, final URI baseURI)
    {
        final Map<String, CategoryMapDTO> dtoMap = new TreeMap<String, CategoryMapDTO>() ;
        for(Category category: categories)
        {
            dtoMap.put(toString(category.getId()), getCategoryMap(category, baseURI)) ;
        }
        return dtoMap ;
    }
    
    public CategoryMapDTO getCategoryMap(final Category category, final URI baseURI)
    {
        final Map<String, ItemDTO> dtoMap = new TreeMap<String, ItemDTO>() ;
        for(Item item: category.getItems())
        {
            dtoMap.put(toString(item.getId()), getItem(item, baseURI)) ;
        }
        return new CategoryMapDTO(toString(category.getId()), category.getName(), dtoMap) ;
    }
    
    public List<CategoryDTO> getCategories(final List<Category> categories, final URI baseURI)
    {
        final List<CategoryDTO> dtos = new ArrayList<CategoryDTO>() ;
        for(Category category: categories)
        {
            dtos.add(getCategory(category, baseURI)) ;
        }
        return dtos ;
    }
    
    public List<CategorySummaryDTO> getCategoriesSummary(final List<Category> categories)
    {
        final List<CategorySummaryDTO> dtos = new ArrayList<CategorySummaryDTO>() ;
        for(Category category: categories)
        {
            dtos.add(getCategorySummary(category)) ;
        }
        return dtos ;
    }
    
    public CategoryDTO getCategory(final Category category, final URI baseURI)
    {
        return new CategoryDTO(toString(category.getId()), category.getName(), getItems(category.getItems(), baseURI)) ;
    }
    
    public CategorySummaryDTO getCategorySummary(final Category category)
    {
        return new CategorySummaryDTO(toString(category.getId()), category.getName()) ;
    }
    
    public List<ItemDTO> getItems(final List<Item> items, final URI baseURI)
    {
        final List<ItemDTO> dtos = new ArrayList<ItemDTO>() ;
        for(Item item: items)
        {
            dtos.add(getItem(item, baseURI)) ;
        }
        return dtos ;
    }
    
    public ItemDTO getItem(final Item item, final URI baseURI)
    {
        return new ItemDTO(toString(item.getId()), getCategorySummary(item.getCategory()), item.getName(),
            getURI(baseURI, item.getImageURL()), getURI(baseURI, item.getThumbnailURL()), item.getDescription(), getCurrency(item.getPrice())) ;
    }

    public UserDTO getUser(final User user)
    {
        return new UserDTO(toString(user.getId()), user.getName(), user.getRole().name(), user.getTeam().name(),
            getBuyerTotal(user.getBuyerTotal(), user.getApprovedTotal()), getApproverTotal(user.getApproverTotal())) ;
    }

    public UserIdDTO getUserId(final User user)
    {
        return new UserIdDTO(toString(user.getId()), user.getName()) ;
    }
    
    public List<UserDTO> getUsers(final List<User> users)
    {
        final List<UserDTO> dtos = new ArrayList<UserDTO>() ;
        for(User user: users)
        {
            dtos.add(new UserDTO(toString(user.getId()), user.getName(), user.getRole().name(), user.getTeam().name(),
                getBuyerTotal(user.getBuyerTotal(), user.getApprovedTotal()), getApproverTotal(user.getApproverTotal()))) ;
        }
        return dtos ;
    }

    public List<OrderItemDTO> getOrderItems(final List<OrderItem> orderItems, final URI baseURI)
    {
        final List<OrderItemDTO> dtos = new ArrayList<OrderItemDTO>() ;
        for(OrderItem orderItem: orderItems)
        {
            dtos.add(new OrderItemDTO(toString(orderItem.getId()), getItem(orderItem.getItem(), baseURI), orderItem.getQuantity())) ;
        }
        return dtos ;
    }
    
    public List<OrderItemSummaryDTO> getOrderItemSummaries(final List<OrderItem> orderItems, final URI baseURI)
    {
        final List<OrderItemSummaryDTO> dtos = new ArrayList<OrderItemSummaryDTO>() ;
        for(OrderItem orderItem: orderItems)
        {
            final Item item = orderItem.getItem() ;
            dtos.add(new OrderItemSummaryDTO(toString(orderItem.getId()), item.getName(), getURI(baseURI, item.getImageURL()), getURI(baseURI, item.getThumbnailURL()), orderItem.getQuantity())) ;
        }
        return dtos ;
    }
    
    public ShoppingCartDTO getShoppingCart(final ShoppingCart shoppingCart, final URI baseURI)
    {
        return new ShoppingCartDTO(toString(shoppingCart.getBuyer().getId()), getOrderItems(shoppingCart.getOrderItems(), baseURI)) ;
    }
    
    public ShoppingCartSummaryDTO getShoppingCartSummary(final ShoppingCart shoppingCart, final URI baseURI)
    {
        final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
        long total = 0 ;
        for(OrderItem orderItem: orderItems)
        {
            total += orderItem.getQuantity() * orderItem.getItem().getPrice() ;
        }
        return new ShoppingCartSummaryDTO(toString(shoppingCart.getBuyer().getId()), getOrderItemSummaries(orderItems, baseURI), getCurrency(total)) ;
    }
    
    public OrderDTO getOrder(final Order order, final URI baseURI)
    {
        final User approver = order.getApprover() ;
        final User assignee = order.getAssignee() ;
        
        return new OrderDTO(toString(order.getId()), getUserId(order.getBuyer()), order.getApproved(), order.getRejectionMessage(),
            getOrderItems(order.getOrderItems(), baseURI), order.getExceedsLimit(), order.getSignedOff(),
            (approver == null ? null : getUserId(approver)),
            (assignee == null ? null : getUserId(assignee)),
            getCurrency(order.getTotal())) ;
    }
    
    public List<OrderDTO> getOrders(final List<Order> orders, final URI baseURI)
    {
        final ArrayList<OrderDTO> dtos = new ArrayList<OrderDTO>() ;
        for(Order order: orders)
        {
            final User approver = order.getApprover() ;
            final User assignee = order.getAssignee() ;
            dtos.add(new OrderDTO(toString(order.getId()), getUserId(order.getBuyer()), order.getApproved(), order.getRejectionMessage(),
                    getOrderItems(order.getOrderItems(), baseURI), order.getExceedsLimit(), order.getSignedOff(),
                    (approver == null ? null : getUserId(approver)),
                    (assignee == null ? null : getUserId(assignee)),
                    getCurrency(order.getTotal()))) ;
        }
        return dtos ;
    }
    
    public ProcessStatisticsDTO getProcessStatistics(final ProcessStatistics processStatistics)
    {
        return new ProcessStatisticsDTO(processStatistics.getProcessStartedCount(), processStatistics.getProcessCompletedCount(),
            processStatistics.getNodeCounts()) ;
    }

    public StoreStatusDTO getStoreStatus(final StoreStatus status)
    {
        return new StoreStatusDTO(status.name()) ;
    }

    public VipStatusDTO getVipStatus(final boolean vip)
    {
        return new VipStatusDTO(vip) ;
    }

    private BuyerTotalDTO getBuyerTotal(final BuyerTotal buyerTotal, final ApprovedTotal approvedTotal)
    {
        if ((buyerTotal == null) && (approvedTotal == null))
        {
            return null ;
        }
        final BigDecimal totalBought = getCurrency(buyerTotal.getTotalBought()) ;
        final BigDecimal totalRejected = getCurrency(approvedTotal.getRejected()) ;
        final BigDecimal totalApproved = getCurrency(approvedTotal.getApproved()) ;
        final long awaitingApproval = buyerTotal.getTotalBought() - (approvedTotal.getRejected() + approvedTotal.getApproved()) ; 
        
        return new BuyerTotalDTO(totalBought, totalApproved, totalRejected, getCurrency(awaitingApproval)) ;
    }

    private ApproverTotalDTO getApproverTotal(final ApproverTotal approverTotal)
    {
        if (approverTotal == null)
        {
            return null ;
        }
        return new ApproverTotalDTO(getCurrency(approverTotal.getApproved()), getCurrency(approverTotal.getRejected())) ;
    }
    
    private String toString(final Long id)
    {
        return (id == null ? null : id.toString()) ;
    }
    
    private BigDecimal getCurrency(final long price)
    {
        return new BigDecimal(price).divide(HUNDRED, 2, RoundingMode.UNNECESSARY) ;
    }
    
    private String getURI(final URI baseURI, final String url)
    {
        final URI imageURI = URI.create(url) ;
        if (imageURI.isAbsolute())
        {
            return url ;
        }
        return baseURI.resolve(imageURI).toASCIIString() ;
    }
}
