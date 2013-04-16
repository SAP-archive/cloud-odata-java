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
package com.sap.core.odata.api.ep.callback;

import java.util.Map;

import com.sap.core.odata.api.ep.EntityProviderWriteProperties;

/**
 * Result of a callback. It contains the data of the entry which is to be expanded as well as the properties of this entry.
 * @author SAP AG
 */
public class WriteEntryCallbackResult {

  EntityProviderWriteProperties inlineProperties;
  Map<String, Object> oneEntryData;

  /**
   * @return the inline properties
   */
  public EntityProviderWriteProperties getInlineProperties() {
    return inlineProperties;
  }

  /**
   * Sets the inline properties for this entry
   * @param inlineProperties
   */
  public void setInlineProperties(final EntityProviderWriteProperties inlineProperties) {
    this.inlineProperties = inlineProperties;
  }

  /**
   * @return the data for the entry as a map
   */
  public Map<String, Object> getEntryData() {
    return oneEntryData;
  }

  /**
   * @param data for the entry as a map
   */
  public void setEntryData(final Map<String, Object> data) {
    oneEntryData = data;
  }
}
