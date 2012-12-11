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
   * @return mapping value as String
   */
  String getValue();
  
  /**
   * Get the mime type (of Media Resource or an EdmBinary)
   * 
   * @return mime type as String
   */
  String getMimeType();
}
