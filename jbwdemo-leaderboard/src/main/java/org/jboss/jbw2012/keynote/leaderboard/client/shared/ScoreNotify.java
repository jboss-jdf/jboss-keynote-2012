package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * @author Mike Brock
 */
@Portable
public class ScoreNotify {
  private final Long userId;
  private final double score;

  public ScoreNotify(@MapsTo("userId") Long userId, @MapsTo("score") double score) {
    this.userId = userId;
    this.score = score;
  }

  public Long getUserId() {
    return userId;
  }

  public double getScore() {
    return score;
  }
}
