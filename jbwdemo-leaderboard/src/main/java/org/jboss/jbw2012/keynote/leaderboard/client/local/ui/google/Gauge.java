package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * A wrapper implementation for a Google API Gauge.
 *
 * @author Mike Brock
 */
public class Gauge {
  private final JavaScriptObject _gaugeInstance;
  private final JavaScriptObject _data;
  private final GaugeOptions options;

  public Gauge(final Element divElement, final String title) {
    _gaugeInstance = create(divElement);
    _data = defaultData(title);
    options = GaugeOptions.create();
  }

  public GaugeOptions getOptions() {
    return options;
  }

  private static native JavaScriptObject defaultData(String title) /*-{
    var gaugeData = new $wnd.google.visualization.DataTable();
    gaugeData.addColumn('number', title);
    gaugeData.addRows(1);
    gaugeData.setCell(0, 0, 0);

    return gaugeData;
  }-*/;

  private static native JavaScriptObject create(Element divElement) /*-{
    return new $wnd.google.visualization.Gauge(divElement);
  }-*/;

  public static native void setOnLoadCallback(Runnable runnable) /*-{
    if (typeof $wnd.google.visualization != "undefined") {
      runnable.@java.lang.Runnable::run()();
    }
    else {
      $wnd.google.setOnLoadCallback(function () {
        runnable.@java.lang.Runnable::run()();
      })
    }
  }-*/;

  public native final void draw() /*-{
    this.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google.Gauge::_gaugeInstance.draw(this.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google.Gauge::_data, this.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google.Gauge::options);
  }-*/;

  public native final void setValue(double value) /*-{
    this.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google.Gauge::_data.setValue(0, 0, value);
  }-*/;
}
