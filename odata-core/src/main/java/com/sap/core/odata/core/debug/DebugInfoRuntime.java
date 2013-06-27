package com.sap.core.odata.core.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sap.core.odata.api.processor.ODataContext.RuntimeMeasurement;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public class DebugInfoRuntime implements DebugInfo {

  private class RuntimeNode {
    protected String className;
    protected String methodName;
    protected long timeStarted;
    protected long timeStopped;
    protected List<RuntimeNode> children = new ArrayList<RuntimeNode>();

    protected RuntimeNode() {
      timeStarted = 0;
      timeStopped = Long.MAX_VALUE;
    }

    private RuntimeNode(final RuntimeMeasurement runtimeMeasurement) {
      className = runtimeMeasurement.getClassName();
      methodName = runtimeMeasurement.getMethodName();
      timeStarted = runtimeMeasurement.getTimeStarted();
      timeStopped = runtimeMeasurement.getTimeStopped();
    }

    protected boolean add(final RuntimeMeasurement runtimeMeasurement) {
      if (timeStarted <= runtimeMeasurement.getTimeStarted()
          && timeStopped != 0 && timeStopped >= runtimeMeasurement.getTimeStopped()) {
        for (RuntimeNode candidate : children)
          if (candidate.add(runtimeMeasurement))
            return true;
        children.add(new RuntimeNode(runtimeMeasurement));
        return true;
      } else {
        return false;
      }
    }

    /**
     * Combines runtime measurements with identical class names and method
     * names into one measurement, assuming that they originate from a loop
     * or a similar construct where a summary measurement has been intended.
     */
    protected void combineRuntimeMeasurements() {
      RuntimeNode preceding = null;
      for (Iterator<RuntimeNode> iterator = children.iterator(); iterator.hasNext();) {
        final RuntimeNode child = iterator.next();
        if (preceding != null
            && preceding.timeStopped != 0 && child.timeStopped != 0
            && preceding.timeStopped <= child.timeStarted
            && preceding.children.isEmpty() && child.children.isEmpty()
            && preceding.methodName.equals(child.methodName)
            && preceding.className.equals(child.className)) {
          preceding.timeStarted = child.timeStarted - (preceding.timeStopped - preceding.timeStarted);
          preceding.timeStopped = child.timeStopped;
          iterator.remove();
        } else {
          preceding = child;
          child.combineRuntimeMeasurements();
        }
      }
    }
  }

  private final RuntimeNode rootNode;

  public DebugInfoRuntime(final List<RuntimeMeasurement> runtimeMeasurements) {
    rootNode = new RuntimeNode();
    for (final RuntimeMeasurement runtimeMeasurement : runtimeMeasurements)
      rootNode.add(runtimeMeasurement);
    rootNode.combineRuntimeMeasurements();
  }

  @Override
  public String getName() {
    return "Runtime";
  }

  @Override
  public void appendJson(JsonStreamWriter jsonStreamWriter) throws IOException {
    appendJsonChildren(jsonStreamWriter, rootNode);
  }

  private static void appendJsonNode(JsonStreamWriter jsonStreamWriter, final RuntimeNode node) throws IOException {
    jsonStreamWriter.beginObject()
        .namedStringValueRaw("class", node.className).separator()
        .namedStringValueRaw("method", node.methodName).separator()
        .name("duration")
        .unquotedValue(node.timeStopped == 0 ? null :
            Long.toString((node.timeStopped - node.timeStarted) / 1000))
        .separator()
        .name("children");
    appendJsonChildren(jsonStreamWriter, node);
    jsonStreamWriter.endObject();
  }

  private static void appendJsonChildren(final JsonStreamWriter jsonStreamWriter, final RuntimeNode node) throws IOException {
    jsonStreamWriter.beginArray();
    boolean first = true;
    for (final RuntimeNode childNode : node.children) {
      if (!first)
        jsonStreamWriter.separator();
      first = false;
      appendJsonNode(jsonStreamWriter, childNode);
    }
    jsonStreamWriter.endArray();
  }
}
