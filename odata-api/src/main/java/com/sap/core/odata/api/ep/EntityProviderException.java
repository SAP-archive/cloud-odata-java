package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class EntityProviderException extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(EntityProviderException.class, "COMMON");
  public static final MessageReference MISSING_PROPERTY = createMessageReference(EntityProviderException.class, "MISSING_PROPERTY");
  public static final MessageReference MISSING_ATTRIBUTE = createMessageReference(EntityProviderException.class, "MISSING_ATTRIBUTE");
  public static final MessageReference UNSUPPORTED_PROPERTY_TYPE = createMessageReference(EntityProviderException.class, "UNSUPPORTED_PROPERTY_TYPE");
  public static final MessageReference INLINECOUNT_INVALID = createMessageReference(EntityProviderException.class, "INLINECOUNT_INVALID");
  public static final MessageReference INVALID_STATE = createMessageReference(EntityProviderException.class, "INVALID_STATE");
  public static final MessageReference INVALID_PROPERTY = createMessageReference(EntityProviderException.class, "INVALID_PROPERTY");
  public static final MessageReference ILLEGAL_ARGUMENT = createMessageReference(EntityProviderException.class, "ILLEGAL_ARGUMENT");
  
  public EntityProviderException(MessageReference messageReference) {
    super(messageReference);
  }

  public EntityProviderException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
