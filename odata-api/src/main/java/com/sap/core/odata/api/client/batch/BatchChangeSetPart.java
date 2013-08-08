package com.sap.core.odata.api.client.batch;

import java.util.Map;

import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * A BatchChangeSetPart
 * <p> BatchChangeSetPart represents a change request within a Change Set
 *
 * @author SAP AG
 */
public abstract class BatchChangeSetPart {

  public abstract Map<String, String> getHeaders();

  public abstract String getBody();

  public abstract String getUri();

  public abstract String getMethod();

  public abstract String getContentId();

  /**
   * @param headers
   * @return a new builder object
   */
  public static BatchChangeSetPartBuilder headers(final Map<String, String> headers) {
    return newBuilder().headers(headers);
  }

  /**
   * @param body a change request body
   * @return a new builder object
   */
  public static BatchChangeSetPartBuilder body(final String body) {
    return newBuilder().body(body);
  }

  /**
   * @param uri should not be null
   * @return a new builder object
   */
  public static BatchChangeSetPartBuilder uri(final String uri) {
    return newBuilder().uri(uri);
  }

  /**
   * @param method MUST be the PUT, POST, MERGE, DELETE or PATCH method
   * @return a new builder object
   */
  public static BatchChangeSetPartBuilder method(final String method) {
    return newBuilder().method(method);
  }

  /**
   * @param contentId can be used to identify the different request within a the batch
   * @return a new builder object
   */
  public static BatchChangeSetPartBuilder contentId(final String contentId) {
    return newBuilder().contentId(contentId);
  }

  /**
   * @return returns a new builder object
   */
  public static BatchChangeSetPartBuilder newBuilder() {
    return BatchChangeSetPartBuilder.newInstance();
  }

  public static abstract class BatchChangeSetPartBuilder {

    protected BatchChangeSetPartBuilder() {}

    private static BatchChangeSetPartBuilder newInstance() {
      return RuntimeDelegate.createBatchChangeSetPartBuilder();
    }

    public abstract BatchChangeSetPart build();

    public abstract BatchChangeSetPartBuilder headers(Map<String, String> headers);

    public abstract BatchChangeSetPartBuilder body(String body);

    public abstract BatchChangeSetPartBuilder uri(String uri);

    public abstract BatchChangeSetPartBuilder method(String method);

    public abstract BatchChangeSetPartBuilder contentId(String contentId);

  }

}
