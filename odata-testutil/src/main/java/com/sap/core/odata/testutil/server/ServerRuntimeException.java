/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.testutil.server;

public class ServerRuntimeException extends RuntimeException {

  public ServerRuntimeException(final Exception e) {
    super(e);
  }

  private static final long serialVersionUID = 1L;

}
