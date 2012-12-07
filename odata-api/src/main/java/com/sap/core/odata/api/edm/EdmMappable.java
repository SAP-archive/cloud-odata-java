package com.sap.core.odata.api.edm;

/**
 * EdmMappable can be applied to CSDL elements to associate additional information.
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmMappable {

  /**
   * Get mapping information applied to an EDM element
   * 
   * @return {@link EdmMapping}
   * @throws EdmException
   */
  EdmMapping getMapping() throws EdmException;
}