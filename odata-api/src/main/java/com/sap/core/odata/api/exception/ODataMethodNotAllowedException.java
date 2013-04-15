/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 405 method not allowed
 * @author SAP AG
 */
public class ODataMethodNotAllowedException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference DISPATCH = createMessageReference(ODataMethodNotAllowedException.class, "DISPATCH");
  public static final MessageReference TUNNELING = createMessageReference(ODataMethodNotAllowedException.class, "TUNNELING");

  public ODataMethodNotAllowedException(final MessageReference messageReference) {
    super(messageReference, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  public ODataMethodNotAllowedException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  public ODataMethodNotAllowedException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }

  public ODataMethodNotAllowedException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }

}
