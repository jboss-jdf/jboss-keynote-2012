package org.jboss.jbw2012.keynote.model.utils;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Iterator ;
import java.util.List ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.persistence.EntityManager ;
import javax.persistence.EntityManagerFactory ;
import javax.persistence.NoResultException ;
import javax.persistence.PersistenceUnit ;
import javax.persistence.TypedQuery ;
import javax.persistence.criteria.CriteriaBuilder ;
import javax.persistence.criteria.CriteriaQuery ;
import javax.persistence.criteria.Root ;

import org.jboss.jbw2012.keynote.model.ApprovedTotal ;
import org.jboss.jbw2012.keynote.model.ApproverTotal ;
import org.jboss.jbw2012.keynote.model.BuyerTotal ;
import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.CustomerStatus ;
import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.OrderItem ;
import org.jboss.jbw2012.keynote.model.Role ;
import org.jboss.jbw2012.keynote.model.ShoppingCart ;
import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.utils.TransactionalEntityManagerUtils ;

@Singleton
public class ModelUtils
{
    @PersistenceUnit(unitName="jbwdemo")
    private EntityManagerFactory emf ;

    @Inject
    private TransactionalEntityManagerUtils transactionalEntityManagerUtils ;
    
    public User createUser(final String name, final Role role, final Team team)
    {
        return createUser(name, role, team, CustomerStatus.BRONZE) ;
    }

    public User createUser(final String name, final Role role, final Team team, final CustomerStatus customerStatus)
    {
        final EntityManager em = getEntityManager() ;
        
        final ShoppingCart shoppingCart = createShoppingCart() ;
        final User user = new User(name, role, team, shoppingCart, customerStatus, false) ;
        shoppingCart.setBuyer(user) ;
        em.persist(user) ;
        user.setName(name + " - " + user.getId()) ;
        
        if (Role.BUYER == role)
        {
            final BuyerTotal buyerTotal = new BuyerTotal(user.getId(), user) ;
            em.persist(buyerTotal) ;
            final ApprovedTotal approvedTotal = new ApprovedTotal(user.getId(), user) ;
            em.persist(approvedTotal) ;
            user.setBuyerTotal(buyerTotal) ;
            user.setApprovedTotal(approvedTotal) ;
        }
        else
        {
            final ApproverTotal approverTotal = new ApproverTotal(user.getId(), user) ;
            em.persist(approverTotal) ;
            user.setApproverTotal(approverTotal) ;
        }
        
        return user ;
    }
    
    public User getUser(final long id)
    {
        return getEntityManager().find(User.class, id) ;
    }

    public List<User> getUsers(final int startPosition, final int maxResults)
    {
        final EntityManager em = getEntityManager() ;
        
        final CriteriaQuery<User> criteriaQuery = em.getCriteriaBuilder().createQuery(User.class)  ;
        final Root<User> user = criteriaQuery.from(User.class) ;
        criteriaQuery.orderBy(em.getCriteriaBuilder().asc(user.get("id"))) ;

        final TypedQuery<User> query = em.createQuery(criteriaQuery) ;
        query.setFirstResult(startPosition) ;
        query.setMaxResults(maxResults) ;
        return query.getResultList() ;
    }

    public List<User> getBuyers(final int startPosition, final int maxResults)
    {
        final TypedQuery<User> query = getEntityManager().createNamedQuery("getUsersByRole", User.class)  ;
        query.setFirstResult(startPosition) ;
        query.setMaxResults(maxResults) ;
        query.setParameter("role", Role.BUYER) ;
        return query.getResultList() ;
    }
    
    public void deleteUser(final long id)
    {
        final EntityManager em = getEntityManager() ;
        
        final User user = em.find(User.class, id) ;
        em.remove(user) ;
    }
    
    
    public Category createCategory(final String name)
    {
        final EntityManager em = getEntityManager() ;
        final Category category = new Category(name) ;
        
        em.persist(category) ;
        
        return category ;
    }
    
    public List<Category> getCategories()
    {
        final EntityManager em = getEntityManager() ;

        final CriteriaQuery<Category> query = em.getCriteriaBuilder().createQuery(Category.class) ;
        final Root<Category> category = query.from(Category.class) ;
        query.orderBy(em.getCriteriaBuilder().asc(category.get("id"))) ;
        
        return em.createQuery(query).getResultList() ;
    }
    
    public Category getCategory(final long id)
    {
        return getEntityManager().find(Category.class, id) ;
    }
    
