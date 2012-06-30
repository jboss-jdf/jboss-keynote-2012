package org.jboss.jbw2012.keynote.leaderboard.client.local;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import org.jboss.errai.common.client.util.TimeUnit;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.container.IOCBeanManager;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.ContestEndPanel;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.LeaderboardPanel;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.AxisOptions;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.Converter;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.DataSeries;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.Plot;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.PlotOptions;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.Util;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google.Gauge;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google.GaugeOptions;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers.AllObserverAdapter;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers.EastObserverAdapter;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers.ObserverAdapter;
import org.jboss.jbw2012.keynote.leaderboard.client.local.ui.observers.WestObserverAdapter;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Connect;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ConnectNotify;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.ContestEnd;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.RateTick;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.UserScore;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.google.gwt.user.client.DOM.getElementById;

/**
 * Main application entry point.
 */
@EntryPoint
public class JBWLeaderboard {
  private static final double HISTORY_TICK_SIZE = TimeUnit.SECONDS.toMillis(10);
  private static final double MAX_HISTORY_SIZE = TimeUnit.MINUTES.toMillis(5);
  private static final double MAX_TX_RATE = 4000;

  @Inject private Event<Connect> messageEvent;
  @Inject private RootPanel rootPanel;
  @Inject private LeaderboardPanel leaderboardPanel;
  @Inject private IOCBeanManager manager;

  private Team team = Team.BOTH;
  private ObserverAdapter observerAdapter;

  private DataSeries dataSeries;
  private PlotOptions plotOptions;
  private Plot plot;

  private Gauge awaitingApprovals;
  private Gauge transactionRate;

  private final Queue<RateTick> rateHistory = new LinkedList<RateTick>();
  private final List<RateTick> averagedData = new ArrayList<RateTick>();

  private Timer plotUpdateTimer;

  @PostConstruct
  public void buildUI() {
    rootPanel.add(leaderboardPanel);

    int historySize = (int) Math.round(MAX_HISTORY_SIZE / HISTORY_TICK_SIZE);
    for (int i = 0; i < historySize; i++) {
      averagedData.add(new RateTick(0, 0));
    }

    dataSeries = DataSeries.of("Users");
    dataSeries.setBarOptions(true, 1, "center", false);

    plotOptions = PlotOptions.create();

    final AxisOptions xAxis = AxisOptions.create();

    xAxis.setTickFormatter(new Converter<String, AxisOptions>() {
      @Override
      public String handle(double iterationIndex, AxisOptions toVisit) {
        return "";
      }
    });

    plotOptions.setXAxisOptions(xAxis);

    final AxisOptions yAxis = AxisOptions.create();

    yAxis.setMaxValue(MAX_TX_RATE);
    plotOptions.setYAxisOptions(yAxis);

    leaderboardPanel.resetLeaderboard();

    leaderboardPanel.addTitleClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        final ListBox listBox = new ListBox();

        int sel = 0;
        final Team[] values = Team.values();
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
          Team t = values[i];
          if (t == team) {
            sel = i;
          }
          listBox.addItem(t.name());
        }

        listBox.setItemSelected(sel, true);

        listBox.addChangeHandler(new ChangeHandler() {
          @Override
          public void onChange(ChangeEvent event) {
            rootPanel.remove(listBox);
            final int selectedIndex = listBox.getSelectedIndex();
            team = Team.valueOf(listBox.getItemText(selectedIndex));
            connect();
          }
        });

        listBox.addClickHandler(new ClickHandler() {
          int clicks = 0;
          @Override
          public void onClick(ClickEvent event) {
            if (clicks++ > 0)
              rootPanel.remove(listBox);
          }
        });

        final Style style = listBox.getElement().getStyle();
        style.setPosition(Style.Position.ABSOLUTE);
        style.setTop(5, Style.Unit.PX);
        style.setLeft(5, Style.Unit.PX);
        style.setZIndex(Integer.MAX_VALUE);

