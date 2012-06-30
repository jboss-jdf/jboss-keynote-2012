package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * @author Mike Brock
 */
@Portable
public class UserScore {
  private final User user;
  private volatile double score;
  private volatile int ordered;
  private volatile int approved;
  private volatile int rejected;

  private UserScore(User user, double score) {
    this.user = user;
    this.score = score;
  }

  public static UserScore create(User user) {
    return new UserScore(user, 0);
  }

  public static UserScore create(@MapsTo("user") User user, @MapsTo("score") int score) {
    return new UserScore(user, score);
  }

  public static UserScore createPadding() {
    final User user = new User(-1L, "--", Role.BUYER, Team.BOTH);
    final UserScore userScore =  new UserScore(user, -1);
    return userScore;
  }

  public User getUser() {
    return user;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public int getOrdered() {
    return ordered;
  }

  public void setOrdered(int ordered) {
    this.ordered = ordered;
  }

  public int getApproved() {
    return approved;
  }

  public void setApproved(int approved) {
    this.approved = approved;
  }

  public int getRejected() {
    return rejected;
  }

  public void setRejected(int rejected) {
    this.rejected = rejected;
  }

  public void incrementScore(double amount) {
    synchronized (this) {
      this.score += amount;
    }
  }
}
