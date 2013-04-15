/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 403 forbidden
 * @author SAP AG
 */
public class ODataForbiddenException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataForbiddenException.class, "COMMON");

  public ODataForbiddenException(final MessageReference context) {
    super(context, HttpStatusCodes.FORBIDDEN);
  }

  public ODataForbiddenException(final MessageReference context, final Throwable cause) {
    super(context, cause, HttpStatusCodes.FORBIDDEN);
  }

  public ODataForbiddenException(final MessageReference context, final String errorCode) {
    super(context, HttpStatusCodes.FORBIDDEN, errorCode);
  }

  public ODataForbiddenException(final MessageReference context, final Throwable cause, final String errorCode) {
    super(context, cause, HttpStatusCodes.FORBIDDEN, errorCode);
  }
}
