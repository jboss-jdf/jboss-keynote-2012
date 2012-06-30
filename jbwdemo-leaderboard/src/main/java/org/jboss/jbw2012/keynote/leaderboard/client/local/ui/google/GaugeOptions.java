package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.google;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;

/**
 * @author Mike Brock
 */
public class GaugeOptions extends JavaScriptObject {

  protected GaugeOptions() {
  }

  public static native GaugeOptions create() /*-{
    return {};
  }-*/;

  public native final void setWidth(int width) /*-{
    this.width = width;
  }-*/;

  public native final void setHeight(int height) /*-{
    this.height = height;
  }-*/;

  public native final void setGreenFrom(double greenFrom) /*-{
    this.greenFrom = greenFrom;
  }-*/;

  public native final void setGreenTo(double greenTo) /*-{
    this.greenTo = greenTo;
  }-*/;

  public native final void setMaxValue(double maxValue) /*-{
    this.max = maxValue;
  }-*/;

  public native final void setMinValue(double minValue) /*-{
    this.min = minValue;
  }-*/;

  public native final void setAnimationDuration(double millis) /*-{
    this.animation.duration = millis;
  }-*/;

  public native final void setRedFrom(double redFrom) /*-{
    this.redFrom = redFrom;
  }-*/;

  public native final void setRedTo(double redTo) /*-{
    this.redTo = redTo;
  }-*/;

  public native final void setYellowFrom(double yellowFrom) /*-{
    this.yellowFrom = yellowFrom;
  }-*/;

  public native final void setYellowTo(double yellowTo) /*-{
    this.yellowTo = yellowTo;
  }-*/;

  public native final void setMinorTicks(double minorTicks) /*-{
    this.minorTicks = minorTicks;
  }-*/;

  public native final void setMajorTicks(double majorTicks) /*-{
    this.majorTicks = majorTicks;
  }-*/;
}
