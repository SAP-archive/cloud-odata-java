package com.sap.core.odata.api.edm;

/**
 * A CSDL Property element
 * 
 * EdmProperty defines a simple type or a complex type.
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmProperty extends EdmElement {

  /**
   * Get customizable feed mappings for this property
   * 
   * @return {@link EdmCustomizableFeedMappings}
   * @throws EdmException
   */
  EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException;

  /**
   * Get the related mime type for the property
   * 
   * @return mime type as String
   * @throws EdmException
   */
  String getMimeType() throws EdmException;
}