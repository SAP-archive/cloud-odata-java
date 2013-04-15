/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * @com.sap.core.odata.DoNotImplement
 * An exception for problems regarding the Entity Data Model.
 * @author SAP AG
 */
public class EdmException extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(EdmException.class, "COMMON");
  public static final MessageReference PROVIDERPROBLEM = createMessageReference(EdmException.class, "PROVIDERPROBLEM");

  public EdmException(final MessageReference messageReference) {
    super(messageReference);
  }

  public EdmException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause);
  }

  public EdmException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, errorCode);
  }

  public EdmException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, errorCode);
  }

}
