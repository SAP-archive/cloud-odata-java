package com.sap.core.odata.api.client.batch;

import java.util.Map;
import java.util.Set;

/**
* A BatchSingleResponse
* <p> BatchSingleResponse represents a single response of a Batch Response body. It can be a response to a change request of ChangeSet or a response to a retrieve request
* @author SAP AG
*/
public interface BatchSingleResponse {
  /**
   * @return a result code of the attempt to understand and satisfy the request
   */
  public String getStatusCode();

  /**
   * @return a short textual description of the status code
   */
  public String getStatusInfo();

  /**
   * @return a value of the Content-Id header
   */
  public String getContentId();

  /**
   * @return a body part of a response message
   */
  public String getBody();

  /**
   * @return all available headers
   */
  public Map<String, String> getHeaders();

  /**
   * @param name HTTP response header name
   * @return a header value or null if not set
   */
  public String getHeader(final String name);

  /**
   * @return a set of all available header names
   */
  public Set<String> getHeaderNames();

}
