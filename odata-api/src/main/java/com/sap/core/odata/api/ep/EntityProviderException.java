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
  /** INVALIDMAPPING requires 1 content value ('propertyName') */
  public static final MessageReference INVALID_MAPPING = createMessageReference(EntityProviderException.class, "INVALID_MAPPING");
  /** INVALIDMAPPING requires 2 content values ('supplied entity type' and 'content entity type') */
  public static final MessageReference INVALID_ENTITYTYPE = createMessageReference(EntityProviderException.class, "INVALID_ENTITYTYPE");
  /** INVALIDMAPPING requires 2 content values ('invalid tag' and 'parent tag') */
  public static final MessageReference INVALID_CONTENT = createMessageReference(EntityProviderException.class, "INVALID_CONTENT");

  public static final MessageReference MISSING_PROPERTY = createMessageReference(EntityProviderException.class, "MISSING_PROPERTY");
  /** INVALID_PARENT_TAG requires 2 content values ('missing attribute name' and 'tag name') */
  public static final MessageReference MISSING_ATTRIBUTE = createMessageReference(EntityProviderException.class, "MISSING_ATTRIBUTE");
  public static final MessageReference UNSUPPORTED_PROPERTY_TYPE = createMessageReference(EntityProviderException.class, "UNSUPPORTED_PROPERTY_TYPE");
  public static final MessageReference INLINECOUNT_INVALID = createMessageReference(EntityProviderException.class, "INLINECOUNT_INVALID");
  /** INVALID_STATE requires 1 content value ('message') */
  public static final MessageReference INVALID_STATE = createMessageReference(EntityProviderException.class, "INVALID_STATE");
  /** INVALID_INLINE_CONTENT requires 1 content value ('invalid inline message') */
  public static final MessageReference INVALID_INLINE_CONTENT = createMessageReference(EntityProviderException.class, "INVALID_INLINE_CONTENT");
  public static final MessageReference INVALID_PROPERTY = createMessageReference(EntityProviderException.class, "INVALID_PROPERTY");
  /** ILLEGAL_ARGUMENT requires 1 content value ('message') */
  public static final MessageReference ILLEGAL_ARGUMENT = createMessageReference(EntityProviderException.class, "ILLEGAL_ARGUMENT");
  /** INVALID_NAMESPACE requires 1 content value ('invalid tag/namespace') */
  public static final MessageReference INVALID_NAMESPACE = createMessageReference(EntityProviderException.class, "INVALID_NAMESPACE");
  /** INVALID_PARENT_TAG requires 2 content values ('expected parent tag' and 'found parent tag') */
  public static final MessageReference INVALID_PARENT_TAG = createMessageReference(EntityProviderException.class, "INVALID_PARENT_TAG");
  public static final MessageReference EXPANDNOTSUPPORTED = createMessageReference(EntityProviderException.class, "EXPANDNOTSUPPORTED");
  /** DOUBLE_PROPERTY requires 1 content value ('double tag/property') */
  public static final MessageReference DOUBLE_PROPERTY = createMessageReference(EntityProviderException.class, "DOUBLE_PROPERTY");
  /** NOT_SET_CHARACTER_ENCODING requires no content value */
  public static final MessageReference NOT_SET_CHARACTER_ENCODING = createMessageReference(EntityProviderException.class, "NOT_SET_CHARACTER_ENCODING");
  /** UNSUPPORTED_CHARACTER_ENCODING requires 1 content value ('found but unsupported character encoding') */
  public static final MessageReference UNSUPPORTED_CHARACTER_ENCODING = createMessageReference(EntityProviderException.class, "UNSUPPORTED_CHARACTER_ENCODING");

  public static final MessageReference MEDIA_DATA_NOT_INITIAL = createMessageReference(EntityProviderException.class, "MEDIA_DATA_NOT_INITIAL");

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
