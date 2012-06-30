package org.jboss.jbw2012.keynote.leaderboard.client.local.ui;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserScore;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Mike Brock
 */
public class LeaderboardPanel extends Composite {
  private static final int MAX_TABLE_SIZE = 10;

  @Inject UiBinder<Widget, LeaderboardPanel> panelUiBinder;
  @Inject HashMap<Long, UserScore> userLookup;

  @UiField(provided = true) CellTable<UserScore> table;
  @UiField Label teamName;
  @UiField Label totalSalesAmount;

  @UiField Label totalSalesPeople;
  private ListDataProvider<UserScore> listDataProvider;

  private final static NumberFormat bigNumberFormat = NumberFormat.getDecimalFormat();
  private final static NumberFormat currencyFormat = NumberFormat.getFormat("$#,##0.00");

  @PostConstruct
  public void bindTable() {
    table = new CellTable<UserScore>(MAX_TABLE_SIZE, CellTableResources.CELL_TABLE_RESOURCES);

    initWidget(panelUiBinder.createAndBindUi(this));
    listDataProvider = setupTable();

    totalSalesPeople.setText("0");
    totalSalesPeople.getElement().setId("totalSalesPeople");
    totalSalesAmount.getElement().setId("totalSalesAmount");
  }

  public void addUserScore(final UserScore userScore) {
    final Long id = userScore.getUser().getId();
    if (!userLookup.containsKey(id)) {
      listDataProvider.getList().add(userScore);
      userLookup.put(userScore.getUser().getId(), userScore);

      ColumnSortEvent.fire(table, table.getColumnSortList());
    }
  }

  public void resetLeaderboard() {
    teamName.setText("Loading ...");

    setTotalSalesAmount(0);
    setSalesPeople(0);

    listDataProvider.getList().clear();
    userLookup.clear();
    padScores();
  }

  public void padScores() {
    for (int i = listDataProvider.getList().size(); i < MAX_TABLE_SIZE; i++) {
      listDataProvider.getList().add(UserScore.createPadding());
    }
  }

  public void setScore(final Long userId, final double score) {
    final UserScore userScore = userLookup.get(userId);
    if (userScore != null && userScore.getScore() != score) {

      userScore.setScore(score);
      ColumnSortEvent.fire(table, table.getColumnSortList());
      table.redraw();
    //  bounceResize("#userScore_" + userId, "36px", "40px", 100, 50);
    }
  }

  public void setTeamName(String name) {
    this.teamName.setText(name);
  }

  public void setTotalSalesAmount(double amount) {
    final String currentText = this.totalSalesAmount.getText();
    final String newText = currencyFormat.format(amount);
    this.totalSalesAmount.setText(newText);

    if (!currentText.equals(newText)) {
      bounceResize("#totalSalesAmount", "36px", "62px", 250, 400);
    }
  }

  public void setSalesPeople(int amount) {
    final String currentText = this.totalSalesPeople.getText();
    final String newText = bigNumberFormat.format(amount);

    this.totalSalesPeople.setText(newText);

    if (!currentText.equals(newText)) {
      bounceResize("#totalSalesPeople", "36px", "62px", 250, 400);
    }
  }

  private ListDataProvider<UserScore> setupTable() {
    final TextColumn<UserScore> userName = new TextColumn<UserScore>() {
      @Override
      public String getValue(UserScore object) {
        return object.getUser().getName();
      }
    };

    final SafeHtmlCell scoreCell = new SafeHtmlCell();

    final Column<UserScore, SafeHtml> score = new Column<UserScore, SafeHtml>(scoreCell) {
      @Override
      public SafeHtml getValue(UserScore object) {
        final SafeHtmlBuilder sb = new SafeHtmlBuilder();
        if (object.getScore() < 0) {
        }
        else {
          sb.appendHtmlConstant("<div id='userScore_" + object.getUser().getId() + "' style='text-overflow: clip;'>");
          sb.appendHtmlConstant(currencyFormat.format(object.getScore()));
          sb.appendHtmlConstant("</div>");
        }
        return sb.toSafeHtml();
      }
    };

    table.setTableLayoutFixed(true);
    table.setEmptyTableWidget(new Label("No data."));

    score.setSortable(true);
    score.setHorizontalAlignment(TextColumn.ALIGN_RIGHT);

    table.addColumn(userName, "Name");
    table.addColumn(score, "Score");

    final ListDataProvider<UserScore> dataProvider = new ListDataProvider<UserScore>();
    dataProvider.addDataDisplay(table);

    final ColumnSortEvent.ListHandler<UserScore> columnSortHandler
            = new ColumnSortEvent.ListHandler<UserScore>(dataProvider.getList());

    columnSortHandler.setComparator(score, new Comparator<UserScore>() {
      @Override
      public int compare(UserScore o1, UserScore o2) {
        return (int) ((o2.getScore() * 100) - (o1.getScore() * 100));
      }
    });

    table.addColumnSortHandler(columnSortHandler);
    table.getColumnSortList().push(score);

    table.redraw();

    return dataProvider;
  }

  /**
   * Resources that match the GWT standard style theme.
   */
  public interface CellTableResources extends CellTable.Resources {
    static final CellTableResources CELL_TABLE_RESOURCES =
            GWT.create(CellTableResources.class);

    /**
     * The styles used in this widget.
     */
    @Override
    @Source("CellTable.css")
    CellTable.Style cellTableStyle();
  }

  public void addTitleClickHandler(ClickHandler handler) {
    teamName.addClickHandler(handler);
  }

  private native void bounceResize(final String id,
                                   final String size,
                                   final String maxSize,
                                   final double speedIn,
                                   final double speedOut) /*-{

    $wnd.$(id).animate({fontSize:maxSize}, speedIn, function () {
      $wnd.$(id).animate({fontSize:size}, speedOut, function () {
      })
    });
  }-*/;
}
