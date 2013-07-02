package com.sap.core.odata.core.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse;

public class BatchResponseWriter {
  private static final String COLON = ":";
  private static final String SP = " ";
  private static final String LF = "\r\n";
  private StringBuilder writer = new StringBuilder();

  private void appendChangeSet(final BatchResponsePart batchResponsePart) throws EntityProviderException {
    String boundary = generateBoundary("changeset");
    writer.append(BatchConstants.HTTP_CONTENT_TYPE).append(COLON).append(SP).append("multipart/mixed; boundary=" + boundary).append(LF).append(LF);
    for (ODataResponse response : batchResponsePart.getResponses()) {
      writer.append("--").append(boundary).append(LF);
      appendResponseBodyPart(response);
    }
    writer.append("--").append(boundary).append("--").append(LF).append(LF);
  }

  public ODataResponse writeResponse(final List<BatchResponsePart> batchResponseParts) throws EntityProviderException {
    String boundary = generateBoundary("batch");
    appendResponseBody(batchResponseParts, boundary);
    String batchResponseBody = writer.toString();
    return ODataResponse.entity(batchResponseBody).status(HttpStatusCodes.ACCEPTED).
        header(BatchConstants.HTTP_CONTENT_TYPE, BatchConstants.MULTIPART_MIXED + "; boundary=" + boundary)
        .header(BatchConstants.HTTP_CONTENT_LENGTH, "" + batchResponseBody.length()).build();

  }

  private void appendResponseBody(final List<BatchResponsePart> batchResponseParts, final String boundary) throws EntityProviderException {

    for (BatchResponsePart batchResponsePart : batchResponseParts) {
      writer.append("--").append(boundary).append(LF);
      if (batchResponsePart.isChangeSet()) {
        appendChangeSet(batchResponsePart);
      } else {
        ODataResponse response = batchResponsePart.getResponses().get(0);
        appendResponseBodyPart(response);
      }
    }
    writer.append("--").append(boundary).append("--");
  }

  private void appendResponseBodyPart(final ODataResponse response) throws EntityProviderException {
    writer.append(BatchConstants.HTTP_CONTENT_TYPE).append(COLON).append(SP).append(BatchConstants.HTTP_APPLICATION_HTTP).append(LF);
    writer.append(BatchConstants.HTTP_CONTENT_TRANSFER_ENCODING).append(COLON).append(SP).append("binary").append(LF).append(LF);
    writer.append("HTTP/1.1").append(SP).append(response.getStatus().getStatusCode()).append(SP).append(response.getStatus().getInfo()).append(LF);
    appendHeader(response);
    if (!HttpStatusCodes.NO_CONTENT.equals(response.getStatus())) {
      String body;
      if (response.getEntity() instanceof InputStream) {
        InputStream in = (InputStream) response.getEntity();
        body = readBody(in);
      } else {
        body = response.getEntity().toString();
      }
      writer.append(BatchConstants.HTTP_CONTENT_LENGTH).append(COLON).append(SP).append(body.length()).append(LF).append(LF);
      writer.append(body);
    }
    writer.append(LF).append(LF);
  }

  private void appendHeader(final ODataResponse response) {
    for (String name : response.getHeaderNames()) {
      writer.append(name).append(COLON).append(SP).append(response.getHeader(name)).append(LF);
    }
  }

  private String generateBoundary(final String value) {
    return value + "_" + UUID.randomUUID().toString();
  }

  private String readBody(final InputStream in) throws EntityProviderException {
    byte[] tmp = new byte[2048];
    int count;
    EntityProviderException cachedException = null;
    StringBuffer b = new StringBuffer();
    try {
      count = in.read(tmp);
      while (count >= 0) {
        b.append(new String(tmp, 0, count));
        count = in.read(tmp);
      }
    } catch (IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
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

}