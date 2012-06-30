package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.HasValue;

import java.util.List;

/**
 * @author Mike Brock
 * @author Jonathan Fuerth
 */
public class DataSeries extends JavaScriptObject {

  protected DataSeries() {
  }

  public static DataSeries of(String name, Double... data) {
    @SuppressWarnings("unchecked")
    final JsArray<JavaScriptObject> jsArray = (JsArray<JavaScriptObject>) JsArray.createArray();

    for (int x = 0; x < data.length; x++) {
      jsArray.push(Util.point(x, data[x]));
    }

    final DataSeries series = create();
    series.setName(name);
    series.setData(jsArray);
    return series;
  }

  private static native DataSeries create() /*-{
    return {};
  }-*/;

  public final native void setData(JsArray<JavaScriptObject> jsArray) /*-{
    this.data = jsArray;
  }-*/;

  public final native JsArray<JavaScriptObject> getData() /*-{
    return this.data;
  }-*/;

  public final native void setName(String name) /*-{
    this.name = name;
  }-*/;

  public final native String getName() /*-{
    return this.name;
  }-*/;

  // note there are more options than this. would be sensible to create a class of all bar options.
  public final native void setBarOptions(boolean show, double barWidth, String align, boolean horizontal) /*-{
    this.bars = { show: show, barWidth: barWidth, align: align, horizontal: horizontal};
  }-*/;
}
