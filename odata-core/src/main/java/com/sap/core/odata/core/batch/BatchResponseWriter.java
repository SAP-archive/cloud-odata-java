package com.sap.core.odata.core.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.sap.core.odata.api.batch.BatchException;
import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.processor.ODataResponse;

public class BatchResponseWriter {
  private static final String COLON = ":";
  private static final String SP = " ";
  private static final String LF = "\r\n";
  private ResponseWriter writer = new ResponseWriter();

  public ODataResponse writeResponse(final List<BatchResponsePart> batchResponseParts) throws BatchException {
    String boundary = BatchHelper.generateBoundary("batch");
    appendResponsePart(batchResponseParts, boundary);
    String batchResponseBody = writer.toString();
    return ODataResponse.entity(batchResponseBody).status(HttpStatusCodes.ACCEPTED)
        .header(HttpHeaders.CONTENT_TYPE, HttpContentType.MULTIPART_MIXED + "; boundary=" + boundary)
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(writer.length()))
        .build();
  }

  private void appendChangeSet(final BatchResponsePart batchResponsePart) throws BatchException {
    String boundary = BatchHelper.generateBoundary("changeset");
    writer.append(HttpHeaders.CONTENT_TYPE).append(COLON).append(SP)
        .append("multipart/mixed; boundary=" + boundary).append(LF).append(LF);
    for (ODataResponse response : batchResponsePart.getResponses()) {
      writer.append("--").append(boundary).append(LF);
      appendResponsePartBody(response);
    }
    writer.append("--").append(boundary).append("--").append(LF).append(LF);
  }

  private void appendResponsePart(final List<BatchResponsePart> batchResponseParts, final String boundary) throws BatchException {
    for (BatchResponsePart batchResponsePart : batchResponseParts) {
      writer.append("--").append(boundary).append(LF);
      if (batchResponsePart.isChangeSet()) {
        appendChangeSet(batchResponsePart);
      } else {
        ODataResponse response = batchResponsePart.getResponses().get(0);
        appendResponsePartBody(response);
      }
    }
    writer.append("--").append(boundary).append("--");
  }

  private void appendResponsePartBody(final ODataResponse response) throws BatchException {
    writer.append(HttpHeaders.CONTENT_TYPE).append(COLON).append(SP)
        .append(HttpContentType.APPLICATION_HTTP).append(LF);
    writer.append(BatchHelper.HTTP_CONTENT_TRANSFER_ENCODING).append(COLON).append(SP)
        .append(BatchHelper.BINARY_ENCODING).append(LF);
    if (response.getHeader(BatchHelper.MIME_HEADER_CONTENT_ID) != null) {
      writer.append(BatchHelper.HTTP_CONTENT_ID).append(COLON).append(SP)
          .append(response.getHeader(BatchHelper.MIME_HEADER_CONTENT_ID)).append(LF);
    }
    writer.append(LF);
    writer.append("HTTP/1.1").append(SP).append(String.valueOf(response.getStatus().getStatusCode())).append(SP)
        .append(response.getStatus().getInfo()).append(LF);
    appendHeader(response);
    if (!HttpStatusCodes.NO_CONTENT.equals(response.getStatus())) {
      String body;
      if (response.getEntity() instanceof InputStream) {
        InputStream in = (InputStream) response.getEntity();
        body = readBody(in);
      } else {
        body = response.getEntity().toString();
      }
      writer.append(HttpHeaders.CONTENT_LENGTH).append(COLON).append(SP)
          .append(String.valueOf(BatchHelper.getBytes(body).length)).append(LF).append(LF);
      writer.append(body);
    }
    writer.append(LF).append(LF);
  }

  private void appendHeader(final ODataResponse response) {
    for (String name : response.getHeaderNames()) {
      if (!BatchHelper.MIME_HEADER_CONTENT_ID.equalsIgnoreCase(name) && !BatchHelper.REQUEST_HEADER_CONTENT_ID.equalsIgnoreCase(name)) {
        writer.append(name).append(COLON).append(SP).append(response.getHeader(name)).append(LF);
      } else if (BatchHelper.REQUEST_HEADER_CONTENT_ID.equalsIgnoreCase(name)) {
        writer.append(BatchHelper.HTTP_CONTENT_ID).append(COLON).append(SP)
            .append(response.getHeader(name)).append(LF);
      }
    }
  }

  private String readBody(final InputStream in) throws BatchException {
    byte[] tmp = new byte[2048];
    int count;
    BatchException cachedException = null;
    StringBuffer b = new StringBuffer();
    try {
      count = in.read(tmp);
      while (count >= 0) {
        b.append(new String(tmp, 0, count, BatchHelper.DEFAULT_ENCODING));
        count = in.read(tmp);
      }
    } catch (IOException e) {
      cachedException = new BatchException(ODataMessageException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      try {
        in.close();
      } catch (IOException e) {
        if (cachedException != null) {
          throw cachedException;
        }
      }
    }
    return b.toString();
  }

  private static class ResponseWriter {
    private StringBuilder sb = new StringBuilder();
    private int length = 0;

    public ResponseWriter append(final String content) {
      length += BatchHelper.getBytes(content).length;
      sb.append(content);
      return this;
    }

    public int length() {
      return length;
    }

    @Override
    public String toString() {
      return sb.toString();
    }
  }

}