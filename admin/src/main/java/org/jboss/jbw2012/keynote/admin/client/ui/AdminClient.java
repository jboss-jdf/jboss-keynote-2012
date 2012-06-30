package org.jboss.jbw2012.keynote.admin.client.ui;

import javax.annotation.PostConstruct ;
import javax.enterprise.event.Event ;
import javax.inject.Inject ;

import org.jboss.errai.bus.client.ErraiBus ;
import org.jboss.errai.bus.client.framework.ClientMessageBusImpl ;
import org.jboss.errai.common.client.util.LogUtil ;
import org.jboss.errai.ioc.client.api.EntryPoint ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIConnectedEvent ;

import com.google.gwt.event.logical.shared.ResizeEvent ;
import com.google.gwt.event.logical.shared.ResizeHandler ;
import com.google.gwt.user.client.Window ;
import com.google.gwt.user.client.ui.RootPanel ;
import com.google.gwt.user.client.ui.VerticalPanel ;

@EntryPoint
public class AdminClient
{
    @Inject
    private Event<AdminUIConnectedEvent> adminConnectedEvent ;
    
    @Inject
    private RootPanel rootPanel ;
    @Inject
    private AdminPanel adminPanel ;
    
    
    @PostConstruct
    public void initialise()
    {
        final VerticalPanel vp = new VerticalPanel() ;
        vp.add(adminPanel) ;
        vp.setWidth("100%") ;
        vp.setHeight(Window.getClientHeight() + "px") ;
        Window.addResizeHandler(new ResizeHandler() {
          public void onResize(final ResizeEvent event) {
            int height = event.getHeight() ;
            vp.setHeight(height + "px") ;
          }
        });
        rootPanel.add(vp) ;
        
        ((ClientMessageBusImpl)ErraiBus.get()).addPostInitTask(new Runnable() {
            public void run() {
                LogUtil.log("JBossWorld Admin connecting") ;
                adminConnectedEvent.fire(new AdminUIConnectedEvent()) ;
            }
        }) ;
    }
}
