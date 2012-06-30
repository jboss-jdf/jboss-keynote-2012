package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * @author Mike Brock
 */
@Portable
public class Connect {
  private final Team team;

  public Connect(@MapsTo("team") Team team) {
    this.team = team;
  }

  public Team getTeam() {
    return team;
  }
}
