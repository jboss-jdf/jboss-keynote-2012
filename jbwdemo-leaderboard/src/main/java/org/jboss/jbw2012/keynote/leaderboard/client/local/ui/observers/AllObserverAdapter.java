package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.ApprovalChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.MiscTelemetry;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ScoreNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserNew;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.qual.Both;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 * @author Mike Brock
 */
@Dependent
public class AllObserverAdapter extends AbstractObserverAdapter {
  @Override
  protected void observesUserNew(@Observes @Both UserNew userNew) {
    super.observesUserNew(userNew);
  }

  @Override
  public void observesScoreUpdate(@Observes @Both ScoreNotify scoreNotify) {
    super.observesScoreUpdate(scoreNotify);
  }

  @Override
  public void observesRateChange(@Observes @Both RateChange rateChange) {
    super.observesRateChange(rateChange);
  }

  @Override
  public void observesApprovalChange(@Observes @Both ApprovalChange approvalChange) {
    super.observesApprovalChange(approvalChange);
  }

  @Override
  public void observesMiscTelemetry(@Observes @Both MiscTelemetry miscTelemetry) {
    super.observesMiscTelemetry(miscTelemetry);
  }
}