    public void deleteCategory(final long id)
    {
        final EntityManager em = getEntityManager() ;
        final Category category = em.find(Category.class, id) ;
        em.remove(category) ;
    }
    
    
    
    
    public Item createItem(final Category category, final String name, final String imageURL,
        final String thumbnailURL, final String description, final long price)
    {
        final Item item = new Item(category, name, imageURL, thumbnailURL, description, price) ;
        
        getEntityManager().persist(item) ;
        
        return item ;
    }
    
    public List<Item> getItems()
    {
        final EntityManager em = getEntityManager() ;
        final CriteriaQuery<Item> query = em.getCriteriaBuilder().createQuery(Item.class) ;
        final Root<Item> item = query.from(Item.class) ;
        query.orderBy(em.getCriteriaBuilder().asc(item.get("id"))) ;
        
        return em.createQuery(query).getResultList() ;
    }
    
    public Item getItem(final long id)
    {
        return getEntityManager().find(Item.class, id) ;
    }
    
    public void deleteItem(final long id)
    {
        final EntityManager em = getEntityManager() ;
        final Item item = em.find(Item.class, id) ;
        em.remove(item) ;
    }
    
    
    
    public ShoppingCart createShoppingCart()
    {
        final ShoppingCart shoppingCart = new ShoppingCart() ;
        
        getEntityManager().persist(shoppingCart) ;
        
        return shoppingCart ;
    }

    public ShoppingCart getShoppingCart(final long id)
    {
        return getEntityManager().find(ShoppingCart.class, id) ;
    }
    
    public OrderItem createOrderItem(final Item item, final long quantity)
    {
        final OrderItem orderItem = new OrderItem(item, quantity) ;
        
        getEntityManager().persist(orderItem) ;
        
        return orderItem ;
    }
    
    public Order createOrder(final User buyer, final List<OrderItem> orderItems)
    {
        final Order order = new Order(buyer, orderItems) ;
        
        long total = 0 ;
        for(OrderItem orderItem: orderItems)
        {
            total += orderItem.getQuantity() * orderItem.getItem().getPrice() ;
        }
        order.setTotal(total) ;
        
        getEntityManager().persist(order) ;
        
        return order ;
    }
    
    public Order getOrder(final long id)
    {
        return getEntityManager().find(Order.class, id) ;
    }

    public List<Order> getOrders(final int startPosition, final int maxResults)
    {
        final EntityManager em = getEntityManager() ;

        final CriteriaQuery<Order> criteriaQuery = em.getCriteriaBuilder().createQuery(Order.class)  ;
        final Root<Order> order = criteriaQuery.from(Order.class) ;
        criteriaQuery.orderBy(em.getCriteriaBuilder().asc(order.get("id"))) ;

        final TypedQuery<Order> query = em.createQuery(criteriaQuery) ;
        query.setFirstResult(startPosition) ;
        query.setMaxResults(maxResults) ;
        return query.getResultList() ;
    }

    public List<Order> getOpenOrders(final long id, final int startPosition, final int maxResults)
    {
        final User approver = getUser(id) ;
        final TypedQuery<Order> query = getEntityManager().createNamedQuery("getOpenOrders", Order.class)  ;
        query.setFirstResult(startPosition) ;
        query.setMaxResults(maxResults) ;
        query.setParameter("id", id) ;
        query.setParameter("team", approver.getTeam()) ;
        return query.getResultList() ;
    }
    
    public long getOrderCount()
    {
        final EntityManager em = getEntityManager() ;

        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder()  ;
        final CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class) ;
        query.select(criteriaBuilder.count(query.from(Order.class))) ;
        
