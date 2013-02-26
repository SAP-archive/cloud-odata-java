package com.sap.core.odata.core.exception;

/**
 * Common un-checked exception for the <code>OData</code> library and
 * base exception for all <code>OData</code>-related exceptions
 * caused by programming errors and/or unexpected behavior within the code.
 * @author SAP AG
 */
public final class ODataRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ODataRuntimeException() {
    super();
  }

  public ODataRuntimeException(final String message) {
    super(message);
  }

  public ODataRuntimeException(final String message, final Throwable throwable) {
    super(message, throwable);
  }

}
