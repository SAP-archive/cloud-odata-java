package com.sap.core.odata.core.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse;

public class BatchResponseWriter {
  private static final String COLON = ":";
  private static final String SP = " ";
  private static final String LF = "\n";

  public ODataResponse writeChangeSet(final List<ODataResponse> responses) throws EntityProviderException {
    String boundary = generateBoundary("changeset");
    StringBuilder writer = new StringBuilder();
    writeResponseBody(responses, boundary, writer);
    return ODataResponse.entity(writer.toString()).
        header(BatchConstants.HTTP_CONTENT_TYPE, BatchConstants.MULTIPART_MIXED + "; boundary=" + boundary).build();
  }

  public ODataResponse write(final List<ODataResponse> responses) throws EntityProviderException {
    String boundary = generateBoundary("batch");
    StringBuilder writer = new StringBuilder();
    writeResponseBody(responses, boundary, writer);
    String batchResponseBody = writer.toString();
    return ODataResponse.entity(batchResponseBody).status(HttpStatusCodes.ACCEPTED).
        header(BatchConstants.HTTP_CONTENT_TYPE, BatchConstants.MULTIPART_MIXED + "; boundary=" + boundary)
        .header(BatchConstants.HTTP_CONTENT_LENGTH, "" + batchResponseBody.length()).build();

  }

  private void writeResponseBody(final List<ODataResponse> responses, final String boundary, final StringBuilder writer) throws EntityProviderException {
    for (ODataResponse response : responses) {
      writer.append("--").append(boundary).append(LF);
      writeResponseBodyPart(response, writer);
    }
    writer.append("--").append(boundary).append("--");
  }

  private void writeResponseBodyPart(final ODataResponse response, final StringBuilder writer) throws EntityProviderException {
    if (response.getContentHeader() != null && response.getContentHeader().matches(BatchConstants.MULTIPART_MIXED + ".*")) {
      writeHeader(response, writer);
      writer.append(LF);
      writer.append(response.getEntity().toString()).append(LF);
    } else {
      writer.append(BatchConstants.HTTP_CONTENT_TYPE).append(COLON).append(SP).append(BatchConstants.HTTP_APPLICATION_HTTP).append(LF);
      writer.append(BatchConstants.HTTP_CONTENT_TRANSFER_ENCODING).append(COLON).append(SP).append("binary").append(LF).append(LF);

      writer.append("HTTP/1.1").append(SP).append(response.getStatus().getStatusCode()).append(SP).append(response.getStatus().getInfo()).append(LF);
      writeHeader(response, writer);
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
      writer.append(LF);
    }
    writer.append(LF);
  }

  private void writeHeader(final ODataResponse response, final StringBuilder writer) {
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
    } finally {
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
