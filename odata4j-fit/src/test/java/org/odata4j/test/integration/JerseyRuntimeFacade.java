package org.odata4j.test.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.core.ODataConstants.Headers;
import org.odata4j.core.ODataHttpMethod;
import org.odata4j.core.Throwables;
import org.odata4j.format.FormatType;
import org.odata4j.jersey.consumer.ODataJerseyConsumer;
import org.odata4j.jersey.consumer.ODataJerseyConsumer.Builder;
import org.odata4j.jersey.producer.server.ODataJerseyServer;
import org.odata4j.producer.resources.DefaultODataApplication;
import org.odata4j.producer.resources.RootApplication;
import org.odata4j.producer.server.ODataServer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.filter.LoggingFilter;

public class JerseyRuntimeFacade implements RuntimeFacade {

  @Override
  public void hostODataServer(String baseUri) {
    try {
      ODataServer server = startODataServer(baseUri);
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

    return new ODataJerseyServer(baseUri, DefaultODataApplication.class, RootApplication.class)
        .addJerseyRequestFilter(LoggingFilter.class); // log all requests
  }

  @Override
  public ODataConsumer createODataConsumer(String endpointUri, FormatType format, OClientBehavior... clientBehaviors) {
    Builder builder = ODataJerseyConsumer.newBuilder(endpointUri);

    if (format != null) {
      builder = builder.setFormatType(format);
    }

    if (clientBehaviors != null) {
      builder = builder.setClientBehaviors(clientBehaviors);
    }

    return builder.build();
  }

  @Override
  public ResponseData getWebResource(String uri) {
    WebResource webResource = new Client().resource(uri);

    ClientResponse response = webResource.get(ClientResponse.class);
    return new ResponseData(response.getStatus(), response.getEntity(String.class));
  }

  @Override
  public ResponseData postWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.requestWebResource(ODataHttpMethod.POST, uri, content, mediaType, headers);
  }

  public ResponseData putWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.requestWebResource(ODataHttpMethod.PUT, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData acceptAndReturn(String uri, MediaType mediaType) {
    uri = uri.replace(" ", "%20");

    WebResource webResource = new Client().resource(uri);

    ClientResponse response = webResource.accept(mediaType).get(ClientResponse.class);
    String body = response.getEntity(String.class);

    return new ResponseData(response.getStatus(), body);
  }

  @Override
  public ResponseData getWebResource(String uri, String accept) {
    WebResource webResource = new Client().resource(uri);

    ClientResponse response = webResource.accept(accept).get(ClientResponse.class);
    String body = response.getEntity(String.class);

    return new ResponseData(response.getStatus(), body);
  }

  @Override
  public void accept(String uri, MediaType mediaType) {
    uri = uri.replace(" ", "%20");
    WebResource webResource = new Client().resource(uri);
    webResource.accept(mediaType);
  }

  @Override
  public ResponseData mergeWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.requestWebResource(ODataHttpMethod.MERGE, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData patchWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.requestWebResource(ODataHttpMethod.PATCH, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData getWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.requestWebResource(ODataHttpMethod.GET, uri, content, mediaType, headers);
  }

  @Override
  public ResponseData deleteWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    return this.requestWebResource(ODataHttpMethod.DELETE, uri, content, mediaType, headers);
  }

  private ResponseData requestWebResource(ODataHttpMethod method, String uri, InputStream content, MediaType mediaType, Map<String, Object> headers) {
    Client client = new Client();
    WebResource.Builder webResource = client.resource(uri).type(mediaType);
    int statusCode;
    String entity = "";
    try {
      if (headers != null) {
        for (Entry<String, Object> entry : headers.entrySet()) {
          webResource = webResource.header(entry.getKey(), entry.getValue());
        }
      }

      ClientResponse response;
      switch (method) {
      case DELETE:
        response = webResource.delete(ClientResponse.class, content);
        break;
      case PATCH:
        webResource = webResource.header(Headers.X_HTTP_METHOD, "PATCH");
        response = webResource.post(ClientResponse.class, content);
        break;
      case GET:
        response = webResource.get(ClientResponse.class);
        break;
      case MERGE:
        webResource = webResource.header(Headers.X_HTTP_METHOD, "MERGE");
        response = webResource.post(ClientResponse.class, content);
        break;
      case POST:
        response = webResource.post(ClientResponse.class, content);
        break;
      case PUT:
        response = webResource.put(ClientResponse.class, content);
        break;
      default:
        throw new RuntimeException("Unsupported http method: " + method);
      }

      statusCode = response.getStatus();
      entity = response.getEntity(String.class);
    } catch (UniformInterfaceException ex) {
      statusCode = ex.getResponse().getStatus();
    }
    return new ResponseData(statusCode, entity);
  }

}
