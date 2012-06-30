package org.jboss.jbw2012.keynote.leaderboard.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

/**
 * @author Mike Brock
 */
@Portable
public class MiscTelemetry {
  private final double totalSalesAmount;
  private final int totalSalesPeople;

  public MiscTelemetry(@MapsTo("totalSalesAmount") final double totalSalesAmount,
                       @MapsTo("totalSalesPeople") final int totalSalesPeople) {
    this.totalSalesAmount = totalSalesAmount;
    this.totalSalesPeople = totalSalesPeople;
  }

  public double getTotalSalesAmount() {
    return totalSalesAmount;
  }

  public int getTotalSalesPeople() {
    return totalSalesPeople;
  }
}
