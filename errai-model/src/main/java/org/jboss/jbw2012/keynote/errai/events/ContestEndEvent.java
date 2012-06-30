package org.jboss.jbw2012.keynote.errai.events;

import java.util.List ;

public class ContestEndEvent
{
  private final List<Long> winners;

  public ContestEndEvent(final List<Long> winners)
  {
    this.winners = winners;
  }

  public List<Long> getWinners()
  {
    return winners;
  }
}
