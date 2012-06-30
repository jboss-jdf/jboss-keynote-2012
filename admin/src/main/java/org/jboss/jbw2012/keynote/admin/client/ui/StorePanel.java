package org.jboss.jbw2012.keynote.admin.client.ui;

import javax.annotation.PostConstruct ;
import javax.enterprise.event.Event ;
import javax.enterprise.event.Observes ;
import javax.inject.Inject ;

import org.jboss.jbw2012.keynote.admin.common.AdminUIStoreStatus ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIChooseWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIChosenWinnersEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIStoreStatusChangeEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIStoreStatusEvent ;

import com.google.gwt.core.client.GWT ;
import com.google.gwt.event.dom.client.ClickEvent ;
import com.google.gwt.event.dom.client.ClickHandler ;
import com.google.gwt.uibinder.client.UiBinder ;
import com.google.gwt.uibinder.client.UiField ;
import com.google.gwt.user.client.ui.Button ;
import com.google.gwt.user.client.ui.Composite ;
import com.google.gwt.user.client.ui.RadioButton ;
import com.google.gwt.user.client.ui.Widget ;

public class StorePanel extends Composite
{
    private static AdminPanelUiBinder uiBinder = GWT.create(AdminPanelUiBinder.class) ;

    interface AdminPanelUiBinder extends UiBinder<Widget, StorePanel>
    {
    }

    @UiField
    RadioButton open ;
    @UiField
    RadioButton vipOnly ;
    @UiField
    RadioButton closed ;
    @UiField
    Button chooseWinners ;
    
    @Inject
    private Event<AdminUIStoreStatusChangeEvent> adminUIStoreStatusChangeEvent ;
    @Inject
    private Event<AdminUIChooseWinnersEvent> adminUIChooseWinnersEvent ;
    
    @PostConstruct
    public void initialise()
    {
        initWidget(uiBinder.createAndBindUi(this)) ;
        
        open.addClickHandler(new StoreHandler(AdminUIStoreStatus.OPEN)) ;
        vipOnly.addClickHandler(new StoreHandler(AdminUIStoreStatus.VIP_ONLY)) ;
        closed.addClickHandler(new StoreHandler(AdminUIStoreStatus.CLOSED)) ;
        chooseWinners.addClickHandler(new ChooseWinnersHandler()) ;
    }
    
    public void storeStatus(final @Observes AdminUIStoreStatusEvent storeStatusEvent)
    {
        final AdminUIStoreStatus storeStatus = storeStatusEvent.getStoreStatus() ;
        switch(storeStatus)
        {
            case OPEN:
                open.setValue(Boolean.TRUE) ;
                break ;
            case VIP_ONLY:
                vipOnly.setValue(Boolean.TRUE) ;
                break ;
            case CLOSED:
                closed.setValue(Boolean.TRUE) ;
                break ;
        }
        setEnabled(true) ;
    }
    
    public void chosenWinners(final @Observes AdminUIChosenWinnersEvent chosenWinnersEvent)
    {
        setEnabled(true) ;
    }
    
    private void setEnabled(final boolean enabled)
    {
        open.setEnabled(enabled) ;
        vipOnly.setEnabled(enabled) ;
        closed.setEnabled(enabled) ;
        chooseWinners.setEnabled(enabled && (closed.getValue() == Boolean.TRUE)) ;
    }

    public class StoreHandler implements ClickHandler
    {
        private final AdminUIStoreStatus storeStatus ;
        
        public StoreHandler(final AdminUIStoreStatus storeStatus)
        {
            this.storeStatus = storeStatus ;
        }
        
        @Override
        public void onClick(final ClickEvent event)
        {
            setEnabled(false) ;
            adminUIStoreStatusChangeEvent.fire(new AdminUIStoreStatusChangeEvent(storeStatus)) ;
        }
    }

    public class ChooseWinnersHandler implements ClickHandler
    {
        @Override
        public void onClick(final ClickEvent event)
        {
            setEnabled(false) ;
            adminUIChooseWinnersEvent.fire(new AdminUIChooseWinnersEvent()) ;
        }
    }
}
