package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * @author Mike Brock
 */
public class RateTick {
  private final double time;
  private final double value;

  public RateTick(@MapsTo("timeDelta") double time, @MapsTo("value") double value) {
    this.time = time;
    this.value = value;
  }

  public double getTime() {
    return time;
  }

  public double getValue() {
    return value;
  }

  public String toString() {
    return "RateTick(time=" + new Double(time).longValue() + ", value=" + new Double(value).longValue() + ")";
  }
}
