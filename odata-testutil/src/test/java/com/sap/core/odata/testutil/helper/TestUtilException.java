package com.sap.core.odata.testutil.helper;

/**
 * This class is a helper to throw RuntimeExceptions in test util methods
 * @author SAP AG
 *
 */
public class TestUtilException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public TestUtilException(Throwable e) {
    super(e);
  }

}
