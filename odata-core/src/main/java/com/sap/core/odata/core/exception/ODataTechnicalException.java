package com.sap.core.odata.core.exception;

public class ODataTechnicalException extends RuntimeException implements ODataException {

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

}
