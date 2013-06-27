package com.sap.core.odata.core.uri;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * @author SAP AG
 */
public class ExpandSelectTreeNodeImpl implements ExpandSelectTreeNode {

  public enum AllKinds {
    IMPLICITLYTRUE(true), EXPLICITLYTRUE(true), FALSE(false);

    private boolean booleanRepresentation;

    private AllKinds(final boolean booleanRepresentation) {
      this.booleanRepresentation = booleanRepresentation;
    }

    public boolean getBoolean() {
      return booleanRepresentation;
    }
  }

  private AllKinds isAll = AllKinds.IMPLICITLYTRUE;
  private boolean isExplicitlySelected = false;
  private boolean isExpanded = false;
  private final List<EdmProperty> properties = new ArrayList<EdmProperty>();
  private final Map<String, ExpandSelectTreeNodeImpl> links = new HashMap<String, ExpandSelectTreeNodeImpl>();

  @Override
  public boolean isAll() {
    return isAll.getBoolean();
  }

  @Override
  public List<EdmProperty> getProperties() {
    return properties;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, ExpandSelectTreeNode> getLinks() {
    return (Map<String, ExpandSelectTreeNode>) ((Map<String, ? extends ExpandSelectTreeNode>) Collections.unmodifiableMap(links));
  }

  public void putLink(final String name, final ExpandSelectTreeNodeImpl node) {
    links.put(name, node);
  }

  public void removeLink(final String name) {
    links.remove(name);
  }

  public boolean isExplicitlySelected() {
    return isExplicitlySelected;
  }

  public void setExplicitlySelected() {
    isExplicitlySelected = true;
    setAllExplicitly();
  }

  public boolean isExpanded() {
    return isExpanded;
  }

  public void setExpanded() {
    isExpanded = true;
  }

  public void addProperty(final EdmProperty property) {
    if (property != null && isAll != AllKinds.EXPLICITLYTRUE && !properties.contains(property)) {
      properties.add(property);
      isAll = AllKinds.FALSE;
    }
  }

  public void setAllExplicitly() {
    properties.clear();
    isAll = AllKinds.EXPLICITLYTRUE;
  }

  public AllKinds getAllKind() {
    return isAll;
  }

  public void setAllKindFalse() {
    isAll = AllKinds.FALSE;
  }

  public String toJsonString() {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject()
          .name("all").unquotedValue(Boolean.toString(isAll())).separator()
          .name("properties")
          .beginArray();
      boolean first = true;
      for (EdmProperty property : properties) {
        if (first)
          first = false;
        else
          jsonStreamWriter.separator();
        jsonStreamWriter.stringValueRaw(property.getName());
      }
      jsonStreamWriter.endArray().separator()
          .name("links")
          .beginArray();
      first = true;
      for (Map.Entry<String, ExpandSelectTreeNodeImpl> entry : links.entrySet()) {
        if (first)
          first = false;
        else
          jsonStreamWriter.separator();
        final String nodeString = entry.getValue() == null ? null : entry.getValue().toJsonString();
        jsonStreamWriter.beginObject()
            .name(entry.getKey()).unquotedValue(nodeString)
            .endObject();
      }
      jsonStreamWriter.endArray()
          .endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      throw new ODataRuntimeException("IOException: ", e);
    } catch (final EdmException e) {
      throw new ODataRuntimeException("EdmException: ", e);
    }
  }
}
