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
package com.sap.core.odata.processor.core.jpa.access.data;

import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.cud.SalesOrderLineItem;

public class TestUtil {

  public static ExpandSelectTreeNode mockExpandSelectTreeNode() {
    ExpandSelectTreeNode nextExpandNode = EasyMock
        .createMock(ExpandSelectTreeNode.class);
    Map<String, ExpandSelectTreeNode> nextLink = null;
    EasyMock.expect(nextExpandNode.getLinks()).andStubReturn(nextLink);
    EasyMock.replay(nextExpandNode);
    ExpandSelectTreeNode expandNode = EasyMock
        .createMock(ExpandSelectTreeNode.class);
    Map<String, ExpandSelectTreeNode> links = new HashMap<String, ExpandSelectTreeNode>();
    links.put("SalesOrderLineItemDetails", nextExpandNode);
    EasyMock.expect(expandNode.getLinks()).andStubReturn(links);
    EasyMock.replay(expandNode);
    return expandNode;
  }

  public static ExpandSelectTreeNode mockCurrentExpandSelectTreeNode() {
    ExpandSelectTreeNode expandNode = EasyMock
        .createMock(ExpandSelectTreeNode.class);
    Map<String, ExpandSelectTreeNode> links = new HashMap<String, ExpandSelectTreeNode>();
    EasyMock.expect(expandNode.getLinks()).andStubReturn(links);
    EasyMock.replay(expandNode);
    return expandNode;
  }

  public static List<ArrayList<NavigationPropertySegment>> getExpandList() {
    List<ArrayList<NavigationPropertySegment>> expandList = new ArrayList<ArrayList<NavigationPropertySegment>>();
    ArrayList<NavigationPropertySegment> expands = new ArrayList<NavigationPropertySegment>();
    expands.add(mockNavigationPropertySegment());
    expandList.add(expands);
    return expandList;
  }

