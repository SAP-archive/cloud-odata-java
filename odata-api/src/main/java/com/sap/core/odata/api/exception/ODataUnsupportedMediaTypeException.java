package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataUnsupportedMediaTypeException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataUnsupportedMediaTypeException.class, "COMMON");

  public ODataUnsupportedMediaTypeException(MessageReference context) {
    super(context, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }
}
