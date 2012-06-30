package org.jboss.jbw2012.keynote.rest.admin;

import java.util.ArrayList ;
import java.util.HashSet ;
import java.util.List ;
import java.util.Random ;
import java.util.Set ;

import javax.annotation.Resource ;
import javax.enterprise.event.Event ;
import javax.enterprise.event.Observes ;
import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.transaction.RollbackException ;
import javax.transaction.Status ;
import javax.transaction.Synchronization ;
import javax.transaction.SystemException ;
import javax.transaction.Transaction ;
import javax.transaction.TransactionManager ;

import org.jboss.jbw2012.keynote.admin.model.AdminStoreStatus ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminChooseWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminChosenWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminConnectedEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminStoreStatusChangeEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminStoreStatusEvent ;
import org.jboss.jbw2012.keynote.errai.events.ContestEndEvent ;
import org.jboss.jbw2012.keynote.model.Order ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jboss.jbw2012.keynote.rest.resource.utils.Store ;
import org.jboss.jbw2012.keynote.rest.resource.utils.StoreStatus ;
import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;

@Singleton
public class AdminStoreService
{
    private static final int NUM_WINNERS = 5 ;
    
    @Resource(mappedName="java:/TransactionManager")
    private TransactionManager tm ;

    @Inject
    private ModelUtils modelUtils ;

    @Inject
    private Event<AdminStoreStatusEvent> adminStoreStatusEvent ;
    @Inject
    private Event<AdminChosenWinnersEvent> adminChosenWinnersEvent ;
    @Inject
    private Event<ContestEndEvent> contestEndEvent ;
    
    public void connected(final @Observes AdminConnectedEvent event)
    {
        broadcastStatus() ;
    }
    
    public void updateStatus(final @Observes AdminStoreStatusChangeEvent event)
    {
        Store.getStore().setStatus(StoreStatus.valueOf(event.getStoreStatus().name())) ;
        broadcastStatus() ;
    }
    
    @Transactional
    public void chooseWinners(final @Observes AdminChooseWinnersEvent event)
    {
        final int orderCount = (int)modelUtils.getOrderCount() ;
        System.out.println("order count " + orderCount) ;
        final Random random = new Random(System.nanoTime()) ;
        final Set<Integer> chosenOrderIds = new HashSet<Integer>() ;
        final List<Long> winners = new ArrayList<Long>() ;
        do
        {
            final int winningOrder = random.nextInt(orderCount) ;
            if (chosenOrderIds.add(winningOrder))
            {
                final List<Order> orders = modelUtils.getOrders(winningOrder, 1) ;
                final Long id = orders.get(0).getBuyer().getId() ;
                if (!winners.contains(id))
                {
                    winners.add(id) ;
                }
            }
        }
        while((winners.size() <= NUM_WINNERS) && (chosenOrderIds.size() < orderCount)) ; // NUM_WINNERS + 1 for spare
        
        try
        {
            final Transaction tx = tm.getTransaction() ;
            tx.registerSynchronization(new ChosenWinnersSynchronization(winners)) ;
        }
        catch (final RollbackException re)
        {
            throw new IllegalStateException("Transaction management exception", re) ;
        }
        catch (final SystemException se)
        {
            throw new IllegalStateException("Transaction management exception", se) ;
        }
    }
    
    private void broadcastStatus()
    {
        adminStoreStatusEvent.fire(new AdminStoreStatusEvent(AdminStoreStatus.valueOf(Store.getStore().getStatus().name()))) ;
    }

    private class ChosenWinnersSynchronization implements Synchronization
    {
        private final List<Long> winners ;
        
        ChosenWinnersSynchronization(final List<Long> winners)
        {
            this.winners = winners ;
        }
        
        @Override
        public void beforeCompletion()
        {
        }

        @Override
        public void afterCompletion(final int status)
        {
            if (Status.STATUS_COMMITTED == status)
            {
                adminChosenWinnersEvent.fire(new AdminChosenWinnersEvent()) ;
                contestEndEvent.fire(new ContestEndEvent(winners)) ;
            }
        }
    }
}
