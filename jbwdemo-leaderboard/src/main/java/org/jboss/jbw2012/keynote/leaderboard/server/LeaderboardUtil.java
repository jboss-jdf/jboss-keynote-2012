package org.jboss.jbw2012.keynote.leaderboard.server;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserScore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Mike Brock
 */
public final class LeaderboardUtil {
  private LeaderboardUtil() {}

  public static final List<UserScore> getUserScoresByTeam(final Team team, final Collection<UserScore> userScores) {
     if (team == Team.BOTH) {
       return new ArrayList<UserScore>(userScores);
     }

     final List<UserScore> teamScores = new ArrayList<UserScore>();
     for (UserScore userScore : new ArrayList<UserScore>(userScores)) {
       if (team == userScore.getUser().getTeam()) {
         teamScores.add(userScore);
       }
     }
     return Collections.unmodifiableList(teamScores);
   }
}
