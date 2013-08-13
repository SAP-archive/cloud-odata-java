package com.sap.core.odata.core.batch;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.sap.core.odata.core.exception.ODataRuntimeException;

public class BatchHelper {

  public static final String BINARY_ENCODING = "binary";

  public static final String DEFAULT_ENCODING = "utf-8";

  public static final String HTTP_CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";

  public static final String HTTP_CONTENT_ID = "Content-Id";

  public static final String MIME_HEADER_CONTENT_ID = "MimeHeader-ContentId";

  public static final String REQUEST_HEADER_CONTENT_ID = "RequestHeader-ContentId";

  protected static String generateBoundary(final String value) {
    return value + "_" + UUID.randomUUID().toString();
  }

  protected static byte[] getBytes(final String body) {
    try {
      return body.getBytes(DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new ODataRuntimeException(e);
    }
  }
}
