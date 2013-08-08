package com.sap.core.odata.core.batch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.client.batch.BatchQueryPart;

public class BatchQueryPartImpl extends BatchQueryPart {
  private String method;
  private Map<String, String> headers = new HashMap<String, String>();
  private String uri;
  private String contentId;
  private static final String GET = "GET";

  @Override
  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(headers);
  }

  @Override
  public String getMethod() {
    return method;
  }

  @Override
  public String getUri() {
    return uri;
  }

  @Override
  public String getContentId() {
    return contentId;
  }

  public class BatchQueryRequestBuilderImpl extends BatchQueryPartBuilder {
    private String method;
    private Map<String, String> headers = new HashMap<String, String>();
    private String uri;
    private String contentId;

    @Override
    public BatchQueryPart build() {
      if (method == null || uri == null) {
        throw new IllegalArgumentException();
      }
      BatchQueryPartImpl.this.method = method;
      BatchQueryPartImpl.this.headers = headers;
      BatchQueryPartImpl.this.uri = uri;
      BatchQueryPartImpl.this.contentId = contentId;
      return BatchQueryPartImpl.this;
    }

    @Override
    public BatchQueryPartBuilder headers(final Map<String, String> headers) {
      this.headers = headers;
      return this;
    }

    @Override
    public BatchQueryPartBuilder uri(final String uri) {
      this.uri = uri;
      return this;
    }

    @Override
    public BatchQueryPartBuilder method(final String method) {
      if (method != null && method.matches(GET)) {
        this.method = method;
      } else {
        throw new IllegalArgumentException();
      }
      return this;
    }

    @Override
    public BatchQueryPartBuilder contentId(final String contentId) {
      this.contentId = contentId;
      return this;
    }

  }

}
