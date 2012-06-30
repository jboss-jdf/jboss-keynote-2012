package org.jboss.jbw2012.keynote.admin.client.ui;

import javax.annotation.PostConstruct ;
import javax.enterprise.event.Event ;
import javax.enterprise.event.Observes ;
import javax.inject.Inject ;

import org.jboss.jbw2012.keynote.admin.common.events.AdminUIClearedEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIResetEvent ;

import com.google.gwt.core.client.GWT ;
import com.google.gwt.event.dom.client.ClickEvent ;
import com.google.gwt.event.dom.client.ClickHandler ;
import com.google.gwt.uibinder.client.UiBinder ;
import com.google.gwt.uibinder.client.UiField ;
import com.google.gwt.user.client.ui.Button ;
import com.google.gwt.user.client.ui.Composite ;
import com.google.gwt.user.client.ui.Widget ;

public class ResetPanel extends Composite
{
    private static AdminPanelUiBinder uiBinder = GWT.create(AdminPanelUiBinder.class) ;

    interface AdminPanelUiBinder extends UiBinder<Widget, ResetPanel>
    {
    }

    @UiField
    Button reset ;
    
    @Inject
    private Event<AdminUIResetEvent> adminUIResetEvent ;
    
    @PostConstruct
    public void initialise()
    {
        initWidget(uiBinder.createAndBindUi(this)) ;
        
        reset.addClickHandler(new ResetHandler()) ;
    }
    
    public void cleared(final @Observes AdminUIClearedEvent clearedEvent)
    {
        setEnabled(true) ;
    }
    
    private void setEnabled(final boolean enabled)
    {
        reset.setEnabled(enabled) ;
    }

    public class ResetHandler implements ClickHandler
    {
        @Override
        public void onClick(final ClickEvent event)
        {
            setEnabled(false) ;
            adminUIResetEvent.fire(new AdminUIResetEvent()) ;
        }
    }
}
