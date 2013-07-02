package com.sap.core.odata.testutil;

/**
 * This class is a helper to throw RuntimeExceptions in test util methods
 * @author SAP AG
 *
 */
public class TestUtilRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TestUtilRuntimeException(final Throwable e) {
    super(e);
  }

  public TestUtilRuntimeException(final String msg) {
    super(msg);
  }

  public TestUtilRuntimeException(final String msg, final Throwable e) {
    super(msg, e);
  }

}
