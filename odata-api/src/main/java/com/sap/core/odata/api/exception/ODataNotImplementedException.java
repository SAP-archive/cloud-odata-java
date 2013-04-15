/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 501 Not implemented
 * @author SAP AG
 */
public class ODataNotImplementedException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataNotImplementedException.class, "COMMON");

  public ODataNotImplementedException(final MessageReference context) {
    super(context, HttpStatusCodes.NOT_IMPLEMENTED);
  }

  public ODataNotImplementedException(final MessageReference context, final String errorCode) {
    super(context, HttpStatusCodes.NOT_IMPLEMENTED, errorCode);
  }

  public ODataNotImplementedException() {
    this(COMMON);
  }
}
