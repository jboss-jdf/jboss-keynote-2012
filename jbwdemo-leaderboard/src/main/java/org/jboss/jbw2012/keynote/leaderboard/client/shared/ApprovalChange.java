package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

@Portable
public class ApprovalChange {
  private final double awaitingApproval;

  public ApprovalChange(@MapsTo("awaitingApproval") double awaitingApproval) {
    this.awaitingApproval = awaitingApproval;
  }

  public double getAwaitingApproval() {
    return awaitingApproval;
  }
}
