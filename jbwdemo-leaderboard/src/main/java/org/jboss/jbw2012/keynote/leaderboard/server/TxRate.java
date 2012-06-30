package org.jboss.jbw2012.keynote.leaderboard.server;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Mike Brock
 */
public class TxRate {
  private final Queue<Tick> ticks = new ConcurrentLinkedQueue<Tick>();
  private final long sampleDuration = TimeUnit.SECONDS.toMillis(10);

  public void push(double value) {
    ticks.add(new Tick(System.currentTimeMillis(), value));
  }

  private volatile double lastSample = -1;

  /**
   * Returns a sample if the current sample rate is different from the last time {@link #sample()} or
   * <tt>sampleIfChanged()</tt> was called. Otherwise this method returns <tt>-1</tt>.
   *
   * @return Returns new sample rate if rate has changed, or <tt>-1</tt> if it hasn't.
   */
  public double sampleIfChanged() {
    double newSample = sample();
    if (lastSample != newSample) {
      return lastSample = newSample;
    }
    else {
      return -1;
    }
  }

  public double sample() {
    if (ticks.isEmpty()) return 0d;

    double rate = 0;

    final long toRange = range();
    final Iterator<Tick> tickIterator = ticks.iterator();
    while (tickIterator.hasNext()) {
      final Tick tick = tickIterator.next();
      if (tick.time >= toRange) {
        rate += tick.value;
      }
      else {
        tickIterator.remove();
      }
    }

    return rate;
  }

  private long range() {
    return System.currentTimeMillis() - sampleDuration;
  }

  private static final class Tick {
    private final long time;
    private final double value;

    private Tick(long time, double value) {
      this.time = time;
      this.value = value;
    }
  }

  public void flushCache() {
    lastSample = -1;
  }
}
