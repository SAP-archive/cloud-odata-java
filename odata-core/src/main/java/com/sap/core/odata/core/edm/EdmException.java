package com.sap.core.odata.core.edm;

public class EdmException extends Exception {

  public EdmException() {
    super();
  }

  public EdmException(String msg) {
    super(msg);
  }

  public EdmException(String msg, Throwable e) {
    super(msg, e);
  }

  public EdmException(Throwable e) {
    super(e);
  }

  private static final long serialVersionUID = 1L;

}
