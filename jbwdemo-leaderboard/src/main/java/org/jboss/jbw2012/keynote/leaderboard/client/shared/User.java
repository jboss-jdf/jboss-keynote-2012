package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

import java.io.Serializable;

@Portable
public class User implements Serializable {
  private final Long id;
  private final String name;
  private final Team team;
  private final Role role;

  public User(@MapsTo("id")   final Long id,
              @MapsTo("name") final String name,
              @MapsTo("role") final Role role,
              @MapsTo("team") final Team team) {

    this.id = id;
    this.name = name;
    this.role = role;
    this.team = team;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Team getTeam() {
    return team;
  }

  public Role getRole() {
    return role;
  }
}
