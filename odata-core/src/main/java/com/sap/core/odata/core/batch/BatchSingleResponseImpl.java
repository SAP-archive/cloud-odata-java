package com.sap.core.odata.core.batch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sap.core.odata.api.client.batch.BatchSingleResponse;

public class BatchSingleResponseImpl implements BatchSingleResponse {

  private String statusCode;
  private String statusInfo;
  private String body;
  private Map<String, String> headers = new HashMap<String, String>();
  private String contentId;

  @Override
  public String getStatusCode() {
    return statusCode;
  }

  @Override
  public String getStatusInfo() {
    return statusInfo;
  }

  @Override
  public String getBody() {
    return body;
  }

  @Override
  public Map<String, String> getHeaders() {
    return headers;
  }

  @Override
  public String getContentId() {
    return contentId;
  }

  @Override
  public String getHeader(final String name) {
    return headers.get(name);
  }

  @Override
  public Set<String> getHeaderNames() {
    return headers.keySet();
  }

  public void setStatusCode(final String statusCode) {
    this.statusCode = statusCode;
  }

  public void setStatusInfo(final String statusInfo) {
    this.statusInfo = statusInfo;
  }

  public void setBody(final String body) {
    this.body = body;
  }

  public void setHeaders(final Map<String, String> headers) {
    this.headers = headers;
  }

  public void setContentId(final String contentId) {
    this.contentId = contentId;
  }

}
