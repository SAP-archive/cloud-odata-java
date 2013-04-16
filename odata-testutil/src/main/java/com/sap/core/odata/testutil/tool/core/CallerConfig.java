/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

  public CallerConfig(final String baseUrl, final CallerResultHandler responseHandler, final List<TestPath> testPaths) throws URISyntaxException {
    this(baseUrl);
    this.responseHandler = responseHandler;
    this.testPaths = testPaths;
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

  public CallerConfig setResponseHandler(final CallerResultHandler responseHandler) {
    this.responseHandler = responseHandler;
    return this;
  }

  public CallerResultHandler getResponseHandler() {
    return responseHandler;
  }

  public CallerConfig addTestPath(final TestPath path) {
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

  public CallerConfig setBasicAuthCredentials(final String basicAuthCredentials) {
    this.basicAuthCredentials = basicAuthCredentials;
    return this;
  }

  public URI getBaseUri() {
    return baseUri;
  }
}
