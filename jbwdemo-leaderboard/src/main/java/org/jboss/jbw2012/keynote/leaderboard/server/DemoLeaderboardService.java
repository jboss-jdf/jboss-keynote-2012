package org.jboss.jbw2012.keynote.leaderboard.server;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.Connect;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ConnectNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ContestEnd;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.MiscTelemetry;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ScoreNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.User;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserScore;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A very simple CDI based service.
 */
@ApplicationScoped
@Alternative
public class DemoLeaderboardService extends AbstractLeaderboardService {
  private final Map<Long, Double> speedBias = new ConcurrentHashMap<Long, Double>();
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  public DemoLeaderboardService() {
    System.out.println("**** LOAD DEMO! ****");
  }

  @PostConstruct
  private void setupGame() {
    for (int i = 0; i < 20; i++) {
      getNewUser();
    }
    executor.scheduleAtFixedRate(new FakeScoreUpdate(), 0, 2000, TimeUnit.MILLISECONDS);
    executor.scheduleAtFixedRate(new RateUpdate(), 0, 5, TimeUnit.SECONDS);
    executor.schedule(new Runnable() {
      @Override
      public void run() {
        final List<User> randomUsers = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
          randomUsers.add(getRandomUserScore().getUser());
        }

        notifyContestEnd(randomUsers);
      }
    }, 5, TimeUnit.SECONDS);


    System.out.println("*** DEMO RUNNING ***");
  }

  private UserScore getNewUser() {
    final User user = FakeFactory.newFakeUser();
    speedBias.put(user.getId(), FakeFactory.nextUnsignedDouble());
    return recordNewUser(user);
  }

  private UserScore getRandomUserScore() {
    final int rand = FakeFactory.nextUnsignedInt() % getActiveUsers();
    int i = 0;
    for (UserScore userScore : getUserScores()) {
      if (i++ == rand) {
        return userScore;
      }
    }
    return getRandomUserScore();
  }

  final class FakeScoreUpdate implements Runnable {
    @Override
    public void run() {
      try {
        final UserScore userScore = getRandomUserScore();
        final LeaderboardStats stats = getLeaderboardStats(userScore.getUser().getTeam());

        if (true || FakeFactory.nextUnsignedDouble() > speedBias.get(userScore.getUser().getId())) {
          final double incrValue = FakeFactory.nextUnsignedDouble() * 100;
          final Team team = userScore.getUser().getTeam();
          incrementSalesAmount(team, incrValue);
          pushTxRate(team, incrValue);
          userScore.incrementScore(incrValue);

          fireForTeamAndBoth(scoreNotifyEvent, team, new ScoreNotify(userScore.getUser().getId(), userScore.getScore()));
          fireForTeam(miscTelemetryEvent, team, new MiscTelemetry(stats.getSalesAmount(), getTotalUserScoresByTeam(team)));
          fireForTeam(miscTelemetryEvent, Team.BOTH,
                  new MiscTelemetry(getLeaderboardStats(Team.BOTH).getSalesAmount(), getActiveUsers()));
        }
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

  final class RateUpdate implements Runnable {
    @Override
    public void run() {
      notifyAllIfRateChanged();
    }
  }
}
