package org.jboss.jbw2012.keynote.leaderboard.server;

import com.google.common.util.concurrent.AtomicDouble;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mike Brock
 */
public class LeaderboardStats {
  private final AtomicDouble totalSalesAmount = new AtomicDouble();
  private final AtomicInteger totalOrderedAmount = new AtomicInteger();
  private final AtomicInteger totalApprovedAmount = new AtomicInteger();
  private final AtomicInteger totalRejectedAmount = new AtomicInteger();

  private volatile double lastSalesAmount = -1;
  private volatile int lastOrderedAmount = -1;
  private volatile int lastApprovedAmount = -1;
  private volatile int lastRejectedAmount = -1;
  private volatile int lastAwaitingApprovalAmount = -1;

  private final TxRate txRate = new TxRate();
  private final Team team;

  private LeaderboardStats(Team team) {
    this.team = team;
  }

  public static LeaderboardStats create(Team team) {
    return new LeaderboardStats(team);
  }

  public Team getTeam() {
    return team;
  }

  public void pushTxRate(double rate) {
    txRate.push(rate);
  }

  public void setSalesAmount(double amount) {
    totalSalesAmount.set(amount);
  }

  public double incrementSalesAmount(double amount) {
    return totalSalesAmount.addAndGet(amount);
  }

  public double sampleTxRate() {
    return txRate.sample();
  }

  public double sampleTxRateIfChanged() {
    return txRate.sampleIfChanged();
  }

  public double getSalesAmount() {
    return totalSalesAmount.get();
  }

  public double getSalesAmountIfChanged() {
    double value = getSalesAmount();
    if (value != lastSalesAmount) {
      return lastSalesAmount = value;
    }
    else {
      return -1;
    }
  }

  public void incrementTotalOrderedAmount(int delta) {
    totalOrderedAmount.addAndGet(delta);
  }

  public void incrementTotalApprovedAmount(int delta) {
    totalApprovedAmount.addAndGet(delta);
  }

  public void incrementTotalRejectedAmount(int delta) {
    totalRejectedAmount.addAndGet(delta);
  }

  public int getTotalOrderedAmount() {
    return totalOrderedAmount.get();
  }

  public int getTotalApprovedAmount() {
    return totalApprovedAmount.get();
  }

  public int getTotalRejectedAmount() {
    return totalRejectedAmount.get();
  }

  private int calculateAwaitingApproval() {
    return getTotalOrderedAmount() - (getTotalApprovedAmount() + getTotalRejectedAmount());
  }

  public int getAwaitingApproval() {
    return lastAwaitingApprovalAmount = calculateAwaitingApproval();
  }

  public int getTotalOrderedAmountIfChanged() {
    int value = getTotalOrderedAmount();
    if (lastOrderedAmount != value) {
      return lastOrderedAmount = value;
    }
    else {
      return -1;
    }
  }

  public int getTotalApprovedAmountIfChanged() {
    int value = getTotalApprovedAmount();
    if (lastApprovedAmount != value) {
      return lastApprovedAmount = value;
    }
    else {
      return -1;
    }
  }

  public int getTotalRejectedAmountIfChanged() {
    int value = getTotalRejectedAmount();
    if (lastRejectedAmount != value) {
      return lastRejectedAmount = value;
    }
    else {
      return -1;
    }
  }

  public int getAwaitingApprovalIfChanged() {
    int value = calculateAwaitingApproval();
    if (lastAwaitingApprovalAmount != value) {
      return lastAwaitingApprovalAmount = value;
    }
    else {
      return -1;
    }
  }

  public void flushCache() {
    lastApprovedAmount = -1;
    lastAwaitingApprovalAmount = -1;
    lastOrderedAmount = -1;
    lastRejectedAmount = -1;
    lastSalesAmount = -1;
    txRate.flushCache();
  }
}
