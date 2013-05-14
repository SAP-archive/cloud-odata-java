package com.sap.core.odata.api;

/**
 * @author SAP AG
 *
 */
public interface ODataDebugCallback extends ODataCallback {

  /**
   * @return true if the system is in debug mode and the current user has the rights to debug the odata service
   */
  boolean isDebugEnabled();

}
