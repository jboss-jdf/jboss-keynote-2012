package org.jboss.jbw2012.keynote.robots;

import java.net.ConnectException ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.Random ;

import javax.ws.rs.core.Response.Status ;

import org.jboss.jbw2012.keynote.rest.resource.api.CategoryAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.ShoppingCartAPI ;
import org.jboss.jbw2012.keynote.rest.resource.api.UserAPI ;
import org.jboss.jbw2012.keynote.rest.resource.dto.CategoryDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.ItemIdDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserCreationDTO ;
import org.jboss.jbw2012.keynote.rest.resource.dto.UserIdDTO ;
import org.jboss.jbw2012.keynote.robots.exceptions.InvalidUserException ;
import org.jboss.jbw2012.keynote.robots.exceptions.StoreClosedException ;
import org.jboss.jbw2012.keynote.robots.rest.RestClient ;
import org.jboss.resteasy.client.ClientResponseFailure ;

class BuyerRobot extends Robot
{
    private static final Random RANDOM = new Random() ;
    
    BuyerRobot(final Team team)
    {
        super(team) ;
    }

    public void executeRobot()
    {
        final RestClient restClient = RestClient.getRestClient() ;
        final UserAPI userAPI = restClient.getUserAPI(getServerURL()) ;
        final CategoryAPI categoryAPI = restClient.getCategoryAPI(getServerURL()) ;
        final ShoppingCartAPI shoppingCartAPI = restClient.getShoppingCartAPI(getServerURL()) ;
        
        final List<ItemDTO> items = retrieveItems(categoryAPI) ;
        
        final UserIdDTO user = userAPI.createUser(new UserCreationDTO("buyer", Role.BUYER.name(), getTeam().name(), null))  ;
        final long userId = Long.parseLong(user.getId()) ;
        
        while(okayToContinue())
        {
            try
            {
                populateShoppingCart(shoppingCartAPI, userId, items) ;
                shoppingCartAPI.checkout(userId) ;
                incrementRobotInvocationCount() ;
                delay() ;
            }
            catch (final ClientResponseFailure crf)
            {
                if (Status.NOT_FOUND.getStatusCode() == crf.getResponse().getStatus())
                {
                    return ;
                }
                System.out.println("Unexpected Client Response Failure: " + crf) ;
                crf.printStackTrace(System.out) ;
                longDelay() ;
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

    private static void populateShoppingCart(final ShoppingCartAPI shoppingCartAPI,
        final long userId, final List<ItemDTO> items)
    {
        final int itemCount = RANDOM.nextInt(4) ;
        final int numItems = items.size() ;
        for(int count = 0 ; count < itemCount ; count++)
        {
            final ItemDTO itemDTO = items.get(RANDOM.nextInt(numItems)) ;
            shoppingCartAPI.addToShoppingCart(userId, new ItemIdDTO(itemDTO.getId())) ;
        }
    }

    private static List<ItemDTO> retrieveItems(final CategoryAPI categoryAPI)
    {
        final List<CategoryDTO> categories = categoryAPI.getCategories()  ;
        final List<ItemDTO> items = new ArrayList<ItemDTO>() ;
        for(CategoryDTO category: categories)
        {
            items.addAll(category.getItems()) ;
        }
        return items ;
    }
}
