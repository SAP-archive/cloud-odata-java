package org.odata4j.test.integration;

import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.format.FormatType;
import org.odata4j.producer.server.ODataServer;

public interface RuntimeFacade {

  public void hostODataServer(String baseUri);

  public ODataServer startODataServer(String baseUri);

  public ODataServer createODataServer(String baseUri);

  public ODataConsumer createODataConsumer(String endpointUri, FormatType format, OClientBehavior... clientBehaviors);

  public ResponseData acceptAndReturn(String uri, MediaType mediaType);

  public void accept(String uri, MediaType mediaType);

  public ResponseData getWebResource(String uri, String accept);

  public ResponseData getWebResource(String uri);

  public ResponseData postWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers);

  public ResponseData putWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers);

  public ResponseData mergeWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers);

  public ResponseData patchWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers);

  public ResponseData getWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers);

  public ResponseData deleteWebResource(String uri, InputStream content, MediaType mediaType, Map<String, Object> headers);

}
