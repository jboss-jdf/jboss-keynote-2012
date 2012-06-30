package org.jboss.jbw2012.keynote.leaderboard.server;

import org.jboss.jbw2012.keynote.errai.events.ContestEndEvent ;
import org.jboss.jbw2012.keynote.errai.events.UpdateRateEvent;
import org.jboss.jbw2012.keynote.errai.events.UpdateTotal;
import org.jboss.jbw2012.keynote.errai.events.UpdateTotalEvent;
import org.jboss.jbw2012.keynote.errai.events.UserInfo;
import org.jboss.jbw2012.keynote.errai.events.UserUpdateEvent;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Role;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ScoreNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.User;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserNew;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserScore;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;

import java.util.ArrayList ;
import java.util.List ;
import java.util.concurrent.TimeUnit;

/**
 * Leaderboard proxy service for events.
 */
@ApplicationScoped
@Alternative
public class LeaderboardService extends AbstractLeaderboardService {

  @PostConstruct
  public void initialiseRateScheduler() {
    getExecutorService().scheduleAtFixedRate(new RateUpdateCommand(), 0, 1, TimeUnit.SECONDS);
  }

  public void userUpdate(@Observes final UserUpdateEvent userUpdateEvent) {
    for (UserInfo userInfo : userUpdateEvent.getUsers()) {
      final UserScore current = getUserScoreById(userInfo.getId());

      if (current == null) {
        final UserScore userScore = recordNewUser(new User(userInfo.getId(),
                userInfo.getName(), Role.valueOf(userInfo.getRole()),
                Team.valueOf(userInfo.getTeam())));

        fireForTeamAndBoth(userNewEvent, userScore.getUser().getTeam(), new UserNew(userScore));
      }
    }

    notifyAllMiscTelemetry();
  }

  public void updateTotal(@Observes final UpdateTotalEvent updateTotalEvent) {
    for (UpdateTotal updateTotal : updateTotalEvent.getUpdateTotals()) {
      final long userId = updateTotal.getUserId();
      final UserScore userScore = getUserScoreById(userId);
      final Team team = userScore.getUser().getTeam();

      final double oldScore = userScore.getScore();
      userScore.setScore(updateTotal.getApproved() / 100.00);
      incrementSalesAmount(team, userScore.getScore() - oldScore);

      fireForTeamAndBoth(scoreNotifyEvent, team, new ScoreNotify(userId, userScore.getScore()));

      final int ordered = (int) updateTotal.getOrdered();
      incrementTotalOrdered(team, ordered - userScore.getOrdered());
      userScore.setOrdered(ordered);

      final int approved = (int) updateTotal.getApproved();
      incrementTotalApproved(team, approved - userScore.getApproved());
      userScore.setApproved(approved);

      final int rejected = (int) updateTotal.getRejected();
      incrementTotalRejected(team, rejected - userScore.getRejected());
      userScore.setRejected(rejected);

      notifyAllMiscTelemetry();
      notifyAllIfAwaitingApprovalChanged();
    }
  }
  
  public void winners(final @Observes ContestEndEvent winnerEvent) {
    final List<User> winners = new ArrayList<User>();
    for(Long userId: winnerEvent.getWinners()) {
      winners.add(getUserScoreById(userId).getUser());
    }
    notifyContestEnd(winners) ;
  }

  public void updateRate(@Observes final UpdateRateEvent updateRateEvent) {
    pushTxRate(getTeamFromString(updateRateEvent.getTeam()), updateRateEvent.getRate() / 100.00);
    notifyAllIfRateChanged();
  }

  public class RateUpdateCommand implements Runnable {
    public void run() {
      notifyAllIfRateChanged();
      notifyAllMiscTelemetry();
    }
  }
}
