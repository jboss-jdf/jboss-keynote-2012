package org.jboss.jbw2012.keynote.admin.client.ui;

import java.util.Collections ;
import java.util.List ;

import javax.annotation.PostConstruct ;
import javax.enterprise.event.Event ;
import javax.enterprise.event.Observes ;
import javax.inject.Inject ;

import org.jboss.jbw2012.keynote.admin.common.AdminUIUserInfo ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIBuyerTotalEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIUserQueryEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIUserUpdateEvent ;
import org.jboss.jbw2012.keynote.admin.common.events.AdminUIUserVIPUpdateEvent ;

import com.google.gwt.cell.client.CheckboxCell ;
import com.google.gwt.cell.client.FieldUpdater ;
import com.google.gwt.core.client.GWT ;
import com.google.gwt.uibinder.client.UiBinder ;
import com.google.gwt.uibinder.client.UiField ;
import com.google.gwt.user.cellview.client.CellTable ;
import com.google.gwt.user.cellview.client.Column ;
import com.google.gwt.user.cellview.client.SimplePager ;
import com.google.gwt.user.cellview.client.TextColumn ;
import com.google.gwt.user.client.ui.Composite ;
import com.google.gwt.user.client.ui.Widget ;
import com.google.gwt.view.client.AsyncDataProvider ;
import com.google.gwt.view.client.HasData ;
import com.google.gwt.view.client.Range ;

public class UserPanel extends Composite
{
    private static AdminPanelUiBinder uiBinder = GWT.create(AdminPanelUiBinder.class) ;

    interface AdminPanelUiBinder extends UiBinder<Widget, UserPanel>
    {
    }

    @UiField(provided=true)
    CellTable<AdminUIUserInfo> cellTable = new CellTable<AdminUIUserInfo>() ;
    
    @UiField(provided=true)
    SimplePager pager ;
    
    @Inject
    private Event<AdminUIUserQueryEvent> userQueryEvent ;
    @Inject
    private Event<AdminUIUserVIPUpdateEvent> userVIPUpdateEvent ;
    
    private AsyncDataProvider<AdminUIUserInfo> provider ;
    
    @PostConstruct
    public void initialise()
    {
        final TextColumn<AdminUIUserInfo> nameColumn = new TextColumn<AdminUIUserInfo>() {
            @Override
            public String getValue(final AdminUIUserInfo userInfo) {
                return userInfo.getName() ;
            }
        } ;
        cellTable.addColumn(nameColumn, "Name") ;
        
        final TextColumn<AdminUIUserInfo> idColumn = new TextColumn<AdminUIUserInfo>() {
            @Override
            public String getValue(final AdminUIUserInfo userInfo) {
                return userInfo.getId() ;
            }
        } ;
        cellTable.addColumn(idColumn, "Id") ;
        
        final CheckboxCell checkboxCell = new CheckboxCell() ;
        final Column<AdminUIUserInfo, Boolean> vipColumn = new Column<AdminUIUserInfo, Boolean>(checkboxCell) {
            @Override
            public Boolean getValue(final AdminUIUserInfo userInfo) {
                return userInfo.isVip() ;
            }
        } ;
        vipColumn.setFieldUpdater(new FieldUpdater<AdminUIUserInfo, Boolean>() {
            @Override
            public void update(final int index, final AdminUIUserInfo userInfo, final Boolean value) {
                userVIPUpdateEvent.fire(new AdminUIUserVIPUpdateEvent(userInfo.getId(), value)) ;
            }
        }) ;
        cellTable.addColumn(vipColumn, "VIP") ;
        
        provider = new AsyncDataProvider<AdminUIUserInfo>() {
            @Override
            protected void onRangeChanged(final HasData<AdminUIUserInfo> display) {
                final Range range = display.getVisibleRange()  ;
                userQueryEvent.fire(new AdminUIUserQueryEvent(range.getStart(), range.getLength())) ;
            }
        } ;
        provider.addDataDisplay(cellTable) ;
        
        pager = new SimplePager() ;
        pager.setDisplay(cellTable) ;
        
        initWidget(uiBinder.createAndBindUi(this)) ;
    }

    public void updateBuyerTotal(final @Observes AdminUIBuyerTotalEvent buyerTotalEvent)
    {
        final int total = buyerTotalEvent.getTotal() ;
        provider.updateRowCount(total, true) ;
        final int start = cellTable.getVisibleRange().getStart() ;
        final int end = start + cellTable.getPageSize() ;
        if ((total > start) && (total < end))
        {
            userQueryEvent.fire(new AdminUIUserQueryEvent(start, total-start)) ;
        }
        else if (total == start)
        {
            final List<AdminUIUserInfo> users = Collections.emptyList() ;
            provider.updateRowData(start, users) ;
        }
    }

    public void updateUsers(final @Observes AdminUIUserUpdateEvent userUpdateEvent)
    {
        provider.updateRowData(userUpdateEvent.getStart(), userUpdateEvent.getUserInfo()) ;
    }
}
