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
   * Get the mime type (of Media Resource or an EdmBinary)
   * 
   * @return mime type as String
   */
  String getMimeType();
  
  /**
   * Get the object this mapping is set to
   * 
   * @return {@link Object} mapped object
   */
  Object getObject();
}
