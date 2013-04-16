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

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcceptHeaderCaller {

  private static final Logger LOG = LoggerFactory.getLogger(AcceptHeaderCaller.class);

  private DefaultHttpClient httpClient;
  private final CallerConfig config;

  AcceptHeaderCaller(final CallerConfig config) {
    this.config = config;
  }

  public static AcceptHeaderCaller create(final CallerConfig config) {
    return new AcceptHeaderCaller(config);
  }

  private void init() {
    httpClient = new DefaultHttpClient();
    if (config.isProxySet()) {
      initProxy();
    }

    if (config.isBasicAuthCredentialsSet()) {
      final URI baseUri = config.getBaseUri();
      httpClient.getCredentialsProvider().setCredentials(
          new AuthScope(baseUri.getHost(), baseUri.getPort()),
          new UsernamePasswordCredentials("anzeiger", "display"));//config.getBasicAuthCredentials()));
    }
  }

  private void initProxy() throws IllegalArgumentException {
    final String proxyUrl = config.getProxy();
    final String[] hostAndPort = proxyUrl.split(":");
    if (hostAndPort.length != 2) {
      throw new IllegalArgumentException("Unable to parse proxy url. Supported format is 'hostname:port''");
    }
    final String host = hostAndPort[0];
    int port;
    try {
      port = Integer.parseInt(hostAndPort[1]);
    } catch (final NumberFormatException e) {
      throw new IllegalArgumentException("Unable to parse proxy url because port is Not a Number. Supported format is 'hostname:port''", e);
    }
    final HttpHost proxy = new HttpHost(host, port);
    LOG.info("Use proxy '{}:{}'", proxy.getHostName(), proxy.getPort());
    httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
  }

  public void call() {
    init();

    for (final TestPath path : config.getTestPaths()) {
      call(config.getBaseUri(), path);
    }
  }

  private void call(final URI baseUri, final TestPath testPath) {
    HttpGet request = null;
    try {
      request = new HttpGet(baseUri.getPath() + testPath.getPath());
      testPath.applyHeaders(request);

      LOG.debug("Call for request line '{}'", request.getRequestLine());

      final HttpHost targetHost = new HttpHost(baseUri.getHost(), baseUri.getPort());
      final HttpResponse response = httpClient.execute(targetHost, request);

      //
      getHandler().handle(baseUri, testPath, request, response);
    } catch (final IOException e) {
      getHandler().handle(testPath, request, e);
      LOG.debug("Got exception for calling TestPath '" + testPath + "'.");
    } finally {
      if (request != null) {
        request.releaseConnection();
      }
    }
  }

  private CallerResultHandler getHandler() {
    return config.getResponseHandler();
  }
}
