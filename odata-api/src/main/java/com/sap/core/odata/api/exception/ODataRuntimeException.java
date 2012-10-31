package com.sap.core.odata.api.exception;

public class ODataRuntimeException extends RuntimeException {

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
