package com.sap.core.odata.core.uri;

public class UriParserException extends Exception {

  public UriParserException() {
    super();
  }

  public UriParserException(String msg) {
    super(msg);
  }

  public UriParserException(String msg, Throwable e) {
    super(msg, e);
  }

  public UriParserException(Throwable e) {
    super(e);
  }

  private static final long serialVersionUID = 1L;

}
