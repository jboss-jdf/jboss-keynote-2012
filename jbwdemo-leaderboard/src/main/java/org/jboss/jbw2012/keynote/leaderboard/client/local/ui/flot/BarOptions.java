package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Mike Brock
 */
public class BarOptions extends JavaScriptObject {
  protected BarOptions() {
  }

  public static native BarOptions create() /*-{
    return {};
  }-*/;

  public final native void setBarWidth(double width) /*-{
    this.barWidth = width;
  }-*/;
}
