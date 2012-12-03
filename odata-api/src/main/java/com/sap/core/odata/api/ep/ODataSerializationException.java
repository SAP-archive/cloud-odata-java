package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;


public class ODataSerializationException extends ODataMessageException {

  private static final long serialVersionUID = 1L;
  
  public static final MessageReference COMMON = createMessageReference(ODataSerializationException.class, "COMMON");

  public static final MessageReference ATOM_TITLE = createMessageReference(ODataSerializationException.class, "ATOM_TITLE");

  public static final MessageReference MISSING_PROPERTY = createMessageReference(ODataSerializationException.class, "MISSING_PROPERTY");

  public static final MessageReference UNSUPPORTED_PROPERTY_TYPE = createMessageReference(ODataSerializationException.class, "UNSUPPORTED_PROPERTY_TYPE");;

  public ODataSerializationException(MessageReference messageReference) {
    super(messageReference);
  }
  
  public ODataSerializationException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
