package com.sap.core.odata.api.exception;

public class ODataTechnicalException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ODataTechnicalException() {
    super();
  }

  public ODataTechnicalException(String message) {
    super(message);
  }

  public ODataTechnicalException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
