/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
    String propertiesString = "";
    String linksString = "";

    try {
      for (EdmProperty property : properties) {
        if (!propertiesString.isEmpty()) {
          propertiesString += ",";
        }
        propertiesString += "\"" + property.getName() + "\"";
      }

      for (Map.Entry<String, ExpandSelectTreeNodeImpl> entry : links.entrySet()) {
        final String nodeString = entry.getValue() == null ? null : entry.getValue().toJsonString();
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
