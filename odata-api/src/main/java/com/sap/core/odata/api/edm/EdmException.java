package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class EdmException extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(EdmException.class, "COMMON");
  public static final MessageReference PROVIDERPROBLEM = createMessageReference(EdmException.class, "PROVIDERPROBLEM");

  public EdmException(MessageReference messageReference) {
    super(messageReference);
  }

  public EdmException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }

}
