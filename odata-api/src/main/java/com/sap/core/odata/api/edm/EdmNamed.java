package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmName is the base interface for nearly all CSDL constructs.
 * @author SAP AG
 */
public interface EdmNamed {

  /** 
   * @return name as String
   * @throws EdmException
   */
  String getName() throws EdmException;
}