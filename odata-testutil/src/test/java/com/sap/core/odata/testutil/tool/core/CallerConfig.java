package com.sap.core.odata.testutil.tool.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class CallerConfig {

  // common settings
  private final URI baseUri;
  private CallerResultHandler responseHandler;
  private String proxy = null;
  private String basicAuthCredentials;

  //
  private List<TestPath> testPaths;

  public CallerConfig(String baseUrl, CallerResultHandler responseHandler, List<TestPath> testPaths) throws URISyntaxException {
    this(baseUrl);
    this.responseHandler = responseHandler;
    this.testPaths = testPaths;
  }

  public CallerConfig(String baseUrl) throws URISyntaxException {
    baseUri = new URI(baseUrl);
  }

  public static CallerConfig create(String baseUrl) throws URISyntaxException {
    return new CallerConfig(baseUrl);
  }

  public CallerConfig setProxy(String proxy) {
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

  public CallerConfig setResponseHandler(CallerResultHandler responseHandler) {
    this.responseHandler = responseHandler;
    return this;
  }

  public CallerResultHandler getResponseHandler() {
    return responseHandler;
  }

  public CallerConfig addTestPath(TestPath path) {
    testPaths.add(path);
    return this;
  }

  /**
   * @return the testPaths
   */
  public final List<TestPath> getTestPaths() {
    return Collections.unmodifiableList(testPaths);
  }

  public boolean isBasicAuthCredentialsSet() {
    return basicAuthCredentials != null;
  }

  public String getBasicAuthCredentials() {
    return basicAuthCredentials;
  }

  public CallerConfig setBasicAuthCredentials(String basicAuthCredentials) {
    this.basicAuthCredentials = basicAuthCredentials;
    return this;
  }

  public URI getBaseUri() {
    return baseUri;
  }
}
