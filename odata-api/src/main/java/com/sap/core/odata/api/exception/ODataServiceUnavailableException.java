/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 503 service unavailable
 * @author SAP AG
 */
public class ODataServiceUnavailableException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataServiceUnavailableException.class, "COMMON");

  public ODataServiceUnavailableException(final MessageReference context) {
    super(context, HttpStatusCodes.SERVICE_UNAVAILABLE);
  }

  public ODataServiceUnavailableException(final MessageReference context, final Throwable cause) {
    super(context, cause, HttpStatusCodes.SERVICE_UNAVAILABLE);
  }

  public ODataServiceUnavailableException(final MessageReference context, final String errorCode) {
    super(context, HttpStatusCodes.SERVICE_UNAVAILABLE, errorCode);
  }

  public ODataServiceUnavailableException(final MessageReference context, final Throwable cause, final String errorCode) {
    super(context, cause, HttpStatusCodes.SERVICE_UNAVAILABLE, errorCode);
  }

}
