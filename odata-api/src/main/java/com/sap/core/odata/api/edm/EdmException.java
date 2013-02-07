package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * @author SAP AG
 */
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
  
  public EdmException(MessageReference messageReference, String errorCode) {
    super(messageReference, errorCode);
  }

  public EdmException(MessageReference messageReference, Throwable cause, String errorCode) {
    super(messageReference, cause, errorCode);
  }

}
