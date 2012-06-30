package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.qual.West;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ApprovalChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.MiscTelemetry;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ScoreNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserNew;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 * @author Mike Brock
 */
@Dependent
public class WestObserverAdapter extends AbstractObserverAdapter {
  @Override
  protected void observesUserNew(@Observes @West UserNew userNew) {
    super.observesUserNew(userNew);
  }

  @Override
  public void observesScoreUpdate(@Observes @West ScoreNotify scoreNotify) {
    super.observesScoreUpdate(scoreNotify);
  }

  @Override
  public void observesRateChange(@Observes @West RateChange rateChange) {
    super.observesRateChange(rateChange);
  }

  @Override
  public void observesApprovalChange(@Observes @West ApprovalChange approvalChange) {
    super.observesApprovalChange(approvalChange);
  }

  @Override
  public void observesMiscTelemetry(@Observes @West MiscTelemetry miscTelemetry) {
    super.observesMiscTelemetry(miscTelemetry);
  }
}
