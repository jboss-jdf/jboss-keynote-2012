package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * @author Mike Brock
 */
@Portable
public class RateChange {
  private final double rate;

  public RateChange(@MapsTo("rate") double rate) {
    this.rate = rate;
  }

  public double getRate() {
    return rate;
  }
}
