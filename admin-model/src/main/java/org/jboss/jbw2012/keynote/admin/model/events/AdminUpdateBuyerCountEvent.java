package org.jboss.jbw2012.keynote.admin.model.events;


public class AdminUpdateBuyerCountEvent
{
  private final int buyerCount ;

  public AdminUpdateBuyerCountEvent(final int buyerCount)
  {
    this.buyerCount = buyerCount;
  }

  public int getBuyerCount()
  {
    return buyerCount;
  }
}
