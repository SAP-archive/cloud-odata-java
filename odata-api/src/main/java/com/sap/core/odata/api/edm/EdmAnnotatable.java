package com.sap.core.odata.api.edm;

/**
 * EdmAnnotatable can be applied to CSDL elements as described in the Conceptual Schema Definition Language
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmAnnotatable {

  /**
   * Get all annotations applied to an EDM element
   * 
   * @return {@link EdmAnnotations}
   * @throws EdmException
   */
  EdmAnnotations getAnnotations() throws EdmException;
}