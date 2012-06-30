package org.jboss.jbw2012.keynote.admin.server;

import javax.enterprise.context.ApplicationScoped ;
import javax.enterprise.event.Event ;
import javax.enterprise.event.Observes ;
import javax.inject.Inject ;

import org.jboss.jbw2012.keynote.admin.common.AdminUIStoreStatus ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIChooseWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIChosenWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIConnectedEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIStoreStatusChangeEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIStoreStatusEvent ;
import org.jboss.jbw2012.keynote.admin.model.AdminStoreStatus ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminChooseWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminChosenWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminConnectedEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminStoreStatusChangeEvent ;
import org.jboss.jbw2012.keynote.admin.model.events.AdminStoreStatusEvent ;

@ApplicationScoped
public class AdminUIStoreService
{
    @Inject
    private Event<AdminConnectedEvent> adminConnectedEvent ;
    @Inject
    private Event<AdminStoreStatusChangeEvent> adminStoreStatusChangeEvent ;
    @Inject
    private Event<AdminUIStoreStatusEvent> adminUIStoreStatusEvent ;
    @Inject
    private Event<AdminChooseWinnersEvent> adminChooseWinnersEvent ;
    @Inject
    private Event<AdminUIChosenWinnersEvent> adminUIChosenWinnersEvent ;
    
    public void connected(final @Observes AdminUIConnectedEvent event)
    {
        adminConnectedEvent.fire(new AdminConnectedEvent()) ;
    }
    
    public void updateStatus(final @Observes AdminUIStoreStatusChangeEvent event)
    {
        adminStoreStatusChangeEvent.fire(new AdminStoreStatusChangeEvent(AdminStoreStatus.valueOf(event.getStoreStatus().name()))) ;
    }

    public void updatedStatus(final @Observes AdminStoreStatusEvent event)
    {
        adminUIStoreStatusEvent.fire(new AdminUIStoreStatusEvent(AdminUIStoreStatus.valueOf(event.getStoreStatus().name()))) ;
    }

    public void chooseWinners(final @Observes AdminUIChooseWinnersEvent event)
    {
        adminChooseWinnersEvent.fire(new AdminChooseWinnersEvent()) ;
    }

    public void choosenWinners(final @Observes AdminChosenWinnersEvent event)
    {
        adminUIChosenWinnersEvent.fire(new AdminUIChosenWinnersEvent()) ;
    }
}