  public static WriteFeedCallbackContext getWriteFeedCallBackContext() {
    URI selfLink = null;
    WriteFeedCallbackContext writeContext = new WriteFeedCallbackContext();
    try {
      selfLink = new URI("SalesOrders(2L)/SalesOrderLineItemDetails");
      writeContext.setSelfLink(selfLink);
      writeContext
          .setCurrentExpandSelectTreeNode(mockCurrentExpandSelectTreeNode());
      writeContext.setNavigationProperty(mockNavigationProperty());
      writeContext.setSourceEntitySet(mockSourceEntitySet());
      writeContext.setEntryData(getFeedData());

    } catch (URISyntaxException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

    return writeContext;
  }

  public static WriteEntryCallbackContext getWriteEntryCallBackContext() {
    WriteEntryCallbackContext writeContext = new WriteEntryCallbackContext();
    writeContext
        .setCurrentExpandSelectTreeNode(mockCurrentExpandSelectTreeNode());
    writeContext.setNavigationProperty(mockNavigationProperty());
    writeContext.setSourceEntitySet(mockSourceEntitySet());
    writeContext.setEntryData(getEntryData());
    return writeContext;
  }

  private static EdmEntitySet mockSourceEntitySet() {
    EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
    try {
      EasyMock.expect(entitySet.getEntityType()).andStubReturn(
          mockSourceEdmEntityType());
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(entitySet);
    return entitySet;
  }

  public static EdmEntityType mockSourceEdmEntityType() {
    EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
    List<String> navigationPropertyNames = new ArrayList<String>();
    List<String> propertyNames = new ArrayList<String>();
    propertyNames.add("id");
    propertyNames.add("description");
    navigationPropertyNames.add("SalesOrderLineItemDetails");
    try {
      EasyMock.expect(mapping.getInternalName()).andStubReturn(
          "SalesOrderHeader");
      EasyMock.replay(mapping);
      EasyMock.expect(entityType.getName()).andStubReturn(
          "SalesOrderHeader");
      EasyMock.expect(entityType.getMapping()).andStubReturn(mapping);
      EasyMock.expect(entityType.getNavigationPropertyNames())
          .andStubReturn(navigationPropertyNames);
      EasyMock.expect(entityType.getProperty("SalesOrderLineItemDetails"))
          .andStubReturn(mockNavigationProperty());
      EdmProperty property1 = mockEdmPropertyOfSource1();
      EasyMock.expect(entityType.getProperty("id")).andStubReturn(
          property1);
      EasyMock.expect(entityType.getProperty("description"))
          .andStubReturn(mockEdmPropertyOfSource2());
      EasyMock.expect(entityType.getPropertyNames()).andStubReturn(
          propertyNames);

    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(entityType);
    return entityType;
  }

  private static EdmTyped mockEdmPropertyOfSource2() {
    EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
    EdmType type = EasyMock.createMock(EdmType.class);
    EasyMock.expect(type.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
    EasyMock.replay(type);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(mapping.getInternalName()).andStubReturn("description");
    EasyMock.replay(mapping);
    try {
      EasyMock.expect(edmProperty.getName()).andStubReturn("description");
      EasyMock.expect(edmProperty.getType()).andStubReturn(type);
      EasyMock.expect(edmProperty.getMapping()).andStubReturn(mapping);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(edmProperty);
    return edmProperty;
  }

  private static EdmProperty mockEdmPropertyOfSource1() {
    EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
    EdmType type = EasyMock.createMock(EdmType.class);
    EasyMock.expect(type.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
    EasyMock.replay(type);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(mapping.getInternalName()).andStubReturn("id");
    EasyMock.replay(mapping);
    try {
      EasyMock.expect(edmProperty.getName()).andStubReturn("id");
      EasyMock.expect(edmProperty.getType()).andStubReturn(type);
      EasyMock.expect(edmProperty.getMapping()).andStubReturn(mapping);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(edmProperty);
    return edmProperty;
  }

  private static Map<String, Object> getFeedData() {
    Map<String, Object> entryData = new HashMap<String, Object>();
    entryData.put("id", 1);
    entryData.put("description", "laptop");
    List<SalesOrderLineItem> salesOrderLineItems = new ArrayList<SalesOrderLineItem>();
    salesOrderLineItems.add(new SalesOrderLineItem(23));
    salesOrderLineItems.add(new SalesOrderLineItem(45));
    entryData.put("SalesOrderLineItemDetails", salesOrderLineItems);
    return entryData;
  }

  private static Map<String, Object> getEntryData() {
    Map<String, Object> entryData = new HashMap<String, Object>();
    entryData.put("id", 1);
    entryData.put("description", "laptop");
    entryData.put("SalesOrderLineItemDetails", new SalesOrderLineItem(23));
    return entryData;
  }

  private static NavigationPropertySegment mockNavigationPropertySegment() {
    NavigationPropertySegment navigationPropSegment = EasyMock
        .createMock(NavigationPropertySegment.class);
    EasyMock.expect(navigationPropSegment.getNavigationProperty())
        .andStubReturn(mockNavigationProperty());
    EasyMock.expect(navigationPropSegment.getTargetEntitySet())
        .andStubReturn(mockTargetEntitySet());
    EasyMock.replay(navigationPropSegment);
    return navigationPropSegment;
  }

  public static NavigationPropertySegment mockThirdNavigationPropertySegment()
  {
    NavigationPropertySegment navigationPropSegment = EasyMock
        .createMock(NavigationPropertySegment.class);
    EasyMock.expect(navigationPropSegment.getNavigationProperty())
        .andStubReturn(mockSecondNavigationProperty());
    EasyMock.expect(navigationPropSegment.getTargetEntitySet())
        .andStubReturn(mockThirdEntitySet());
    EasyMock.replay(navigationPropSegment);
    return navigationPropSegment;
  }

  public static EdmNavigationProperty mockSecondNavigationProperty() {
    EdmNavigationProperty navigationProperty = EasyMock
        .createMock(EdmNavigationProperty.class);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(mapping.getInternalName()).andStubReturn(
        "materials");
    EasyMock.replay(mapping);
    try {
      EasyMock.expect(navigationProperty.getMultiplicity())
          .andStubReturn(EdmMultiplicity.ONE);
      EasyMock.expect(navigationProperty.getMapping()).andStubReturn(
          mapping);
      EasyMock.expect(navigationProperty.getName()).andStubReturn(
          "MaterialDetails");
      EasyMock.expect(navigationProperty.getFromRole()).andStubReturn("SalesOrderLineItem");
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(navigationProperty);
    return navigationProperty;
  }

  public static EdmEntitySet mockTargetEntitySet() {
    EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
    try {
      EasyMock.expect(entitySet.getEntityType()).andStubReturn(
          mockTargetEdmEntityType());
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(entitySet);
    return entitySet;
  }

  public static EdmEntitySet mockThirdEntitySet() {
    EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
    try {
      EasyMock.expect(entitySet.getEntityType()).andStubReturn(
          mockThirdEdmEntityType());
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(entitySet);
    return entitySet;

  }

  private static EdmEntityType mockThirdEdmEntityType() {
    EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);

    List<String> propertyNames = new ArrayList<String>();
    propertyNames.add("price");
    try {
      EasyMock.expect(mapping.getInternalName()).andStubReturn(
          "Material");
      EasyMock.replay(mapping);
      EasyMock.expect(entityType.getName()).andStubReturn(
          "Material");
      EasyMock.expect(entityType.getMapping()).andStubReturn(mapping);
      EdmProperty property = mockEdmPropertyOfTarget();
      EasyMock.expect(entityType.getProperty("price")).andStubReturn(
          property);
      EasyMock.expect(entityType.getPropertyNames()).andStubReturn(
          propertyNames);

    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(entityType);
    return entityType;
  }

  public static EdmEntityType mockTargetEdmEntityType() {
    EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);

    List<String> propertyNames = new ArrayList<String>();
    propertyNames.add("price");
    try {
      EasyMock.expect(mapping.getInternalName()).andStubReturn(
          "SalesOrderLineItem");
      EasyMock.replay(mapping);
      EasyMock.expect(entityType.getName()).andStubReturn(
          "SalesOrderLineItem");
      EasyMock.expect(entityType.getMapping()).andStubReturn(mapping);
      EdmProperty property = mockEdmPropertyOfTarget();
      EasyMock.expect(entityType.getProperty("price")).andStubReturn(
          property);
      EasyMock.expect(entityType.getPropertyNames()).andStubReturn(
          propertyNames);

    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(entityType);
    return entityType;
  }

  private static EdmProperty mockEdmPropertyOfTarget() {
    EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);

    EdmType type = EasyMock.createMock(EdmType.class);
    EasyMock.expect(type.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
    EasyMock.replay(type);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(mapping.getInternalName()).andStubReturn("price");
    EasyMock.replay(mapping);
    try {
      EasyMock.expect(edmProperty.getName()).andStubReturn("price");
      EasyMock.expect(edmProperty.getType()).andStubReturn(type);
      EasyMock.expect(edmProperty.getMapping()).andStubReturn(mapping);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(edmProperty);
    return edmProperty;
  }

  public static EdmNavigationProperty mockNavigationProperty() {
    EdmNavigationProperty navigationProperty = EasyMock
        .createMock(EdmNavigationProperty.class);
    EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(mapping.getInternalName()).andStubReturn(
        "salesOrderLineItems");
    EasyMock.replay(mapping);
    try {
      EasyMock.expect(navigationProperty.getMultiplicity())
          .andStubReturn(EdmMultiplicity.MANY);
      EasyMock.expect(navigationProperty.getMapping()).andStubReturn(
          mapping);
      EasyMock.expect(navigationProperty.getName()).andStubReturn(
          "SalesOrderLineItemDetails");
      EasyMock.expect(navigationProperty.getFromRole()).andStubReturn("SalesOrderHeader");
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(navigationProperty);
    return navigationProperty;
  }

}
