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

  private class RuntimeTree {
    private RuntimeMeasurement runtimeMeasurement;
    private RuntimeTree parent;
  }

  private final List<RuntimeTree> tree;

  public DebugInfoRuntime(final List<RuntimeMeasurement> runtimeMeasurements) {
    combineRuntimeMeasurements(runtimeMeasurements);
    tree = createRuntimeTree(runtimeMeasurements);
  }

  /**
   * Combines runtime measurements with identical class names and method
   * names into one measurement, assuming that they originate from a loop
   * or a similar construct where a summary measurement has been intended.
   */
  private static void combineRuntimeMeasurements(final List<RuntimeMeasurement> runtimeMeasurements) {
    RuntimeMeasurement preceding = null;
    for (Iterator<RuntimeMeasurement> iterator = runtimeMeasurements.iterator(); iterator.hasNext();) {
      final RuntimeMeasurement runtimeMeasurement = iterator.next();
      if (preceding != null
          && preceding.getTimeStopped() > 0 && runtimeMeasurement.getTimeStopped() > 0
          && preceding.getTimeStopped() <= runtimeMeasurement.getTimeStarted()
          && preceding.getMethodName().equals(runtimeMeasurement.getMethodName())
          && preceding.getClassName().equals(runtimeMeasurement.getClassName())
          && getParent(preceding, runtimeMeasurements) == getParent(runtimeMeasurement, runtimeMeasurements)) {
        preceding.setTimeStarted(preceding.getTimeStarted()
            + runtimeMeasurement.getTimeStarted() - preceding.getTimeStopped());
        preceding.setTimeStopped(runtimeMeasurement.getTimeStopped());
        iterator.remove();
      } else {
        preceding = runtimeMeasurement;
      }
    }
  }

  private static RuntimeMeasurement getParent(final RuntimeMeasurement runtimeMeasurement, final List<RuntimeMeasurement> runtimeMeasurements) {
    RuntimeMeasurement result = null;
    for (final RuntimeMeasurement candidate : runtimeMeasurements) {
      if (runtimeMeasurement != candidate
          && runtimeMeasurement.getTimeStarted() >= candidate.getTimeStarted()
          && runtimeMeasurement.getTimeStopped() <= candidate.getTimeStopped()) {
        result = candidate;
      }
    }
    return result;
  }

  private List<RuntimeTree> createRuntimeTree(final List<RuntimeMeasurement> runtimeMeasurements) {
    List<RuntimeTree> tree = new ArrayList<RuntimeTree>();
    RuntimeTree previous = null;
    for (final RuntimeMeasurement runtimeMeasurement : runtimeMeasurements) {
      RuntimeTree treeNode = new RuntimeTree();
      treeNode.runtimeMeasurement = runtimeMeasurement;
      while (previous != null
          && previous.runtimeMeasurement.getTimeStopped() > 0
          && previous.runtimeMeasurement.getTimeStopped() <= runtimeMeasurement.getTimeStarted()) {
        previous = previous.parent;
      }
      treeNode.parent = previous;
      tree.add(treeNode);
      previous = treeNode;
    }
    return tree;
  }

  @Override
  public String getName() {
    return "Runtime";
  }

  @Override
  public void appendJson(final JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginArray();
    for (final RuntimeTree runtimeTree : tree) {
      if (runtimeTree.parent == null) {
        appendJsonTreeNode(jsonStreamWriter, runtimeTree);
      }
    }
    jsonStreamWriter.endArray();
  }

  private void appendJsonTreeNode(final JsonStreamWriter jsonStreamWriter, final RuntimeTree node) throws IOException {
    jsonStreamWriter.beginObject();
    jsonStreamWriter.namedStringValueRaw("class", node.runtimeMeasurement.getClassName());
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValueRaw("method", node.runtimeMeasurement.getMethodName());
    jsonStreamWriter.separator();
    jsonStreamWriter.name("duration");
    jsonStreamWriter.unquotedValue(node.runtimeMeasurement.getTimeStopped() == 0 ? null :
        Long.toString((node.runtimeMeasurement.getTimeStopped() - node.runtimeMeasurement.getTimeStarted()) / 1000));
    jsonStreamWriter.separator();
    jsonStreamWriter.name("children");
    jsonStreamWriter.beginArray();
    boolean first = true;
    for (final RuntimeTree childNode : tree) {
      if (childNode.parent != null
          && childNode.parent.runtimeMeasurement.getTimeStarted() == node.runtimeMeasurement.getTimeStarted()) {
        if (!first) {
          jsonStreamWriter.separator();
        }
        first = false;
        appendJsonTreeNode(jsonStreamWriter, childNode);
      }
    }
    jsonStreamWriter.endArray();
    jsonStreamWriter.endObject();
  }
}
