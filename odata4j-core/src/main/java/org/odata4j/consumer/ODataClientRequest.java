package org.odata4j.consumer;

import java.util.HashMap;
import java.util.Map;

import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.format.Entry;
import org.odata4j.format.SingleLink;

/**
 * Generic OData http request builder used by the low-level {@link ODataClient} api.
 *
 * <p>Also interesting for developers of custom {@link OClientBehavior} implementations.</p>
 *
 * @see ODataClient
 */
public class ODataClientRequest {

  private final String method;
  private final String url;
  private final Map<String, String> headers;
  private final Map<String, String> queryParams;
  private final Object payload;

  public ODataClientRequest(String method, String url, Map<String, String> headers, Map<String, String> queryParams, Object payload) {
    this.method = method;
    this.url = url;
    this.headers = headers == null ? new HashMap<String, String>() : headers;
    this.queryParams = queryParams == null ? new HashMap<String, String>() : queryParams;
    this.payload = payload;
  }

  /**
   * Gets the request http method.
   *
   * @return the http method
   */
  public String getMethod() {
    return method;
  }

  /**
   * Gets the request url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Gets the request http headers.
   *
   * @return the headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Gets the request query parameters.
   *
   * @return the query parameters
   */
  public Map<String, String> getQueryParams() {
    return queryParams;
  }

  /**
   * Gets the normalized OData payload.
   *
   * @return the normalized OData payload
   */
  public Object getPayload() {
    return payload;
  }

  /**
   * Creates a new GET request.
   *
   * @param url  the request url
   * @return a new request builder
   */
  public static ODataClientRequest get(String url) {
    return new ODataClientRequest("GET", url, null, null, null);
  }

  /**
   * Creates a new POST request.
   *
   * @param url  the request url
   * @param entry  the normalized OData payload
   * @return a new request builder
   */
  public static ODataClientRequest post(String url, Entry entry) {
    return new ODataClientRequest("POST", url, null, null, entry);
  }

  /**
   * Creates a new POST request.
   *
   * @param url  the request url
   * @param link  the link
   * @return a new request builder
   */
  public static ODataClientRequest post(String url, SingleLink link) {
    return new ODataClientRequest("POST", url, null, null, link);
  }

  /**
   * Creates a new PUT request.
   *
   * @param url  the request url
   * @param entry  the normalized OData payload
   * @return a new request builder
   */
  public static ODataClientRequest put(String url, Entry entry) {
    return new ODataClientRequest("PUT", url, null, null, entry);
  }

  /**
   * Creates a new PUT request.
   *
   * @param url  the request url
   * @param link  the link
   * @return a new request builder
   */
  public static ODataClientRequest put(String url, SingleLink link) {
    return new ODataClientRequest("PUT", url, null, null, link);
  }

  /**
   * Creates a new MERGE request.
   *
   * @param url  the request url
   * @param entry  the normalized OData payload
   * @return a new request builder
   */
  public static ODataClientRequest merge(String url, Entry entry) {
    return new ODataClientRequest("MERGE", url, null, null, entry);
  }

  /**
   * Creates a new MERGE request.
   *
   * @param url  the request url
   * @param link  the link
   * @return a new request builder
   */
  public static ODataClientRequest merge(String url, SingleLink link) {
    return new ODataClientRequest("MERGE", url, null, null, link);
  }

  /**
   * Creates a new DELETE request.
   *
   * @param url  the request url
   * @return a new request builder
   */
  public static ODataClientRequest delete(String url) {
    return new ODataClientRequest("DELETE", url, null, null, null);
  }

  /**
   * Sets a request query parameter.
   *
   * @param name  the query parameter name
   * @param value  the query parameter value
   * @return the request builder
   */
  public ODataClientRequest queryParam(String name, String value) {
    this.getQueryParams().put(name, value);
    return new ODataClientRequest(this.getMethod(), this.getUrl(), this.getHeaders(), this.getQueryParams(), this.getPayload());
  }

  /**
   * Sets an http request header.
   *
   * @param name  the header name
   * @param value  the header value
   * @return the request builder
   */
  public ODataClientRequest header(String name, String value) {
    headers.put(name, value);
    return new ODataClientRequest(method, url, headers, queryParams, payload);
  }

  /**
   * Sets the http request method.
   *
   * @param method  the method
   * @return the request builder
   */
  public ODataClientRequest method(String method) {
    return new ODataClientRequest(method, url, headers, queryParams, payload);
  }
}
