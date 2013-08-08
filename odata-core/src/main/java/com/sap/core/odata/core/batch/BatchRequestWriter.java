package com.sap.core.odata.core.batch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.client.batch.BatchChangeSet;
import com.sap.core.odata.api.client.batch.BatchChangeSetPart;
import com.sap.core.odata.api.client.batch.BatchPart;
import com.sap.core.odata.api.client.batch.BatchQueryPart;
import com.sap.core.odata.api.commons.HttpHeaders;

public class BatchRequestWriter extends BatchWriter {
  private static final String REG_EX_BOUNDARY = "([a-zA-Z0-9_\\-\\.'\\+]{1,70})|\"([a-zA-Z0-9_\\-\\.'\\+\\s\\(\\),/:=\\?]{1,69}[a-zA-Z0-9_\\-\\.'\\+\\(\\),/:=\\?])\""; // See RFC 2046
  private static final String COLON = ":";
  private static final String SP = " ";
  private static final String LF = "\r\n";
  private String batchBoundary;
  private StringBuilder writer = new StringBuilder();

  public InputStream writeBatchRequest(final List<BatchPart> batchParts, final String boundary) {
    if (boundary.matches(REG_EX_BOUNDARY)) {
      batchBoundary = boundary;
    } else {
      throw new IllegalArgumentException();
    }
    for (BatchPart batchPart : batchParts) {
      writer.append("--" + boundary).append(LF);
      if (batchPart instanceof BatchChangeSet) {
        appendChangeSet((BatchChangeSet) batchPart);
      } else if (batchPart instanceof BatchQueryPart) {
        BatchQueryPart request = (BatchQueryPart) batchPart;
        appendRequestBodyPart(request.getMethod(), request.getUri(), null, request.getHeaders(), request.getContentId());

      }
    }
    writer.append("--").append(boundary).append("--").append(LF).append(LF);
    InputStream batchRequestBody;
    batchRequestBody = new ByteArrayInputStream(writer.toString().getBytes());

    return batchRequestBody;
  }

  private void appendChangeSet(final BatchChangeSet batchChangeSet) {
    String boundary = generateBoundary("changeset");
    while (boundary.equals(batchBoundary) || !boundary.matches(REG_EX_BOUNDARY)) {
      boundary = generateBoundary("changeset");
    }
    writer.append(HttpHeaders.CONTENT_TYPE).append(COLON).append(SP).append(BatchConstants.MULTIPART_MIXED + "; boundary=" + boundary).append(LF).append(LF);
    for (BatchChangeSetPart request : batchChangeSet.getChangeSetParts()) {
      writer.append("--").append(boundary).append(LF);
      appendRequestBodyPart(request.getMethod(), request.getUri(), request.getBody(), request.getHeaders(), request.getContentId());
    }
    writer.append("--").append(boundary).append("--").append(LF).append(LF);
  }

  private void appendRequestBodyPart(final String method, final String uri, final String body, final Map<String, String> headers, final String contentId) {
    boolean isContentLengthPresent = false;
    writer.append(HttpHeaders.CONTENT_TYPE).append(COLON).append(SP).append(BatchConstants.HTTP_APPLICATION_HTTP).append(LF);
    writer.append(BatchConstants.HTTP_CONTENT_TRANSFER_ENCODING).append(COLON).append(SP).append("binary").append(LF);
    if (contentId != null) {
      writer.append(BatchConstants.HTTP_CONTENT_ID).append(COLON).append(SP).append(contentId).append(LF);
    }
    for (Map.Entry<String, String> headerMap : headers.entrySet()) {
      if (headerMap.getKey().equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH) && !headerMap.getValue().isEmpty()) {
        isContentLengthPresent = true;
      }
    }
    writer.append(LF);
    writer.append(method).append(SP).append(uri).append(SP).append("HTTP/1.1");
    writer.append(LF);

    if (!isContentLengthPresent && body != null && !body.isEmpty()) {
      writer.append(HttpHeaders.CONTENT_LENGTH).append(COLON).append(SP).append(body.getBytes().length).append(LF);
    }
    appendHeader(headers);

    if (body != null && !body.isEmpty()) {
      writer.append(LF);
      writer.append(body);
    }
    writer.append(LF).append(LF);
  }

  private void appendHeader(final Map<String, String> headers) {
    for (Map.Entry<String, String> headerMap : headers.entrySet()) {
      String name = headerMap.getKey();
      writer.append(name).append(COLON).append(SP).append(headerMap.getValue()).append(LF);
    }
  }
}
