package org.jboss.jbw2012.keynote.model ;

import java.io.Serializable ;
import java.util.Set ;

import javax.persistence.CascadeType ;
import javax.persistence.Entity ;
import javax.persistence.EnumType ;
import javax.persistence.Enumerated ;
import javax.persistence.FetchType ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.NamedQueries ;
import javax.persistence.NamedQuery ;
import javax.persistence.OneToMany ;
import javax.persistence.OneToOne ;
import javax.persistence.Table ;
import javax.validation.constraints.NotNull ;
import javax.validation.constraints.Size ;

@Entity
@Table(name="Users")
@NamedQueries({@NamedQuery(name="getUsersWithAssignedOrders", query="select user from User user where user.assignedOrder is not null"),
    @NamedQuery(name="getUsersByRole", query="select user from User user where user.role = :role")
})
public class User implements Serializable
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -2970869454313030917L ;

    private Long id ;
    private String name ;
    private Team team ;
    private Role role;
    private Set<Order> orders ;
    private Order assignedOrder ;
    private ShoppingCart shoppingCart ;
    private ApprovedTotal approvedTotal ;
    private ApproverTotal approverTotal ;
    private BuyerTotal buyerTotal ;
    private CustomerStatus customerStatus ;
    private boolean vip ;

    public User()
    {
    }

    public User(final String name, final Role role, final Team team, final ShoppingCart shoppingCart, final CustomerStatus customerStatus, final boolean vip)
    {
        this.name = name ;
        this.role = role ;
        this.team = team ;
        this.shoppingCart = shoppingCart ;
        this.customerStatus = customerStatus ;
        this.vip = vip ;
    }
    
    @Id
    @GeneratedValue
    public Long getId()
    {
        return id ;
    }

    public void setId(final Long id)
    {
        this.id = id ;
    }

    @NotNull
    @Size(min=1)
    public String getName()
    {
        return name ;
    }

    public void setName(final String name)
    {
        this.name = name ;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public Team getTeam()
    {
        return team ;
    }

    public void setTeam(final Team team)
    {
        this.team = team ;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public Role getRole()
    {
        return role ;
    }

    public void setRole(final Role role)
    {
        this.role = role ;
    }

    @OneToMany(mappedBy = "buyer", fetch=FetchType.LAZY)
    public Set<Order> getOrders()
    {
        return orders ;
    }

    public void setOrders(final Set<Order> orders)
    {
        this.orders = orders ;
    }
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    public ShoppingCart getShoppingCart()
    {
        return shoppingCart ;
    }

    public void setShoppingCart(final ShoppingCart shoppingCart)
    {
        this.shoppingCart = shoppingCart ;
    }

    @OneToOne(fetch=FetchType.LAZY)
    public Order getAssignedOrder()
    {
        return assignedOrder ;
    }

    public void setAssignedOrder(final Order assignedOrder)
    {
        this.assignedOrder = assignedOrder ;
    }
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    public ApprovedTotal getApprovedTotal()
    {
        return approvedTotal ;
    }
    
    public void setApprovedTotal(final ApprovedTotal approvedTotal)
    {
        this.approvedTotal = approvedTotal ;
    }
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    public ApproverTotal getApproverTotal()
    {
        return approverTotal ;
    }
    
    public void setApproverTotal(final ApproverTotal approverTotal)
    {
        this.approverTotal = approverTotal ;
    }
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    public BuyerTotal getBuyerTotal()
    {
        return buyerTotal ;
    }
    
    public void setBuyerTotal(final BuyerTotal buyerTotal)
    {
        this.buyerTotal = buyerTotal ;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public CustomerStatus getCustomerStatus()
    {
        return customerStatus ;
    }

    public void setCustomerStatus(final CustomerStatus customerStatus)
    {
        this.customerStatus = customerStatus ;
    }
    
    public boolean isVip()
    {
        return vip ;
    }
    
    public void setVip(final boolean vip)
    {
        this.vip = vip ;
    }
}
