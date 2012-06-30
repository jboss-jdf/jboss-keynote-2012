package org.jboss.jbw2012.keynote.leaderboard.client.local.ui.flot;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import java.util.Collection;
import java.util.List;

/**
 * @author Mike Brock
 */
public class Util {
  public static native JsArray<JavaScriptObject> singletonArray(final JavaScriptObject obj) /*-{
    return [obj];
  }-*/;

  public static native JavaScriptObject point(final double x, final double y) /*-{
    return [x, y];
  }-*/;

  public static native JavaScriptObject point(final String label, final double x, final double y) /*-{
    var obj = [x, y];
    obj.label = label;
    return obj;
  }-*/;

  public static <T> JsArray<JavaScriptObject> toArray(final Iterable<T> data,
                                                      final Converter<JavaScriptObject, T> converter) {
    @SuppressWarnings("unchecked")
    final JsArray<JavaScriptObject> newJsArray = (JsArray<JavaScriptObject>) JavaScriptObject.createArray();

    double index = 0;
    for (T t : data) {
      newJsArray.push(converter.handle(index++, t));
    }

    return newJsArray;
  }

  public static <T> void pushToArray(final T value,
                                     final JsArray<JavaScriptObject> array,
                                     final Converter<JavaScriptObject, T> converter) {
    array.push(converter.handle(array.length(), value));
  }
}
