package org.jboss.jbw2012.keynote.robots;

import java.net.ConnectException ;

import org.jboss.jbw2012.keynote.rest.resource.api.OrderAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.UserAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.OrderDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserCreationDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserIdDTO ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidUserException ;
import org.jboss.jbw2012.keynote.robots.exceptions.StoreClosedException ;
import org.jboss.jbw2012.keynote.robots.rest.RestClient ;

class ApproverRobot extends Robot
{
    private final String name ;
    private final Role role ;
    private final String password ;
    
    ApproverRobot(final Team team)
    {
        this("approver", Role.APPROVER, team, "letmein") ;
    }
    
    ApproverRobot(final String name, final Role role, final Team team, final String password)
    {
        super(team) ;
        this.name = name ;
        this.role = role ;
        this.password = password ;
    }

    public void executeRobot()
    {
        final RestClient restClient = RestClient.getRestClient() ;
        final UserAPI userAPI = restClient.getUserAPI(getServerURL()) ;
        final OrderAPI orderAPI = restClient.getOrderAPI(getServerURL()) ;
        
        final UserIdDTO user = userAPI.createUser(new UserCreationDTO(name, role.name(), getTeam().name(), password))  ;
        final long userId = Long.parseLong(user.getId()) ;
        
        while(okayToContinue())
        {
            try
            {
                final OrderDTO nextOrder = orderAPI.nextOrder(userId) ;
                if (nextOrder != null)
                {
                    final long orderId = Long.parseLong(nextOrder.getId()) ;
                    if (nextOrder.getTotal().intValue() > 3000)
                    {
                        orderAPI.rejectOrder(orderId, userId, "Exceeds upper limit") ;
                    }
                    else
                    {
                        orderAPI.approveOrder(orderId, userId) ;
                    }
                    incrementRobotInvocationCount() ;
                    delay() ;
                }
                else
                {
                    longDelay() ;
                }
            }
            catch (final InvalidUserException iue)
            {
                return ;
            }
            catch (final StoreClosedException sce)
            {
                longDelay() ;
            }
            catch (final RuntimeException re)
            {
                boolean connect = false ;
                Throwable cause = re.getCause() ;
                while(cause != null)
                {
                    if (cause instanceof ConnectException)
                    {
                        connect = true ;
                        break ;
                    }
                }
                if (!connect)
                {
                    System.out.println("Unexpected Runtime Exception: " + re) ;
                    re.printStackTrace(System.out) ;
                    longDelay() ;
                }
            }
        }
    }
}
