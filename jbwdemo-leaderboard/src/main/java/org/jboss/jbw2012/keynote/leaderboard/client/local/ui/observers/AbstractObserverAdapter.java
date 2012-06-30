package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers;

import org.jboss.jbw2012.keynote.leaderboard.client.local.JBWLeaderboard;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ApprovalChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.MiscTelemetry;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ScoreNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserNew;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserScore;

import javax.inject.Inject;

/**
 * @author Mike Brock
 */
public abstract class AbstractObserverAdapter implements ObserverAdapter {
  @Inject protected JBWLeaderboard leaderboard;

  protected void observesUserNew(final UserNew userNew) {
    leaderboard.getPanel().addUserScore(userNew.getUserScore());
  }

  protected void observesScoreUpdate(final ScoreNotify scoreNotify) {
    leaderboard.getPanel().setScore(scoreNotify.getUserId(), scoreNotify.getScore());
  }

  protected void observesRateChange(final RateChange rateChange) {
    leaderboard.setTransactionRate(rateChange.getRate());
  }

  protected void observesApprovalChange(final ApprovalChange approvalChange) {
    leaderboard.setAwaitingApprovals(approvalChange.getAwaitingApproval());
  }

  protected void observesMiscTelemetry(final MiscTelemetry miscTelemetry) {
    leaderboard.setSalesPeopleCount(miscTelemetry.getTotalSalesPeople());
    leaderboard.setTotalSalesAmount(miscTelemetry.getTotalSalesAmount());
  }
}
