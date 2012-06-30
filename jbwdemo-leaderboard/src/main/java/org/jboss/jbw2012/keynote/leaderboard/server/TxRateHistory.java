package org.jboss.jbw2012.keynote.leaderboard.server;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateTick;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Mike Brock
 */
public class TxRateHistory {
  private final Queue<Tick> ticks = new ConcurrentLinkedQueue<Tick>();

  public void push(double value) {
    ticks.add(new Tick(System.currentTimeMillis(), value));
  }


  public List<RateTick> getHistory(final double step, final int amount) {
    final List<RateTick> rateTickList = new ArrayList<RateTick>();

    double end = System.currentTimeMillis();
    double start = end - (step * amount);
    double min = start - step;

    final Iterator<Tick> tickIterator = ticks.iterator();
    final List<Tick> accumulator = new ArrayList<Tick>();

    while (tickIterator.hasNext()) {
      accumulator.clear();

      do {
        final Tick tick = tickIterator.next();

        if (tick.time <= min) {
          tickIterator.remove();
        }
        else if (tick.time < start && tick.time < end) {
          accumulator.add(tick);
        }
      }
      while (tickIterator.hasNext());

      rateTickList.add(new RateTick(start, simpleAverage(accumulator)));

      start += step;

      if (start <= end) {
        break;
      }
    }

    return Collections.unmodifiableList(rateTickList);
  }

  private static double simpleAverage(final Collection<Tick> ticks) {

    if (ticks.isEmpty()) return 0d;

    double d = 0;
    for (Tick tick : ticks) {
      d += tick.value;
    }

    return d / ticks.size();
  }

  private static final class Tick {
    private final long time;
    private final double value;

    private Tick(long time, double value) {
      this.time = time;
      this.value = value;
    }
  }
}
