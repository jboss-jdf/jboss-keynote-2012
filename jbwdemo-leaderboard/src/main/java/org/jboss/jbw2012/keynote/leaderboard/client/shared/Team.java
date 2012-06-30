package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public enum Team {
  EAST {
    {
      displayName = "East";
    }
  },
  WEST {
    {
      displayName = "West";
    }
  }
  , BOTH {
    {
      displayName = "Grand Totals";
    }
  };

  public String displayName;

  public String getDisplayName() {
    return displayName;
  }
}
