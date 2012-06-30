package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational ;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

import java.util.List;

/**
 * @author Mike Brock
 */
@Portable @Conversational
public class ConnectNotify {
  private final List<UserScore> users;

  public ConnectNotify(@MapsTo("user") List<UserScore> users) {
    this.users = users;
  }

  public List<UserScore> getUsers() {
    return users;
  }
}
