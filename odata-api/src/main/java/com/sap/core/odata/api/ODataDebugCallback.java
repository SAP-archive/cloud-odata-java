package com.sap.core.odata.api;

/**
 * @author SAP AG
 *
 */
public interface ODataDebugCallback extends ODataCallback {

  /**
   * Determines whether additional debug information can be retrieved
   * from this OData service for the current request.
   * @return <code>true</code> if the system is in debug mode
   *         and the current user has the rights to debug the OData service
   */
  boolean isDebugEnabled();

}
