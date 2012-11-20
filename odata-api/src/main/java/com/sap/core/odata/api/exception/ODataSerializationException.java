package com.sap.core.odata.api.exception;


public class ODataSerializationException extends ODataMessageException {

  private static final long serialVersionUID = 1L;
  
  public static final MessageReference COMMON = createMessageReference(ODataSerializationException.class, "COMMON");

  public ODataSerializationException(MessageReference messageReference) {
    super(messageReference);
  }
  
  public ODataSerializationException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
