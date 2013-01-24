package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.exception.MessageReference;

/**
 * @author SAP AG
 */
public class EdmSimpleTypeException extends EdmException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(EdmSimpleTypeException.class, "COMMON");

  public static final MessageReference LITERAL_KIND_MISSING = createMessageReference(EdmSimpleTypeException.class, "LITERAL_KIND_MISSING");
  public static final MessageReference LITERAL_KIND_NOT_SUPPORTED = createMessageReference(EdmSimpleTypeException.class, "LITERAL_KIND_NOT_SUPPORTED");

  public static final MessageReference LITERAL_NULL_NOT_ALLOWED = createMessageReference(EdmSimpleTypeException.class, "LITERAL_NULL_NOT_ALLOWED");
  public static final MessageReference LITERAL_ILLEGAL_CONTENT = createMessageReference(EdmSimpleTypeException.class, "LITERAL_ILLEGAL_CONTENT");
  public static final MessageReference LITERAL_FACETS_NOT_MATCHED = createMessageReference(EdmSimpleTypeException.class, "LITERAL_FACETS_NOT_MATCHED");
  public static final MessageReference LITERAL_UNCONVERTIBLE_TO_VALUE_TYPE = createMessageReference(EdmSimpleTypeException.class, "LITERAL_UNCONVERTIBLE_TO_VALUE_TYPE");

  public static final MessageReference VALUE_TYPE_NOT_SUPPORTED = createMessageReference(EdmSimpleTypeException.class, "VALUE_TYPE_NOT_SUPPORTED");
  public static final MessageReference VALUE_NULL_NOT_ALLOWED = createMessageReference(EdmSimpleTypeException.class, "VALUE_NULL_NOT_ALLOWED");
  public static final MessageReference VALUE_ILLEGAL_CONTENT = createMessageReference(EdmSimpleTypeException.class, "VALUE_ILLEGAL_CONTENT");
  public static final MessageReference VALUE_FACETS_NOT_MATCHED = createMessageReference(EdmSimpleTypeException.class, "VALUE_FACETS_NOT_MATCHED");

  public EdmSimpleTypeException(MessageReference messageReference) {
    super(messageReference);
  }

  public EdmSimpleTypeException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
