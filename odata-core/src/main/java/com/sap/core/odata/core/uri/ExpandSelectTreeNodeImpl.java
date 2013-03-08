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
      ExpandSelectTreeNodeImpl existingNode = (ExpandSelectTreeNodeImpl) links.get(navigationProperty);
      //If the existing node and the new node differ we can not just return the existing one
      //Thus we merge the new child into the existing one
      if (existingNode != childNode && childNode != null) {
        if (existingNode.isAll() || childNode.isAll()) {
          existingNode.setAllExplicitly();
          for (Map.Entry<EdmNavigationProperty, ExpandSelectTreeNode> entry : childNode.getLinks().entrySet()) {
            if (entry.getValue() != null) {
              existingNode.addChild(entry.getKey(), entry.getValue());
            }
          }
        } else {
          for (EdmProperty property : childNode.getProperties()) {
            existingNode.addProperty(property);
          }
          for (Map.Entry<EdmNavigationProperty, ExpandSelectTreeNode> entry : childNode.getLinks().entrySet()) {
            existingNode.addChild(entry.getKey(), entry.getValue());
          }
        }
      }

      return existingNode;
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
    if (isAll == null) {
      return true;
    }
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

  public String toJsonString() throws EdmException {
    String propertiesString = "";
    String linksString = "";

    if (properties.isEmpty() == false) {
      for (EdmProperty property : properties) {
        propertiesString = propertiesString + "\"" + property.getName() + "\",";
      }
      propertiesString = propertiesString.substring(0, propertiesString.length() - 1);
    }

    if (links.isEmpty() == false) {
      for (Map.Entry<EdmNavigationProperty, ExpandSelectTreeNode> entry : links.entrySet()) {
        String nodeString;
        if (entry.getValue() == null) {
          nodeString = null;
        } else {
          nodeString = ((ExpandSelectTreeNodeImpl) entry.getValue()).toJsonString();
        }
        linksString = linksString + "{\"" + entry.getKey().getName() + "\":" + nodeString + "},";
      }
      linksString = linksString.substring(0, linksString.length() - 1);
    }

    return "{\"all\":" + isAll() + ",\"properties\":[" + propertiesString + "],\"links\":[" + linksString + "]}";
  }

}
