package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Mike Brock
 */
public class AxisOptions extends JavaScriptObject {
  protected AxisOptions() {
  }

  public static native AxisOptions create() /*-{
    return {};
  }-*/;

  public final native void setTickFormatter(Converter<String, AxisOptions> formatter) /*-{
    this.tickFormatter = function (index, axisOptions) {
      return formatter.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.Converter::handle(DLjava/lang/Object;)(index, axisOptions);
    }
  }-*/;

  public final native void setBarOptions(BarOptions barOptions) /*-{
    this.bars = barOptions;
  }-*/;

  public final native void setTickSize(double tickSize) /*-{
    this.tickSize = tickSize;
  }-*/;

  public final native void setMinimumTickSize(double tickSize) /*-{
    this.minTickSize = tickSize;
  }-*/;

  public final native void setMinValue(double value) /*-{
    this.min = value;
  }-*/;

  public final native void setMaxValue(double value) /*-{
    this.max = value;
  }-*/;

  public final native double getMinValue() /*-{
    return this.min;
  }-*/;


  public final native double getMaxValue() /*-{
    return this.max;
  }-*/;

  public final native JsArray<JsArray> getData() /*-{
     return this.data;
  }-*/;
}
