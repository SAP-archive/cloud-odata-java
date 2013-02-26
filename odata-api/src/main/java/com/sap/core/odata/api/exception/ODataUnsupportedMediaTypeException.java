package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 415 unsupported media type
 * @author SAP AG
 */
public class ODataUnsupportedMediaTypeException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataUnsupportedMediaTypeException.class, "COMMON");
  public static final MessageReference NOT_SUPPORTED = createMessageReference(ODataUnsupportedMediaTypeException.class, "NOT_SUPPORTED");

  public ODataUnsupportedMediaTypeException(final MessageReference context) {
    super(context, HttpStatusCodes.UNSUPPORTED_MEDIA_TYPE);
  }

  public ODataUnsupportedMediaTypeException(final MessageReference context, final Throwable cause) {
    super(context, cause, HttpStatusCodes.UNSUPPORTED_MEDIA_TYPE);
  }

  public ODataUnsupportedMediaTypeException(final MessageReference context, final String errorCode) {
    super(context, HttpStatusCodes.UNSUPPORTED_MEDIA_TYPE, errorCode);
  }

  public ODataUnsupportedMediaTypeException(final MessageReference context, final Throwable cause, final String errorCode) {
    super(context, cause, HttpStatusCodes.UNSUPPORTED_MEDIA_TYPE, errorCode);
  }
}
