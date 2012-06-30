package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot;

/**
 * @author Mike Brock
 */
public interface Converter<R, T> {
  public R handle(double iterationIndex, T toVisit);
}
