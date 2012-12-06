package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;


public class ODataEntityProviderException extends ODataMessageException {

  private static final long serialVersionUID = 1L;
  
  public static final MessageReference COMMON = createMessageReference(ODataEntityProviderException.class, "COMMON");

  public static final MessageReference ATOM_TITLE = createMessageReference(ODataEntityProviderException.class, "ATOM_TITLE");

  public static final MessageReference MISSING_PROPERTY = createMessageReference(ODataEntityProviderException.class, "MISSING_PROPERTY");

  public static final MessageReference UNSUPPORTED_PROPERTY_TYPE = createMessageReference(ODataEntityProviderException.class, "UNSUPPORTED_PROPERTY_TYPE");;

  public ODataEntityProviderException(MessageReference messageReference) {
    super(messageReference);
  }
  
  public ODataEntityProviderException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
