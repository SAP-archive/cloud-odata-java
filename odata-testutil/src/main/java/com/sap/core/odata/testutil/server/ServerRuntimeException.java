package com.sap.core.odata.testutil.server;

/**
 * @author SAP AG
 */
public class ServerRuntimeException extends RuntimeException {

  public ServerRuntimeException(final Exception e) {
    super(e);
  }

  private static final long serialVersionUID = 1L;

}
