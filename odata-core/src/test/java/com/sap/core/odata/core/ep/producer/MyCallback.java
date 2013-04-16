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
package com.sap.core.odata.core.ep.producer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.OnWriteEntryContent;
import com.sap.core.odata.api.ep.callback.OnWriteFeedContent;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class MyCallback implements ODataCallback, OnWriteEntryContent, OnWriteFeedContent {

  private AbstractProviderTest dataProvider;
  private URI baseUri;
  private URI roomToEmployee;

  public MyCallback(final AbstractProviderTest dataProvider, final URI baseUri) {
    this.dataProvider = dataProvider;
    this.baseUri = baseUri;
    try {
      roomToEmployee = new URI("Rooms('1')/nr_Employees");
    } catch (URISyntaxException e) {
      throw new ODataRuntimeException(e);
    }
  }

  @Override
  public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) {
    WriteFeedCallbackResult result = new WriteFeedCallbackResult();
    try {
      if ("Rooms".equals(context.getSourceEntitySet().getName())) {
        if ("nr_Employees".equals(context.getNavigationProperty().getName())) {
          HashMap<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
          for (String navPropName : context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType().getNavigationPropertyNames()) {
            callbacks.put(navPropName, this);
          }
          EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(baseUri).callbacks(callbacks).expandSelectTree(context.getCurrentExpandSelectTreeNode()).selfLink(roomToEmployee).build();

          result.setFeedData(dataProvider.getEmployeesData());
          result.setInlineProperties(inlineProperties);
        }
      }
    } catch (EdmException e) {
      throw new ODataRuntimeException("EdmException", e);
    }
    return result;
  }

  @Override
  public WriteEntryCallbackResult retrieveEntryResult(final WriteEntryCallbackContext context) {
    WriteEntryCallbackResult result = new WriteEntryCallbackResult();
    try {
      if ("Employees".equals(context.getSourceEntitySet().getName())) {
        if ("ne_Room".equals(context.getNavigationProperty().getName())) {
          HashMap<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
          for (String navPropName : context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType().getNavigationPropertyNames()) {
            callbacks.put(navPropName, this);
          }
          EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(baseUri).callbacks(callbacks).expandSelectTree(context.getCurrentExpandSelectTreeNode()).build();
          result.setEntryData(dataProvider.getRoomData());
          result.setInlineProperties(inlineProperties);
        } else if ("ne_Team".equals(context.getNavigationProperty().getName())) {
          result.setEntryData(null);
        }
      }
    } catch (EdmException e) {
      throw new ODataRuntimeException("EdmException:", e);
    }
    return result;
  }

}
