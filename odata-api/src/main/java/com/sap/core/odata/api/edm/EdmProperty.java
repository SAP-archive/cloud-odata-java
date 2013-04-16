package com.sap.core.odata.api.edm;

/**
 * A CSDL Property element
 * <p>EdmProperty defines a simple type or a complex type.
 * @com.sap.core.odata.DoNotImplement
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

  /**
   * Get the info if the property is a simple property
   * 
   * @return true, if it is a simple property
   */
  boolean isSimple();
}