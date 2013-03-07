package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;

public class ExpandSelectTreeNodeImpl implements ExpandSelectTreeNode {

  private Boolean isAll;
  private final List<EdmProperty> properties = new ArrayList<EdmProperty>();
  private final HashMap<EdmNavigationProperty, ExpandSelectTreeNode> links = new HashMap<EdmNavigationProperty, ExpandSelectTreeNode>();

  public void addProperty(final EdmProperty property) {
    if (isAll == null) {
      isAll = false;
    }

    if (!isAll && !properties.contains(property)) {
      properties.add(property);
    }
  }

  public ExpandSelectTreeNode addChild(final EdmNavigationProperty navigationProperty, final ExpandSelectTreeNode childNode) {
    if (isAll == null) {
      isAll = false;
    }

    if (links.containsKey(navigationProperty)) {
      return links.get(navigationProperty);
    }

    if (isAll && childNode == null) {
      return null;
    }

    links.put(navigationProperty, childNode);
    return childNode;

  }

  public void setAllExplicitly() {
    isAll = true;
    properties.clear();

    //Remove all selected navigation properties which are not mentioned in the expand 
    Iterator<EdmNavigationProperty> iterator = links.keySet().iterator();
    while (iterator.hasNext()) {
      EdmNavigationProperty navProp = iterator.next();
      if (links.get(navProp) == null) {
        iterator.remove();
      }
    }
  }

  @Override
  public boolean isAll() {
    return isAll;
  }

  @Override
  public List<EdmProperty> getProperties() {
    return properties;
  }

  @Override
  public HashMap<EdmNavigationProperty, ExpandSelectTreeNode> getLinks() {
    return links;
  }

  @Override
  public String toString() {
    String propertiesString = "";
    String linksString = "";
    String isAllString = "";

    if (properties.isEmpty() == false) {
      for (EdmProperty property : properties) {
        try {
          propertiesString = propertiesString + "\"" + property.getName() + "\",";
        } catch (EdmException e) {
          //TODO: What here
        }
      }
      propertiesString = propertiesString.substring(0, propertiesString.length() - 1);
    }

    if (links.isEmpty() == false) {
      for (Map.Entry<EdmNavigationProperty, ExpandSelectTreeNode> entry : links.entrySet()) {
        String nodeString;
        if (entry.getValue() == null) {
          nodeString = null;
        } else {
          nodeString = entry.getValue().toString();
        }
        try {
          linksString = linksString + "{\"" + entry.getKey().getName() + "\":" + nodeString + "},";
        } catch (EdmException e) {
          //TODO: What here
        }
      }
      linksString = linksString.substring(0, linksString.length() - 1);
    }

    if (isAll == null) {
      isAllString = "true";
    } else {
      isAllString = isAll.toString();
    }

    //{"all":true,"properties":[],"links":[]}
    return "{\"all\":" + isAllString + ",\"properties\":[" + propertiesString + "],\"links\":[" + linksString + "]}";
  }

}
