package com.sap.core.odata.testutil.helper;

/**
 * This class is a helper to throw RuntimeExceptions in testutilmethods
 * @author SAP AG
 *
 */
public class TestutilException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public TestutilException(Throwable e){
    super(e);
  }
  
  
}
