package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class BatchException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** INVALID_CHANGESET_BOUNDARY requires no content value */
  public static final MessageReference INVALID_CHANGESET_BOUNDARY = createMessageReference(BatchException.class, "INVALID_CHANGESET_BOUNDARY");

  /** INVALID_BOUNDARY_DELIMITER requires no content value */
  public static final MessageReference INVALID_BOUNDARY_DELIMITER = createMessageReference(BatchException.class, "INVALID_BOUNDARY_DELIMITER");

  /** MISSING_BOUNDARY_DELIMITER requires no content value */
  public static final MessageReference MISSING_BOUNDARY_DELIMITER = createMessageReference(BatchException.class, "MISSING_BOUNDARY_DELIMITER");

  /** MISSING_CLOSE_DELIMITER requires no content value */
  public static final MessageReference MISSING_CLOSE_DELIMITER = createMessageReference(BatchException.class, "MISSING_CLOSE_DELIMITER");

  /** INVALID_QUERY_OPERATION_METHOD requires no content value */
  public static final MessageReference INVALID_QUERY_OPERATION_METHOD = createMessageReference(BatchException.class, "INVALID_QUERY_OPERATION_METHOD");

  /** INVALID_CHANGESET_METHOD requires no content value */
  public static final MessageReference INVALID_CHANGESET_METHOD = createMessageReference(BatchException.class, "INVALID_CHANGESET_METHOD");

  /** INVALID_QUERY_PARAMETER requires no content value */
  public static final MessageReference INVALID_QUERY_PARAMETER = createMessageReference(BatchException.class, "INVALID_QUERY_PARAMETER");

  /** INVALID_URI requires no content value */
  public static final MessageReference INVALID_URI = createMessageReference(BatchException.class, "INVALID_URI");

  /** INVALID_BOUNDARY requires no content value */
  public static final MessageReference INVALID_BOUNDARY = createMessageReference(BatchException.class, "INVALID_BOUNDARY");

  /** NO_MATCH_WITH_BOUNDARY_STRING requires 1 content value ('required boundary') */
  public static final MessageReference NO_MATCH_WITH_BOUNDARY_STRING = createMessageReference(BatchException.class, "NO_MATCH_WITH_BOUNDARY_STRING");

  /** MISSING_CONTENT_TYPE requires no content value */
  public static final MessageReference MISSING_CONTENT_TYPE = createMessageReference(BatchException.class, "MISSING_CONTENT_TYPE");

  /** INVALID_CONTENT_TYPE requires 1 content value ('required content-type') */
  public static final MessageReference INVALID_CONTENT_TYPE = createMessageReference(BatchException.class, "INVALID_CONTENT_TYPE");

  /** MISSING_PARAMETER_IN_CONTENT_TYPE requires no content value */
  public static final MessageReference MISSING_PARAMETER_IN_CONTENT_TYPE = createMessageReference(BatchException.class, "MISSING_PARAMETER_IN_CONTENT_TYPE");

  /** INVALID_HEADER requires 1 content value ('header') */
  public static final MessageReference INVALID_HEADER = createMessageReference(BatchException.class, "INVALID_HEADER");

  /** INVALID_ACCEPT_HEADER requires 1 content value ('header') */
  public static final MessageReference INVALID_ACCEPT_HEADER = createMessageReference(BatchException.class, "INVALID_ACCEPT_HEADER");

  /** INVALID_ACCEPT_LANGUAGE_HEADER requires 1 content value ('header') */
  public static final MessageReference INVALID_ACCEPT_LANGUAGE_HEADER = createMessageReference(BatchException.class, "INVALID_ACCEPT_LANGUAGE_HEADER");

  /** INVALID_CONTENT_TRANSFER_ENCODING requires no content value */
  public static final MessageReference INVALID_CONTENT_TRANSFER_ENCODING = createMessageReference(BatchException.class, "INVALID_CONTENT_TRANSFER_ENCODING");

  /** MISSING_BLANK_LINE requires no content value */
  public static final MessageReference MISSING_BLANK_LINE = createMessageReference(BatchException.class, "MISSING_BLANK_LINE");

  /** INVALID_PATHINFO requires no content value */
  public static final MessageReference INVALID_PATHINFO = createMessageReference(BatchException.class, "INVALID_PATHINFO");

  /** MISSING_METHOD requires 1 content value ('request line') */
  public static final MessageReference MISSING_METHOD = createMessageReference(BatchException.class, "MISSING_METHOD");

  /** INVALID_REQUEST_LINE requires 1 content value ('request line') */
  public static final MessageReference INVALID_REQUEST_LINE = createMessageReference(BatchException.class, "INVALID_REQUEST_LINE");

  public BatchException(final MessageReference messageReference) {
    super(messageReference);
  }

  public BatchException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause);
  }

  public BatchException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, errorCode);
  }

  public BatchException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, errorCode);
  }

}
