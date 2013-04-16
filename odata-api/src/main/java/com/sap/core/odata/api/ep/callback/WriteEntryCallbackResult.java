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
