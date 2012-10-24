package com.sap.core.odata.api.edm;

/**
 * EdmName is the base interface for nearly all CSDL constructs.
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmNamed {

  /**
   * Get the name
   * 
   * @return name as String
   * @throws EdmException
   */
  String getName() throws EdmException;
}