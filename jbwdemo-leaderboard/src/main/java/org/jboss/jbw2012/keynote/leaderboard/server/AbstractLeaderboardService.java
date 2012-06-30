package org.jboss.jbw2012.keynote.leaderboard.server;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.ApprovalChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Connect;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ConnectNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ContestEnd;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.MiscTelemetry;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ScoreNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.User;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserNew;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserScore;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Mike Brock
 */
public abstract class AbstractLeaderboardService {
  @Inject protected Event<ConnectNotify> connectNotifyEvent;
  @Inject protected Event<UserNew> userNewEvent;
  @Inject protected Event<ScoreNotify> scoreNotifyEvent;
  @Inject protected Event<MiscTelemetry> miscTelemetryEvent;
  @Inject protected Event<RateChange> rateChangeEvent;
  @Inject protected Event<ApprovalChange> approvalChangeEvent;
  @Inject protected Event<ContestEnd> contestEndEvent;

  private final LeaderboardStats leaderboardStatsBoth = LeaderboardStats.create(Team.BOTH);
  private final LeaderboardStats leaderboardStatsEast = LeaderboardStats.create(Team.EAST);
  private final LeaderboardStats leaderboardStatsWest = LeaderboardStats.create(Team.WEST);

  private volatile int lastUserTotalBoth = -1;
  private volatile int lastUserTotalEast = -1;
  private volatile int lastUserTotalWest = -1;

  private final Map<Long, UserScore> scoreMap = new ConcurrentSkipListMap<Long, UserScore>();

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

  @PreDestroy
  public void destroyExecutor() throws Exception {
    executorService.shutdown();
    executorService.awaitTermination(10, TimeUnit.SECONDS);
  }

  protected LeaderboardStats getLeaderboardStats(final Team team) {
    switch (team) {
      case BOTH:
        return leaderboardStatsBoth;
      case EAST:
        return leaderboardStatsEast;
      case WEST:
        return leaderboardStatsWest;
    }
    return leaderboardStatsBoth;
  }

  public void observeUserConnect(@Observes final Connect connect) {
    connectNotifyEvent.fire(new ConnectNotify(getUserScoresByTeam(connect.getTeam())));
    flushCaches(connect.getTeam());
    notifyAllIfRateChanged();
    notifyAllMiscTelemetry();
    notifyAllIfAwaitingApprovalChanged();
  }

  protected UserScore recordNewUser(final User user) {
    final UserScore userScore = UserScore.create(user);
    scoreMap.put(user.getId(), userScore);
    return userScore;
  }

  protected List<UserScore> getUserScores() {
    return Collections.unmodifiableList(new ArrayList<UserScore>(scoreMap.values()));
  }

  protected UserScore getUserScoreById(final Long id) {
    return scoreMap.get(id);
  }

  protected List<UserScore> getUserScoresByTeam(final Team team) {
    if (team == Team.BOTH) {
      return new ArrayList<UserScore>(getUserScores());
    }

    final List<UserScore> teamScores = new ArrayList<UserScore>();
    for (UserScore userScore : new ArrayList<UserScore>(getUserScores())) {
      if (team == userScore.getUser().getTeam()) {
        teamScores.add(userScore);
      }
    }
    return Collections.unmodifiableList(teamScores);
  }

  protected int getActiveUsers() {
    return getUserScores().size();
  }

  protected int getTotalUserScoresByTeam(final Team team) {
    switch (team) {
      case EAST:
        return getUserScoresByTeam(team).size();
      case WEST:
        return getUserScoresByTeam(team).size();
      default:
        return getActiveUsers();
    }
  }

  protected int getTotalUserScoresByTeamIfChanged(final Team team) {
    int value = getTotalUserScoresByTeam(team);
    switch (team) {
      case EAST:
        return value != lastUserTotalEast ? lastUserTotalEast = value : -1;
      case WEST:
        return value != lastUserTotalWest ? lastUserTotalWest = value : -1;
      default:
        return value != lastUserTotalBoth ? lastUserTotalBoth = value : -1;
    }
  }

  protected void incrementSalesAmount(final Team team, final double delta) {
    assertNotBoth(team);

    getLeaderboardStats(team).incrementSalesAmount(delta);
    getLeaderboardStats(Team.BOTH).incrementSalesAmount(delta);
  }

  protected void incrementTotalOrdered(final Team team, final int delta) {
    assertNotBoth(team);

    getLeaderboardStats(team).incrementTotalOrderedAmount(delta);
    getLeaderboardStats(Team.BOTH).incrementTotalOrderedAmount(delta);
  }

  protected void incrementTotalApproved(final Team team, final int delta) {
    assertNotBoth(team);

    getLeaderboardStats(team).incrementTotalApprovedAmount(delta);
    getLeaderboardStats(Team.BOTH).incrementTotalApprovedAmount(delta);
  }

