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

/**
 * Expression tree node with information about selected properties and to be expanded links.
 * @author SAP AG
 */
public class ExpandSelectTreeNodeImpl implements ExpandSelectTreeNode {

  public enum AllKinds {
    IMPLICITLYTRUE(true), EXPLICITLYTRUE(true), FALSE(false);

    private boolean booleanRepresentation;

    private AllKinds(boolean booleanRepresentation) {
      this.booleanRepresentation = booleanRepresentation;
    }

    public boolean getBoolean() {
      return booleanRepresentation;

    }
  }

  private AllKinds isAll = AllKinds.IMPLICITLYTRUE;
  private final List<EdmProperty> properties = new ArrayList<EdmProperty>();
  private final HashMap<EdmNavigationProperty, ExpandSelectTreeNode> links = new HashMap<EdmNavigationProperty, ExpandSelectTreeNode>();

  public void addProperty(final EdmProperty property) {
    if (isAll == AllKinds.IMPLICITLYTRUE) {
      isAll = AllKinds.FALSE;
    }

    if (isAll != AllKinds.EXPLICITLYTRUE && !properties.contains(property)) {
      properties.add(property);
    }
  }

  private AllKinds isAllInternal() {
    return isAll;
  }

  public ExpandSelectTreeNode addChild(final EdmNavigationProperty navigationProperty, final ExpandSelectTreeNode childNode) {
    if (isAll == AllKinds.IMPLICITLYTRUE) {
      isAll = AllKinds.FALSE;
    }

    if (links.containsKey(navigationProperty)) {
      ExpandSelectTreeNodeImpl existingNode = (ExpandSelectTreeNodeImpl) links.get(navigationProperty);
      ExpandSelectTreeNodeImpl childNodeInternal = (ExpandSelectTreeNodeImpl) childNode;
      //If the existing node and the new node differ we can not just return the existing one
      //Thus we merge the new child into the existing one
      if (existingNode != childNode && childNode != null) {
        if (existingNode.isAllInternal() == AllKinds.EXPLICITLYTRUE || childNodeInternal.isAllInternal() == AllKinds.EXPLICITLYTRUE) {
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

    if (isAll != AllKinds.FALSE && childNode == null) {
      return null;
    }

    links.put(navigationProperty, childNode);
    return childNode;

  }

  public void setAllExplicitly() {
    isAll = AllKinds.EXPLICITLYTRUE;
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
    return isAll.getBoolean();
  }

  @Override
  public List<EdmProperty> getProperties() {
    return properties;
  }

  @Override
  public Map<EdmNavigationProperty, ExpandSelectTreeNode> getLinks() {
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
  
  @Override
  public String toString(){
   
    try {
      return toJsonString();
    } catch (EdmException e) {
     return "Edm Exception occoured";
    }
  }

}