        rootPanel.add(listBox);
      }
    });

    Gauge.setOnLoadCallback(new Runnable() {
      @Override
      public void run() {
        transactionRate = new Gauge(getElementById("txGauge"), "Tx Rate");

        final GaugeOptions options = transactionRate.getOptions();
        options.setWidth(185);
        options.setMaxValue(MAX_TX_RATE);
        options.setMinorTicks(500);

        options.setGreenFrom(0);
        options.setGreenTo(2499);
        options.setYellowFrom(2500);
        options.setYellowTo(2999);
        options.setRedFrom(3000);
        options.setRedTo(MAX_TX_RATE);

        transactionRate.draw();

        awaitingApprovals = new Gauge(getElementById("awaitApproval"), "Backlog");

        final GaugeOptions options1 = awaitingApprovals.getOptions();
        options1.setWidth(185);
        options1.setMaxValue(MAX_TX_RATE);
        options1.setMinorTicks(500);

        options1.setGreenFrom(0);
        options1.setGreenTo(2499);
        options1.setYellowFrom(2500);
        options1.setYellowTo(2999);
        options1.setRedFrom(3000);
        options1.setRedTo(MAX_TX_RATE);

        awaitingApprovals.draw();
      }
    });

    onReady(new Runnable() {
      @Override
      public void run() {
        new Timer() {
          @Override
          public void run() {
            updatePlot();
            initDetectWake();
          }
        }.schedule(1000);
      }
    });
  }

  /**
   * Have jQuery run the callback function when it is done its initialization.
   *
   * @param runnable a Runnable callback.
   */
  private native static void onReady(final Runnable runnable) /*-{
    $wnd.$(runnable.@java.lang.Runnable::run()());
  }-*/;

  @AfterInitialization
  public void connect() {
    if (observerAdapter != null) {
      manager.destroyBean(observerAdapter);
    }

    switch (team) {
      case BOTH:
        observerAdapter = manager.lookupBean(AllObserverAdapter.class).newInstance();
        break;
      case WEST:
        observerAdapter = manager.lookupBean(WestObserverAdapter.class).newInstance();
        break;
      case EAST:
        observerAdapter = manager.lookupBean(EastObserverAdapter.class).newInstance();
        break;
    }

    stopPlotUpdates();
    leaderboardPanel.resetLeaderboard();
    messageEvent.fire(new Connect(team));
  }

  private void observesConnect(@Observes ConnectNotify connectNotify) {
    for (UserScore user : connectNotify.getUsers()) {
      leaderboardPanel.addUserScore(user);
    }

    leaderboardPanel.setTeamName(team.getDisplayName());

    startPlotUpdates();
  }

  private void observesContestEnd(@Observes ContestEnd contestEnd) {
    stopPlotUpdates();
    rootPanel.remove(leaderboardPanel);

    final ContestEndPanel panel = manager.lookupBean(ContestEndPanel.class).getInstance();
    panel.setWinningTeam(contestEnd.getWinningTeam());
    panel.setContestWinner(contestEnd.getContestWinner());
    panel.setLotteryWinners(contestEnd.getLotteryWinners());

    rootPanel.add(panel);
  }


  private void startPlotUpdates() {
    if (plotUpdateTimer == null) {
      plotUpdateTimer = new Timer() {
        @Override
        public void run() {
          updatePlot();
        }
      };
      plotUpdateTimer.scheduleRepeating((int) HISTORY_TICK_SIZE);
    }
  }

  private void stopPlotUpdates() {
    if (plotUpdateTimer != null) {
      plotUpdateTimer.cancel();
      plotUpdateTimer = null;
    }
  }

  private void updatePlot() {
    drawPlot(false);

    final double currTime = System.currentTimeMillis();
    final double maxHistory = currTime - MAX_HISTORY_SIZE;
    double index = maxHistory;

    trimHistory(rateHistory, maxHistory);

    final Iterator<RateTick> tickIterator = rateHistory.iterator();
    final List<RateTick> accumulator = new ArrayList<RateTick>();

    boolean forceRedraw = false;
    double maxValue = plotOptions.getYAxisOptions().getMaxValue();
    RateTick tick = null;

    for (int i = 0; i < averagedData.size(); index += HISTORY_TICK_SIZE) {
      accumulator.clear();

      for (; ; ) {
        if (tick == null) {
          if (tickIterator.hasNext()) {
            tick = tickIterator.next();
          }
          else {
            break;
          }
        }

        if (tick.getTime() < index) {
          accumulator.add(tick);
          tick = null;
        }
        else {
          // break and scan to the next index position.
          break;
        }
      }

      try {
        final double value;
        if (!accumulator.isEmpty()) {
          value = simpleAverage(accumulator);

          if (value > maxValue) {
            maxValue = value;
            plotOptions.getYAxisOptions().setMaxValue(maxValue * 1.2);
            forceRedraw = true;
          }
        }
        else {
          value = 0;
        }
        averagedData.set(i, new RateTick(index, value));
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
      i++;
    }

    dataSeries.setData(Util.toArray(averagedData, new Converter<JavaScriptObject, RateTick>() {
      @Override
      public JavaScriptObject handle(double i, RateTick toVisit) {
        return Util.point(String.valueOf(i), i, toVisit.getValue());
      }
    }));

    if (forceRedraw) {
      drawPlot(true);
    }

    plot.setData(Util.singletonArray(dataSeries));
    plot.setupGrid();
    plot.draw();
  }

  private static final void trimHistory(final Iterable<RateTick> iterable, final double minValue) {
    final Iterator<RateTick> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getTime() <= minValue) {
        iterator.remove();
      }
    }
  }

  private void drawPlot(boolean force) {
    if (force || plot == null) {
      plot = new Plot(getElementById("barChart"), dataSeries, plotOptions);
    }
  }

  private static double simpleAverage(final Collection<RateTick> ticks) {
    if (ticks.isEmpty()) return 0d;

    double d = 0;
    for (RateTick tick : ticks) {
      d += tick.getValue();
    }

    return d / ticks.size();
  }

  public LeaderboardPanel getPanel() {
    return leaderboardPanel;
  }

  public Queue<RateTick> getRateHistory() {
    return rateHistory;
  }

  public Gauge getTransactionRate() {
    return transactionRate;
  }

  public Gauge getAwaitingApprovals() {
    return awaitingApprovals;
  }

  double lastTxRateTm;

  public void setTransactionRate(double txRate) {
    if (transactionRate != null) {
      rateHistory.add(new RateTick(lastTxRateTm = System.currentTimeMillis(), txRate));
      transactionRate.setValue(Math.round(txRate));
      transactionRate.draw();
    }
  }

  public void setAwaitingApprovals(double approvals) {
    if (awaitingApprovals != null) {
      awaitingApprovals.setValue(Math.round(approvals));
      awaitingApprovals.draw();
    }
  }

  public void setSalesPeopleCount(int salesPeopleCount) {
    if (salesPeopleCount != -1) {
      leaderboardPanel.setSalesPeople(salesPeopleCount);
    }
  }

  public void setTotalSalesAmount(double amount) {
    if (amount != -1) {
      leaderboardPanel.setTotalSalesAmount(amount);
    }
  }

  private static final int WAKE_PULSE_TIMER = 2000;
  private Timer wakeTimer;

  private void initDetectWake() {
    if (wakeTimer != null) return;

    wakeTimer = new Timer() {
      private long time = System.currentTimeMillis();

      @Override
      public void run() {
        final long newTime = System.currentTimeMillis();
        if ((newTime - time) > (20 * 1000)) {
          connect();
        }
        time = newTime;
      }
    };
    wakeTimer.scheduleRepeating(WAKE_PULSE_TIMER);
  }
}