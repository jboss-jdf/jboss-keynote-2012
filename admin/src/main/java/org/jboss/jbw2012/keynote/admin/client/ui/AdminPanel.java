package org.jboss.jbw2012.keynote.admin.client.ui;

import javax.annotation.PostConstruct ;
import javax.inject.Inject ;

import com.google.gwt.core.client.GWT ;
import com.google.gwt.uibinder.client.UiBinder ;
import com.google.gwt.uibinder.client.UiField ;
import com.google.gwt.user.client.ui.Composite ;
import com.google.gwt.user.client.ui.TabLayoutPanel ;
import com.google.gwt.user.client.ui.Widget ;

public class AdminPanel extends Composite
{
    private static AdminPanelUiBinder uiBinder = GWT.create(AdminPanelUiBinder.class) ;

    @UiField
    TabLayoutPanel tabLayoutPanel ;
    
    @Inject
    private StorePanel storePanel ;
    @Inject
    private UserPanel userPanel ;
    @Inject
    private ResetPanel resetPanel ;
    
    interface AdminPanelUiBinder extends UiBinder<Widget, AdminPanel>
    {
    }
    
    @PostConstruct
    public void initialise()
    {
        initWidget(uiBinder.createAndBindUi(this)) ;
        
        tabLayoutPanel.add(storePanel, "Store") ;
        tabLayoutPanel.add(userPanel, "Users") ;
        tabLayoutPanel.add(resetPanel, "Reset") ;
        
        tabLayoutPanel.selectTab(0) ;
    }
}
