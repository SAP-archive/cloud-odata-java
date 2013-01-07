package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.exception.MessageReference;

/**
 * Exception for violation of the OData URI construction rules,
 * resulting in a 400 Bad Request response
 * @author SAP AG
 */
public class EdmLiteralException extends EdmException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference NOTEXT = createMessageReference(EdmLiteralException.class, "NOTEXT");
  public static final MessageReference LITERALFORMAT = createMessageReference(EdmLiteralException.class, "LITERALFORMAT");
  public static final MessageReference UNKNOWNLITERAL = createMessageReference(EdmLiteralException.class, "UNKNOWNLITERAL");
  
  public EdmLiteralException(MessageReference MessageReference) {
    super(MessageReference);
  }
  
  public EdmLiteralException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
