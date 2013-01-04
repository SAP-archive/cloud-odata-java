package com.sap.core.odata.api.edm;

/**
 * EdmMapping holds custom mapping information which can be applied to a CSDL element.
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 * 
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
   * Get the data container for this mapping
   * 
   * @return {@link Object} data container
   */
  Object getDataContainer();
}
