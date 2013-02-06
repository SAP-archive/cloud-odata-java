package com.sap.core.odata.testutil.tool;

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
  
  AcceptHeaderCaller(CallerConfig config) {
    this.config = config;
  }

  public static AcceptHeaderCaller create(CallerConfig config) {
    return new AcceptHeaderCaller(config);
  }
  
  private void init() {
    httpClient = new DefaultHttpClient();
    if (config.isProxySet()) {
      initProxy();
    }
    
    if(config.isBasicAuthCredentialsSet()) {
      URI baseUri = config.getBaseUri();
      httpClient.getCredentialsProvider().setCredentials(
          new AuthScope(baseUri.getHost(), baseUri.getPort()),
          new UsernamePasswordCredentials("anzeiger", "display"));//config.getBasicAuthCredentials()));
    }
  }

  private void initProxy() throws IllegalArgumentException {
    String proxyUrl = config.getProxy();
    String[] hostAndPort = proxyUrl.split(":");
    if(hostAndPort.length != 2) {
      throw new IllegalArgumentException("Unable to parse proxy url. Supported format is 'hostname:port''");
    }
    String host = hostAndPort[0];
    int port;
    try {
      port = Integer.parseInt(hostAndPort[1]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Unable to parse proxy url because port is Not a Number. Supported format is 'hostname:port''");        
    }
    HttpHost proxy = new HttpHost(host, port);
    LOG.info("Use proxy '{}:{}'", proxy.getHostName(), proxy.getPort());
    httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
  }
  
  public void call() {
    init();

    for (TestPath path: config.getTestPaths()) {
      call(config.getBaseUri(), path);
    }
  }
  
  private void call(URI baseUri, TestPath testPath) {
    HttpGet request = null;
    try {
      request = new HttpGet(baseUri.getPath() + testPath.getPath());
      testPath.applyHeaders(request);

      LOG.debug("Call for request line '{}'", request.getRequestLine());

      HttpHost targetHost = new HttpHost(baseUri.getHost(), baseUri.getPort());
      HttpResponse response = httpClient.execute(targetHost, request);

      //
      getHandler().handle(baseUri, testPath, request, response);
    } catch (IOException e) {
      getHandler().handle(testPath, request, e);
      LOG.debug("Got exception for calling TestPath '" + testPath + "'.");
    } finally {
      if(request != null) {
        request.releaseConnection();
      }
    }
  }
  
  private CallerResultHandler getHandler() {
    return config.getResponseHandler();
  }
}
