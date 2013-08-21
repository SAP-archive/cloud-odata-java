package com.sap.core.odata.api.client.batch;

import java.util.Map;

import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * A BatchQueryPart
 * <p>BatchQueryPart represents a single retrieve request
 *
 * @author SAP AG
 */
public abstract class BatchQueryPart implements BatchPart {

  public abstract Map<String, String> getHeaders();

  public abstract String getUri();

  public abstract String getMethod();

  public abstract String getContentId();

  /**
   * @param headers
   * @return a new builder object
   */
  public static BatchQueryPartBuilder headers(final Map<String, String> headers) {
    return newBuilder().headers(headers);
  }

  /**
   * @param uri should not be null
   * @return a new builder object
   */
  public static BatchQueryPartBuilder uri(final String uri) {
    return newBuilder().uri(uri);
  }

  /**
   * @param method MUST be the HTTP GET method
   * @return a new builder object
   */
  public static BatchQueryPartBuilder method(final String method) {
    return newBuilder().method(method);
  }

  /**
   * @param contentId can be used to identify the different request within a the batch
   * @return a new builder object
   */
  public static BatchQueryPartBuilder contentId(final String contentId) {
    return newBuilder().contentId(contentId);
  }

  /**
   * @return returns a new builder object
   */
  public static BatchQueryPartBuilder newBuilder() {
    return BatchQueryPartBuilder.newInstance();
  }

  public static abstract class BatchQueryPartBuilder {

    protected BatchQueryPartBuilder() {}

    private static BatchQueryPartBuilder newInstance() {
      return RuntimeDelegate.createBatchQueryPartBuilder();
    }

    public abstract BatchQueryPart build();

    public abstract BatchQueryPartBuilder headers(Map<String, String> headers);

    public abstract BatchQueryPartBuilder uri(String uri);

    public abstract BatchQueryPartBuilder method(String method);

    public abstract BatchQueryPartBuilder contentId(String contentId);

  }
}