        return em.createQuery(query).getSingleResult() ;
    }

    public List<User> getUsersWithAssignedOrders(final int startPosition, final int maxResults)
    {
        final TypedQuery<User> query = getEntityManager().createNamedQuery("getUsersWithAssignedOrders", User.class)  ;
        query.setFirstResult(startPosition) ;
        query.setMaxResults(maxResults) ;
        return query.getResultList() ;
    }

    public Order getNextOrder(final long id)
    {
        final User approver = getUser(id) ;
        final TypedQuery<Order> query = getEntityManager().createNamedQuery("getNextOrder", Order.class)  ;
        query.setParameter("id", id) ;
        query.setParameter("team", approver.getTeam()) ;
        query.setMaxResults(1) ;
        try
        {
            return query.getSingleResult() ;
        }
        catch (final NoResultException nre)
        {
            return null ;
        }
    }

    public Order checkout(final User buyer)
    {
        final ShoppingCart shoppingCart = buyer.getShoppingCart()  ;
        final List<OrderItem> cartOrderItems = shoppingCart.getOrderItems() ;
        
        final List<OrderItem> orderItems = Collections.emptyList() ;
        shoppingCart.setOrderItems(orderItems) ;
        
        final Order order = createOrder(buyer, cartOrderItems) ;
        
        final BuyerTotal buyerTotal = buyer.getBuyerTotal() ;
        buyerTotal.setTotalBought(buyerTotal.getTotalBought() + order.getTotal()) ;
        
        return order ;
    }

    public void addToCart(final User user, final Item item)
    {
        final Long itemId = item.getId() ;
        final ShoppingCart shoppingCart = user.getShoppingCart() ;
        final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
        if (orderItems == null)
        {
            final ArrayList<OrderItem> newOrderItems = new ArrayList<OrderItem>() ;
            newOrderItems.add(createOrderItem(item, 1)) ;
            shoppingCart.setOrderItems(newOrderItems) ;
        }
        else
        {
            for(OrderItem orderItem: orderItems)
            {
                if (orderItem.getItem().getId().equals(itemId))
                {
                    orderItem.setQuantity(orderItem.getQuantity() + 1) ;
                    return ;
                }
            }
            
            orderItems.add(createOrderItem(item, 1)) ;
        }
    }

    public void removeFromCart(final User user, final Item item)
    {
        final ShoppingCart shoppingCart = user.getShoppingCart() ;
        final List<OrderItem> orderItems = shoppingCart.getOrderItems() ;
        if (orderItems != null)
        {
            final Iterator<OrderItem> orderItemsIter = orderItems.iterator() ;
            final Long removeItemId = item.getId() ;
            while(orderItemsIter.hasNext())
            {
                final OrderItem orderItem = orderItemsIter.next() ;
                if (orderItem.getItem().getId().equals(removeItemId))
                {
                    if (orderItem.getQuantity() > 1)
                    {
                        orderItem.setQuantity(orderItem.getQuantity() - 1) ;
                    }
                    else
                    {
                        orderItemsIter.remove() ;
                    }
                    return ;
                }
            }
        }
    }

    public void approve(final User approver, final Order order, final boolean approved, final String message)
    {
        order.setApproved(approved) ;
        order.setApprover(approver) ;
        order.setRejectionMessage(message) ;
        release(approver, order) ;
        if (!approved || (order.getExceedsLimit() != Boolean.TRUE))
        {
            updateTotals(approver, order, approved) ;
        }
    }

    public void signOff(final User vp, final Order order, final boolean approved, final String message)
    {
        order.setApproved(approved) ;
        order.setRejectionMessage(message) ;
        release(vp, order) ;
        updateTotals(order.getApprover(), order, approved) ;
        // also add approval stats for the VP
        final ApproverTotal vpTotal = vp.getApproverTotal() ;
        if (approved)
        {
            vpTotal.setApproved(vpTotal.getApproved() + order.getTotal()) ;
        }
        else
        {
            vpTotal.setRejected(vpTotal.getRejected() + order.getTotal()) ;
        }
    }

    private void updateTotals(final User approver, final Order order, final boolean approved)
    {
        final User buyer = order.getBuyer() ;
        final ApprovedTotal approvedTotal = buyer.getApprovedTotal() ;
        final ApproverTotal approverTotal = approver.getApproverTotal() ;
        if (approved)
        {
            approvedTotal.setApproved(approvedTotal.getApproved() + order.getTotal()) ;
            approverTotal.setApproved(approverTotal.getApproved() + order.getTotal()) ;
        }
        else
        {
            approvedTotal.setRejected(approvedTotal.getRejected() + order.getTotal()) ;
            approverTotal.setRejected(approverTotal.getRejected() + order.getTotal()) ;
        }
        
    }
    
    public void assign(final User user, final Order order)
    {
        order.setAssignee(user) ;
        user.setAssignedOrder(order) ;
    }
    
    public void release(final User user, final Order order)
    {
        order.setAssignee(null) ;
        user.setAssignedOrder(null) ;
    }
    
    private EntityManager getEntityManager()
    {
        return transactionalEntityManagerUtils.getEntityManager(emf) ;
    }
}
