package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataUnsupportedMediaTypeException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataUnsupportedMediaTypeException.class, "COMMON", HttpStatus.UNSUPPORTED_MEDIA_TYPE);

  public ODataUnsupportedMediaTypeException(MessageReference context) {
    super(context);
  }
}
