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
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.testutil.helper.StringHelper;

public class SupaController {

  private static final Logger LOG = LoggerFactory.getLogger(SupaController.class);

  private static final String BEGIN = "/supa/startMeasurement";
  private static final String STOP = "/supa/stopMeasurement";
  private static final String NEXT_STEP = "/supa/nextStep";
  //  private static final String FIRST_STEP = "/supa/firstStep";
  private static final String CURRENT_STEP = "/supa/currentStep";
  //  private static final String RESET_DATA_PROVIDERS = "/supa/resetDataProviders";
  private static final String GENERATE_EXCEL = "/supa/generateExcel";
  private static final String GET_RESULT_DIR = "/supa/getResultDir";
  private static final String INDEX = "/supa/index";
  private static final String EXIT = "/supa/exit";
  //  private static final String GET_CURRENT_VALUES = "/supa/getCurrentValues";

  private final URI baseUri;

  protected DefaultHttpClient httpClient;
  protected CallerConfig config;

  public SupaController(final String baseUri) throws URISyntaxException {
    this(baseUri, false);
  }

  public SupaController(final String baseUri, final boolean validateServerStarted) throws URISyntaxException {
    if (baseUri.endsWith("/")) {
      config = new CallerConfig(baseUri.substring(0, baseUri.length() - 1));
    } else {
      config = new CallerConfig(baseUri);
    }
    this.baseUri = config.getBaseUri();

    init();
    if (validateServerStarted && !supaServerRunning()) {
      throw new IllegalStateException("No SUPA server is reachable at '" + this.baseUri.toASCIIString() + "'.");
    }
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
          new UsernamePasswordCredentials(config.getBasicAuthCredentials()));
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

  public boolean supaServerRunning() {
    return call(INDEX).matches("<html>.*</html>");
  }

  public void begin() {
    call(BEGIN);
  }

  public String stop() {
    return call(STOP);
  }

  public String getCurrentStepName() {
    return call(CURRENT_STEP);
  }

  public void nextStep() {
    call(NEXT_STEP);
  }

  public String finish() {
    call(GENERATE_EXCEL);
    return call(GET_RESULT_DIR);
  }

  public void shutdownSupaServer() {
    call(EXIT);
  }

  private String call(final String supaCommand) {
    HttpRequestBase request = null;
    try {
      request = new HttpGet(baseUri + supaCommand);

      LOG.debug("Call for request line '{}'", request.getRequestLine());

      final HttpHost targetHost = new HttpHost(baseUri.getHost(), baseUri.getPort());
      final HttpResponse response = httpClient.execute(targetHost, request);

      //
      return extractSupaInformation(response.getEntity());
    } catch (final IOException e) {
      LOG.debug("Got exception for calling supa command '" + supaCommand + "'.");
      return "Exception";
    } finally {
      if (request != null) {
        request.releaseConnection();
      }
    }
  }

  private String extractSupaInformation(final HttpEntity entity) throws IllegalStateException, IOException {
    String rawContent = StringHelper.httpEntityToString(entity);
    if (rawContent == null) {
      return null;
    }
    //
    int startIndex = 0;
    if (rawContent.startsWith("<html><body>")) {
      startIndex = 12;
    }
    //
    int endIndex = rawContent.indexOf("<br /><p />");
    if (endIndex < 0 || endIndex < startIndex) {
      endIndex = rawContent.length();
    }
    //
    String supaContent = rawContent.substring(startIndex, endIndex);
    return supaContent;
  }
}
