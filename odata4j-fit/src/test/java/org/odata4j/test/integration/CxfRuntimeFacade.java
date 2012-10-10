package org.odata4j.test.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.core.ODataConstants.Headers;
import org.odata4j.core.ODataHttpMethod;
import org.odata4j.core.Throwables;
import org.odata4j.cxf.consumer.ODataCxfConsumer;
import org.odata4j.cxf.consumer.ODataCxfConsumer.Builder;
import org.odata4j.cxf.producer.server.ODataCxfServer;
import org.odata4j.format.FormatType;
import org.odata4j.producer.resources.DefaultODataApplication;
import org.odata4j.producer.resources.RootApplication;
import org.odata4j.producer.server.ODataServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CxfRuntimeFacade implements RuntimeFacade {

  private static final Logger LOGGER = LoggerFactory.getLogger(CxfRuntimeFacade.class);

  @Override
  public void hostODataServer(String baseUri) {
    try {
      ODataServer server = this.startODataServer(baseUri);
      System.out.println("Press any key to exit");
      new BufferedReader(new InputStreamReader(System.in)).readLine();
      server.stop();
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public ODataServer startODataServer(String baseUri) {
    return this.createODataServer(baseUri).start();
  }

  @Override
  public ODataServer createODataServer(String baseUri) {
    return new ODataCxfServer(baseUri, DefaultODataApplication.class, RootApplication.class);
  }

  @Override
  public ODataConsumer createODataConsumer(String endpointUri, FormatType format, OClientBehavior... clientBehaviors) {
    Builder builder = ODataCxfConsumer.newBuilder(endpointUri);

    if (format != null) {
      builder = builder.setFormatType(format);
    }

    if (clientBehaviors != null) {
      builder = builder.setClientBehaviors(clientBehaviors);
    }

    return builder.build();
  }

  @Override
  public ResponseData acceptAndReturn(String uri, MediaType mediaType) {
    uri = uri.replace(" ", "%20");
    return this.getResource(ODataHttpMethod.GET, uri, null, mediaType, null);
  }

  @Override
  public ResponseData getWebResource(String uri, String accept) {
    uri = uri.replace(" ", "%20");
    Hashtable<String, Object> header = new Hashtable<String, Object>();
    header.put("accept", accept);
    return this.getResource(ODataHttpMethod.GET, uri, null, null, header);
  }

  @Override
  public void accept(String uri, MediaType mediaType) {
    // no effect???
  }

  @Override
  public ResponseData getWebResource(String uri) {
    return this.getResource(ODataHttpMethod.GET, uri, null, null, null);
  }

  private ResponseData getResource(ODataHttpMethod method, String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    String resource = "";
    try {
      HttpClient httpClient = new DefaultHttpClient();

      if (System.getProperties().containsKey("http.proxyHost") && System.getProperties().containsKey("http.proxyPort")) {
        // support proxy settings
        String hostName = System.getProperties().getProperty("http.proxyHost");
        String hostPort = System.getProperties().getProperty("http.proxyPort");

        HttpHost proxy = new HttpHost(hostName, Integer.parseInt(hostPort));
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
      }

      HttpUriRequest httpRequest;

      switch (method) {
      case GET:
        httpRequest = new HttpGet(uri);
        break;
      case DELETE:
        httpRequest = new HttpDelete(uri);
        break;
      case PATCH:
        HttpPost patch = new HttpPost(uri);
        if (content != null)
          patch.setEntity(new InputStreamEntity(content, -1));
        patch.setHeader(Headers.X_HTTP_METHOD, "PATCH");
        httpRequest = patch;
        break;
      case MERGE:
        HttpPost merge = new HttpPost(uri);
        if (content != null)
          merge.setEntity(new InputStreamEntity(content, -1));
        merge.setHeader(Headers.X_HTTP_METHOD, "MERGE");
        httpRequest = merge;
        break;
      case PUT:
        HttpPut put = new HttpPut(uri);
        if (content != null)
          put.setEntity(new InputStreamEntity(content, -1));
        httpRequest = put;
        break;
      case POST:
        HttpPost post = new HttpPost(uri);
        if (content != null)
          post.setEntity(new InputStreamEntity(content, -1));
        httpRequest = post;
        break;
      default:
        throw new RuntimeException("Unsupported method: " + method);
      }

      if (headers != null) {
        for (String key : headers.keySet()) {
          String value = (String) headers.get(key).toString();
          Header header = new BasicHeader(key, value);
          httpRequest.addHeader(header);
        }
      }
      if (mediaType != null) {
        if (content != null) {
          httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        }
        if (method == ODataHttpMethod.GET)
        {
          httpRequest.addHeader(HttpHeaders.ACCEPT, mediaType.toString());
        }
      }

      // Execute the request
      HttpResponse response = httpClient.execute(httpRequest);
      // Examine the response status
      CxfRuntimeFacade.LOGGER.debug(response.getStatusLine().toString());
      // Get hold of the response entity
      HttpEntity entity = response.getEntity();
      // If the response does not enclose an entity, there is no need
      // to worry about connection release
      if (entity != null) {
        resource = EntityUtils.toString(entity);
      }
      return new ResponseData(response.getStatusLine().getStatusCode(), resource);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public ResponseData postWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.getResource(ODataHttpMethod.POST, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData putWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.getResource(ODataHttpMethod.PUT, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData mergeWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.getResource(ODataHttpMethod.MERGE, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData patchWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.getResource(ODataHttpMethod.PATCH, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData getWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.getResource(ODataHttpMethod.GET, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData deleteWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.getResource(ODataHttpMethod.DELETE, uri, content, mediaType, headers);
  }
}
