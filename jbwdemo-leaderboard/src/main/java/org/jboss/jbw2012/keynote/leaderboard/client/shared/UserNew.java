package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * @author Mike Brock
 */
@Portable
public class UserNew {
  private final UserScore userScore;

  public UserNew(@MapsTo("userScore") UserScore userScore) {
    this.userScore = userScore;
  }

  public UserScore getUserScore() {
    return userScore;
  }
}
