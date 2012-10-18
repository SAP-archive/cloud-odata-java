package com.sap.core.odata.api.exception;

public class ODataError extends Exception {

  public ODataError() {
    super();
  }

  public ODataError(String msg) {
    super(msg);
  }

  public ODataError(String msg, Throwable e) {
    super(msg, e);
  }

  public ODataError(Throwable e) {
    super(e);
  }

  private static final long serialVersionUID = 1L;

}
