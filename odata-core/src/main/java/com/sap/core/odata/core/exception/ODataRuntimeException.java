package com.sap.core.odata.core.exception;

/**
 * Common un-checked exception for <code>OData</code> library and base exception for all <code>OData</code> 
 * related exceptions which are caused by a programming error and/or an unexcpected behavior within the code.
 */
public final class ODataRuntimeException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ODataRuntimeException() {
    super();
  }

  public ODataRuntimeException(String message) {
    super(message);
  }

  public ODataRuntimeException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
