package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmMapping holds custom mapping information which can be applied to a CSDL element.
 * @author SAP AG
 */
public interface EdmMapping {

  /**
   * Get the mapping value
   * 
   * @return mapping name as String
   */
  String getInternalName();

  /**
   * Get the mapping name for mime type lookup
   * 
   * @return mapping name as String
   */
  String getMimeType();

  /**
   * Get the set object for this mapping
   * 
   * @return {@link Object} object
   */
  Object getObject();
}
