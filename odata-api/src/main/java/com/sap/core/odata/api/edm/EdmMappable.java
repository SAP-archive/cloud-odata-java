package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmMappable can be applied to CSDL elements to associate additional information.
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