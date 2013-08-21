package com.sap.core.odata.core.batch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.client.batch.BatchChangeSetPart;

public class BatchChangeSetPartImpl extends BatchChangeSetPart {
  private String method;
  private Map<String, String> headers = new HashMap<String, String>();
  private String body;
  private String uri;
  public String contentId;
  private static final String CHANGE_METHODS = "(PUT|POST|DELETE|MERGE|PATCH)";

  @Override
  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(headers);
  }

  @Override
  public String getBody() {
    return body;
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

  public class BatchChangeSetRequestBuilderImpl extends BatchChangeSetPartBuilder {
    private String method;
    private Map<String, String> headers = new HashMap<String, String>();
    private String body;
    private String uri;
    private String contentId;

    @Override
    public BatchChangeSetPart build() {
      if (method == null || uri == null) {
        throw new IllegalArgumentException();
      }
      BatchChangeSetPartImpl.this.method = method;
      BatchChangeSetPartImpl.this.headers = headers;
      BatchChangeSetPartImpl.this.body = body;
      BatchChangeSetPartImpl.this.uri = uri;
      BatchChangeSetPartImpl.this.contentId = contentId;
      return BatchChangeSetPartImpl.this;
    }

    @Override
    public BatchChangeSetPartBuilder headers(final Map<String, String> headers) {
      this.headers = headers;
      return this;
    }

    @Override
    public BatchChangeSetPartBuilder body(final String body) {
      this.body = body;
      return this;
    }

    @Override
    public BatchChangeSetPartBuilder uri(final String uri) {
      this.uri = uri;
      return this;
    }

    @Override
    public BatchChangeSetPartBuilder method(final String method) {
      if (method != null && method.matches(CHANGE_METHODS)) {
        this.method = method;
      } else {
        throw new IllegalArgumentException();
      }
      return this;
    }

    @Override
    public BatchChangeSetPartBuilder contentId(final String contentId) {
      this.contentId = contentId;
      return this;
    }

  }

}
