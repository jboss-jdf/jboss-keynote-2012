package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Mike Brock
 */
public class PlotOptions extends JavaScriptObject {

  protected PlotOptions() {
  }

  public static native PlotOptions create() /*-{
    return {};
  }-*/;

  public final native void setXAxisOptions(AxisOptions axisOptions) /*-{
    this.xaxis = axisOptions;
  }-*/;

  public final native void setYAxisOptions(AxisOptions axisOptions) /*-{
    this.yaxis = axisOptions;
  }-*/;

  public final native AxisOptions getXAxisOptions() /*-{
    return this.xaxis;
  }-*/;

  public final native AxisOptions getYAxisOptions() /*-{
    return this.yaxis;
  }-*/;

  public final native void setBarOptions(BarOptions barOptions) /*-{
    this.bars = barOptions;
  }-*/;
}
