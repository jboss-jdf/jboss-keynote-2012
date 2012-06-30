package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;

/**
 * @author Mike Brock
 */
public class Plot {
  private final JavaScriptObject _plot;

  public Plot(final Element divElement,
              final DataSeries dataSeries,
              final PlotOptions options) {

    _plot = _instantiateFlotPlot(divElement, dataSeries, options);

    final String width = divElement.getStyle().getWidth();

    if (width == null || "".equals(width)) {
      int parentWidth = divElement.getParentElement().getOffsetWidth();

      System.out.println("parentWidth:" + parentWidth);

      divElement.getStyle().setWidth(parentWidth, Style.Unit.PX);
    }
  }

  private static native JavaScriptObject _instantiateFlotPlot(Element divElement,
                                                              JavaScriptObject data,
                                                              PlotOptions options) /*-{

    return $wnd.jQuery.plot(divElement, [data], options);
  }-*/;

  /**
   * Recalculate and set axis scaling, ticks, legend etc.
   * <p/>
   * Note that because of the drawing model of the canvas, this
   * function will immediately redraw (actually reinsert in the DOM)
   * the labels and the legend, but not the actual tick lines because
   * they're drawn on the canvas. You need to call draw() to get the
   * canvas redrawn.
   */
  public native void setupGrid() /*-{
    this.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.Plot::_plot.setupGrid();
  }-*/;

  public native void draw() /*-{
    this.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.Plot::_plot.draw();
  }-*/;

  public native void setData(JsArray<JavaScriptObject> data) /*-{
    this.@org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot.Plot::_plot.setData(data);
  }-*/;
}
