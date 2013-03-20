package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * An {@link EntityProviderException} is the base exception for all <code>EntityProvider</code> related exceptions.
 * It extends the {@link ODataMessageException} and provides several {@link MessageReference} for specification of 
 * the thrown exception.
 */
public class EntityProviderException extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(EntityProviderException.class, "COMMON");
  public static final MessageReference MISSING_PROPERTY = createMessageReference(EntityProviderException.class, "MISSING_PROPERTY");
  public static final MessageReference MISSING_ATTRIBUTE = createMessageReference(EntityProviderException.class, "MISSING_ATTRIBUTE");
  public static final MessageReference UNSUPPORTED_PROPERTY_TYPE = createMessageReference(EntityProviderException.class, "UNSUPPORTED_PROPERTY_TYPE");
  public static final MessageReference INLINECOUNT_INVALID = createMessageReference(EntityProviderException.class, "INLINECOUNT_INVALID");
  /** INVALID_STATE requires 1 content value ('message') */
  public static final MessageReference INVALID_STATE = createMessageReference(EntityProviderException.class, "INVALID_STATE");
  public static final MessageReference INVALID_PROPERTY = createMessageReference(EntityProviderException.class, "INVALID_PROPERTY");
  /** ILLEGAL_ARGUMENT requires 1 content value ('message') */
  public static final MessageReference ILLEGAL_ARGUMENT = createMessageReference(EntityProviderException.class, "ILLEGAL_ARGUMENT");
  /** INVALID_NAMESPACE requires 1 content value ('invalid tag/namespace') */
  public static final MessageReference INVALID_NAMESPACE = createMessageReference(EntityProviderException.class, "INVALID_NAMESPACE");
  /** INVALID_PARENT_TAG requires 2 content value ('expected parent tag' and 'found parent tag') */
  public static final MessageReference INVALID_PARENT_TAG = createMessageReference(EntityProviderException.class, "INVALID_PARENT_TAG");
  public static final MessageReference EXPANDNOTSUPPORTED = createMessageReference(EntityProviderException.class, "EXPANDNOTSUPPORTED");
  
  public EntityProviderException(final MessageReference messageReference) {
    super(messageReference);
  }

  public EntityProviderException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause);
  }

  public EntityProviderException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, errorCode);
  }

  public EntityProviderException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, errorCode);
  }
}
