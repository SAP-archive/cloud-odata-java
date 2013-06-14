package com.sap.core.odata.core.batch;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataResponse;

public class BatchWriter {
  private static final String LF = "\n";
  private StringBuilder writer = new StringBuilder();

  public void appendResponse(final ODataResponse response) {
    writer.append(BatchRequestConstants.HTTP_CONTENT_TYPE).append(": ").append("application/http").append(LF);
    writer.append(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING).append(": ").append("binary").append(LF).append(LF);
    writer.append(response.getStatus().getStatusCode()).append(" ");
    writer.append(response.getStatus().getInfo()).append(LF);
    for (String name : response.getHeaderNames()) {
      writer.append(name + ": ").append(response.getHeader(name)).append(LF);
    }
    if (!HttpStatusCodes.NO_CONTENT.equals(response.getStatus())) {
      writer.append("Content-Length: ").append(response.getEntity().toString().length()).append(LF).append(LF);
      writer.append(response.getEntity().toString()).append(LF);
    }
    writer.append(LF);
  }

  public void appendDelimiter(final String boundary) {
    writer.append("--").append(boundary).append(LF);
  }

  public void appendChangeSet(final String boundary) {
    writer.append(BatchRequestConstants.HTTP_CONTENT_TYPE).append(": ").append("multipart/mixed; boundary=" + boundary).append(LF).append(LF);
  }

  public void appendCloseDelimiter(final String boundary) {
    writer.append("--").append(boundary).append("--").append(LF).append(LF);
  }

  public ODataResponse createResponse(final String boundary) {
    appendCloseDelimiter(boundary);
    return ODataResponse.entity(writer.toString()).status(HttpStatusCodes.ACCEPTED).header("Content-Type", "multipart/mixed; boundary=" + boundary).build();
  }

}
