package org.jboss.jbw2012.keynote.leaderboard.client.local.ui;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Mike Brock
 */
@Dependent
public class ContestEndPanel extends Composite {
  @Inject UiBinder<Widget, ContestEndPanel> panelUiBinder;

  @UiField Label titleBar;
  @UiField Label contestWinner;
  @UiField HTMLPanel lotteryWinners;

  @PostConstruct
  private void bindUI() {
    initWidget(panelUiBinder.createAndBindUi(this));
  }

  public void setWinningTeam(Team team) {
    if (team == Team.BOTH) {
      titleBar.setText("It's a Draw!");
    } else {
      titleBar.setText("Team " + team.getDisplayName() + " Wins!");
    }
  }

  public void setContestWinner(User user) {
    contestWinner.setText(user.getName());
  }

  public void setLotteryWinners(List<User> users) {
    final SafeHtmlBuilder sb = new SafeHtmlBuilder();
    sb.appendHtmlConstant("<ul>");

    for (User user : users) {
      sb.appendHtmlConstant("<li>" + user.getName() + "</li>");
    }

    sb.appendHtmlConstant("</ul>");

    lotteryWinners.add(new HTML(sb.toSafeHtml()));
  }
}