  protected void incrementTotalRejected(final Team team, final int delta) {
    assertNotBoth(team);

    getLeaderboardStats(team).incrementTotalRejectedAmount(delta);
    getLeaderboardStats(Team.BOTH).incrementTotalRejectedAmount(delta);
  }

  protected void pushTxRate(final Team team, final double rate) {
    assertNotBoth(team);

    getLeaderboardStats(team).pushTxRate(rate);
    getLeaderboardStats(Team.BOTH).pushTxRate(rate);
  }

  private static void assertNotBoth(final Team team) {
    if (team == Team.BOTH) throw new RuntimeException("Cannot update BOTH directly; increment only EAST or WEST");
  }

  protected ScheduledExecutorService getExecutorService() {
    return executorService;
  }

  protected static Team getTeamFromString(final String string) {
    if (Team.EAST.name().equalsIgnoreCase(string)) {
      return Team.EAST;
    }
    else if (Team.WEST.name().equalsIgnoreCase(string)) {
      return Team.WEST;
    }
    else {
      return Team.BOTH;
    }
  }

  protected static <T> void fireForTeam(final Event<T> event, final Team team, final T obj) {
    event.select(AnnotationConsts.getAnnotationForTeam(team)).fire(obj);
  }

  protected static <T> void fireForTeamAndBoth(final Event<T> event, final Team team, final T obj) {
    fireForTeam(event, team, obj);
    fireForTeam(event, Team.BOTH, obj);
  }

  protected void flushCaches(final Team team) {
    lastUserTotalBoth = -1;
    switch (team) {
       case EAST:
         lastUserTotalEast = -1;
         break;
       case WEST:
         lastUserTotalWest = -1;
    }

    getLeaderboardStats(team).flushCache();
  }

  protected void notifyAllIfRateChanged() {
    for (Team team : Team.values()) notifyIfRateChanged(team);
  }

  protected void notifyIfRateChanged(final Team team) {
    final double rate = getLeaderboardStats(team).sampleTxRateIfChanged();
    if (rate != -1) {
      fireForTeam(rateChangeEvent, team, new RateChange(rate));
    }
  }

  protected void notifyAllMiscTelemetry() {
    for (Team team : Team.values()) notifyMiscTelemetry(team);
  }

  protected void notifyMiscTelemetry(Team team) {
    final double totalApproved = getLeaderboardStats(team).getSalesAmount();
    final int totalUsers = getTotalUserScoresByTeamIfChanged(team);

    if (totalApproved != -1 || totalUsers != -1) {
      fireForTeam(miscTelemetryEvent, team, new MiscTelemetry(totalApproved, totalUsers));
    }
  }

  protected void notifyAllIfAwaitingApprovalChanged() {
    for (Team team : Team.values()) notifyIfAwaitingApprovalChanged(team);
  }

  protected void notifyIfAwaitingApprovalChanged(Team team) {
    final int awaitingApproval = getLeaderboardStats(team).getAwaitingApprovalIfChanged();

    if (awaitingApproval != -1) {
      fireForTeam(approvalChangeEvent, team, new ApprovalChange(awaitingApproval / 100.00));
    }
  }
  
  protected double getSumUserScoresByTeam(final Team team) {
    switch (team) {
      case EAST:
        return sum(getUserScoresByTeam(team));
      case WEST:
        return sum(getUserScoresByTeam(team));
      default:
        return sum(getUserScores());
    }
  }

  protected double sum(final List<UserScore> scores) {
    double total = 0 ;
    for(UserScore score: scores) {
      total += score.getScore();
    }
    return total;
  }

  protected void notifyContestEnd(final List<User> winners) {
    executorService.shutdown();
    Team winningTeam;
    double eastScore = getSumUserScoresByTeam(Team.EAST);
    double westScore = getSumUserScoresByTeam(Team.WEST);
    if (westScore > eastScore) {
      winningTeam = Team.WEST;
    } else if (eastScore > westScore) {
      winningTeam = Team.EAST;
    }
    else {
      winningTeam = Team.BOTH;
    }

    User winningUser = null;
    double score = 0;

    for (UserScore userScore : getUserScores()) {
      final double currentScore = userScore.getScore();
      if (currentScore >= score) {
        score = currentScore;
        winningUser = userScore.getUser();
      }
    }
    
    final List<User> lotteryWinners = new ArrayList<User>();
    final int numWinners = winners.size() - 1; // includes one spare
    for(User winner: winners) {
      if (!winner.getId().equals(winningUser.getId())) {
        lotteryWinners.add(winner);
      }
      if (lotteryWinners.size() == numWinners) {
        break;
      }
    }

    contestEndEvent.fire(new ContestEnd(winningUser, winningTeam, lotteryWinners));
  }
}
