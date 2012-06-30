package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.ApprovalChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.MiscTelemetry;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ScoreNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserNew;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.qual.East;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 * @author Mike Brock
 */
@Dependent
public class EastObserverAdapter extends AbstractObserverAdapter {
  @Override
  protected void observesUserNew(@Observes @East UserNew userNew) {
    super.observesUserNew(userNew);
  }

  @Override
  public void observesScoreUpdate(@Observes @East ScoreNotify scoreNotify) {
    super.observesScoreUpdate(scoreNotify);
  }

  @Override
  public void observesRateChange(@Observes @East RateChange rateChange) {
    super.observesRateChange(rateChange);
  }

  @Override
  public void observesApprovalChange(@Observes @East ApprovalChange approvalChange) {
    super.observesApprovalChange(approvalChange);
  }

  @Override
  public void observesMiscTelemetry(@Observes @East MiscTelemetry miscTelemetry) {
    super.observesMiscTelemetry(miscTelemetry);
  }
}
