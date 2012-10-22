package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.exception.ODataException;

public class EdmException extends ODataException {

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
