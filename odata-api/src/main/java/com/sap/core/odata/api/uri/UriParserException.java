package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.exception.ODataException;

public class UriParserException extends ODataException {

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
