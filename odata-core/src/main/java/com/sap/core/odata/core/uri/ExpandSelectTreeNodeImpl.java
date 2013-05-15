package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
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
  private final Map<String, ExpandSelectTreeNode> links = new HashMap<String, ExpandSelectTreeNode>();

  @Override
  public boolean isAll() {
    return isAll.getBoolean();
  }

  @Override
  public List<EdmProperty> getProperties() {
    return properties;
  }

  @Override
  public Map<String, ExpandSelectTreeNode> getLinks() {
    return Collections.unmodifiableMap(links);
  }

  public void putLink(final String name, final ExpandSelectTreeNode node) {
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
    String propertiesString = "";
    String linksString = "";

    try {
      for (EdmProperty property : properties) {
        if (!propertiesString.isEmpty()) {
          propertiesString += ",";
        }
        propertiesString += "\"" + property.getName() + "\"";
      }

      for (Map.Entry<String, ExpandSelectTreeNode> entry : links.entrySet()) {
        final String nodeString = entry.getValue() == null ? null : ((ExpandSelectTreeNodeImpl) entry.getValue()).toJsonString();
        if (!linksString.isEmpty()) {
          linksString += ",";
        }
        linksString += "{\"" + entry.getKey() + "\":" + nodeString + "}";
      }

      return "{\"all\":" + isAll() + ",\"properties\":[" + propertiesString + "],\"links\":[" + linksString + "]}";
    } catch (EdmException e) {
      throw new ODataRuntimeException("EdmException: ", e);
    }
  }
}
