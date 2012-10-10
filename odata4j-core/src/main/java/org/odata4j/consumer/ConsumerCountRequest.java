package org.odata4j.consumer;

import org.odata4j.core.OCountRequest;
import org.odata4j.exceptions.ODataProducerException;

/**
 * Count-request implementation.
 */
public class ConsumerCountRequest implements OCountRequest {

  private ODataClient client;
  private String baseUri;
  private String entitySetName;
  private Integer top;

  public ConsumerCountRequest(ODataClient client, String serviceRootUri) {
    this.client = client;
    this.baseUri = serviceRootUri;
  }

  public ConsumerCountRequest entitySetName(String entitySetName) {
    this.entitySetName = entitySetName;
    return this;
  }

  public ConsumerCountRequest top(int top) {
    this.top = Integer.valueOf(top);
    return this;
  }

  public int execute() throws ODataProducerException {
    String uri = baseUri;

    if (entitySetName != null) {
      uri = uri + entitySetName + "/";
    }

    uri = uri + "$count";

    if (top != null) {
      uri = uri + "?$top=" + top;
    }

    ODataClientRequest request = ODataClientRequest.get(uri);
    String valueString = client.requestBody(client.getFormatType(), request);
    return Integer.parseInt(valueString);
  }

}
