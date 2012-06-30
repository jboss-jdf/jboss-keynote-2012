package org.jboss.jbw2012.keynote.leaderboard.server;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.qual.Both;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.qual.East;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.qual.West;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;

import java.lang.annotation.Annotation;

/**
 * @author Mike Brock
 */
public class AnnotationConsts {
  public static final Both BOTH = new Both() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return Both.class;
    }
  };

  public static final East EAST = new East() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return East.class;
    }
  };

  public static final West WEST = new West() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return West.class;
    }
  };

  public static Annotation getAnnotationForTeam(final Team team) {
    switch (team) {
      case EAST:
        return EAST;
      case WEST:
        return WEST;
      case BOTH:
        return BOTH;
    }
    return null;
  }

  public static Annotation getAnnotationForString(final String team) {
    if (Team.EAST.name().equalsIgnoreCase(team)) {
      return EAST;
    }
    else if (Team.WEST.name().equalsIgnoreCase(team)) {
      return WEST;
    }
    else {
      return BOTH;
    }
  }
}
