/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.exception.MessageReference;

/**
 * @com.sap.core.odata.DoNotImplement
 * Exception for violation of the OData URI construction rules, resulting in a 400 Bad Request response
 * @author SAP AG
 */
public class EdmLiteralException extends EdmException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference NOTEXT = createMessageReference(EdmLiteralException.class, "NOTEXT");
  public static final MessageReference LITERALFORMAT = createMessageReference(EdmLiteralException.class, "LITERALFORMAT");
  public static final MessageReference UNKNOWNLITERAL = createMessageReference(EdmLiteralException.class, "UNKNOWNLITERAL");

  public EdmLiteralException(final MessageReference MessageReference) {
    super(MessageReference);
  }

  public EdmLiteralException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause);
  }

  public EdmLiteralException(final MessageReference MessageReference, final String errorCode) {
    super(MessageReference, errorCode);
  }

  public EdmLiteralException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, errorCode);
  }
}
