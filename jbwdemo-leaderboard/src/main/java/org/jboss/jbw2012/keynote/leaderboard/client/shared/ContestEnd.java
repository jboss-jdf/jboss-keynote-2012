package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

import java.util.List;

/**
 * @author Mike Brock
 */
@Portable
public class ContestEnd {
  private final User contestWinner;
  private final Team winningTeam;
  private final List<User> lotteryWinners;

  public ContestEnd(@MapsTo("contestWinner") User contestWinner,
                    @MapsTo("winningTeam") Team winningTeam,
                    @MapsTo("lotteryWinners") List<User> lotteryWinners) {
    this.contestWinner = contestWinner;
    this.winningTeam = winningTeam;
    this.lotteryWinners = lotteryWinners;
  }

  public User getContestWinner() {
    return contestWinner;
  }

  public Team getWinningTeam() {
    return winningTeam;
  }

  public List<User> getLotteryWinners() {
    return lotteryWinners;
  }
}
