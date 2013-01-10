package com.sap.core.odata.api.ec;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class EntityConsumerException extends ODataMessageException {
  private static final long serialVersionUID = 1L;
  
  public static final MessageReference COMMON = createMessageReference(EntityConsumerException.class, "COMMON");
  public static final MessageReference INVALID_STATE = createMessageReference(EntityConsumerException.class, "INVALID_STATE");

  public static final MessageReference INVALID_PROPERTY = createMessageReference(EntityConsumerException.class, "INVALID_PROPERTY");

  public EntityConsumerException(MessageReference messageReference) {
    super(messageReference);
  }
  
  public EntityConsumerException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }

}
