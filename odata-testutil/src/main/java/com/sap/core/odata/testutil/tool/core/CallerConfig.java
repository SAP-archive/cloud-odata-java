package com.sap.core.odata.testutil.tool.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class CallerConfig {

  // connection settings
  private final URI baseUri;
  private String proxy = null;
  private String basicAuthCredentials;
  // test settings
  private TestResponseHandler responseHandler;
  private List<TestRequest> testRequests;

  public CallerConfig(final String baseUrl, final List<TestRequest> testRequests) throws URISyntaxException {
    this(baseUrl, null, testRequests);
  }

  public CallerConfig(final String baseUrl, final TestResponseHandler responseHandler, final List<TestRequest> testPaths) throws URISyntaxException {
    this(baseUrl);
    this.responseHandler = responseHandler;
    testRequests = testPaths;
  }

  public CallerConfig(final String baseUrl) throws URISyntaxException {
    baseUri = new URI(baseUrl);
  }

  public static CallerConfig create(final String baseUrl) throws URISyntaxException {
    return new CallerConfig(baseUrl);
  }

  public CallerConfig setProxy(final String proxy) {
    this.proxy = proxy;
    return this;
  }

  public String getProxy() {
    return proxy;
  }

  public boolean isProxySet() {
    return proxy != null;
  }

  /**
   * @return the baseUrl
   */
  public final String getBaseUrl() {
    return baseUri.toString();
  }

  public CallerConfig setResponseHandler(final TestResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
    return this;
  }

  public TestResponseHandler getResponseHandler() {
    return responseHandler;
  }

  public CallerConfig addTestRequest(final TestRequest path) {
    testRequests.add(path);
    return this;
  }

  /**
   * @return the testPaths
   */
  public final List<TestRequest> getTestRequests() {
    return Collections.unmodifiableList(testRequests);
  }

  public boolean isBasicAuthCredentialsSet() {
    return basicAuthCredentials != null;
  }

  public String getBasicAuthCredentials() {
    return basicAuthCredentials;
  }

  public CallerConfig setBasicAuthCredentials(final String basicAuthCredentials) {
    this.basicAuthCredentials = basicAuthCredentials;
    return this;
  }

  public URI getBaseUri() {
    return baseUri;
  }
}
