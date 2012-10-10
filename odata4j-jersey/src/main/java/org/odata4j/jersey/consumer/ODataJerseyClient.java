package org.odata4j.jersey.consumer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.StatusType;

import org.core4j.Enumerable;
import org.core4j.xml.XDocument;
import org.core4j.xml.XmlFormat;
import org.odata4j.consumer.AbstractODataClient;
import org.odata4j.consumer.ODataClientRequest;
import org.odata4j.consumer.ODataClientResponse;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.ODataConstants.Charsets;
import org.odata4j.core.OError;
import org.odata4j.core.Throwables;
import org.odata4j.exceptions.ODataProducerException;
import org.odata4j.exceptions.ODataProducerExceptions;
import org.odata4j.format.Entry;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.FormatType;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.format.SingleLink;
import org.odata4j.internal.BOMWorkaroundReader;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.util.StaxUtil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.PartialRequestBuilder;
import com.sun.jersey.api.client.WebResource;

/**
 * OData client based on Jersey.
 */
class ODataJerseyClient extends AbstractODataClient {

  private final OClientBehavior[] requiredBehaviors = new OClientBehavior[] { OClientBehaviors.methodTunneling("MERGE") }; // jersey hates MERGE, tunnel through POST
  private final OClientBehavior[] behaviors;

  private final Client client;

  public ODataJerseyClient(FormatType type, JerseyClientFactory clientFactory, OClientBehavior... behaviors) {
    super(type);
    this.behaviors = Enumerable.create(requiredBehaviors).concat(Enumerable.create(behaviors)).toArray(OClientBehavior.class);
    this.client = JerseyClientUtil.newClient(clientFactory, behaviors);
  }

  public Reader getFeedReader(ODataClientResponse response) {
    ClientResponse clientResponse = ((JerseyClientResponse) response).getClientResponse();
    if (ODataConsumer.dump.responseBody()) {
      String textEntity = clientResponse.getEntity(String.class);
      dumpResponseBody(textEntity, clientResponse.getType());
      return new BOMWorkaroundReader(new StringReader(textEntity));
    }

    InputStream textEntity = clientResponse.getEntityInputStream();
    try {
      return new BOMWorkaroundReader(new InputStreamReader(textEntity, Charsets.Upper.UTF_8));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public String requestBody(FormatType formatType, ODataClientRequest request) throws ODataProducerException {
    ODataClientResponse response = doRequest(formatType, request, Status.OK);
    String entity = ((JerseyClientResponse) response).getClientResponse().getEntity(String.class);
    response.close();
    return entity;
  }

  @SuppressWarnings("unchecked")
  protected ODataClientResponse doRequest(FormatType reqType, ODataClientRequest request, StatusType... expectedResponseStatus) throws ODataProducerException {

    if (behaviors != null) {
      for (OClientBehavior behavior : behaviors)
        request = behavior.transform(request);
    }

    WebResource webResource = JerseyClientUtil.resource(client, request.getUrl(), behaviors);

    // set query params
    for (String qpn : request.getQueryParams().keySet())
      webResource = webResource.queryParam(qpn, request.getQueryParams().get(qpn));

    WebResource.Builder b = webResource.getRequestBuilder();

    // set headers
    b = b.accept(reqType.getAcceptableMediaTypes());

    for (String header : request.getHeaders().keySet())
      b.header(header, request.getHeaders().get(header));
    if (!request.getHeaders().containsKey(ODataConstants.Headers.USER_AGENT))
      b.header(ODataConstants.Headers.USER_AGENT, "odata4j.org");

    if (ODataConsumer.dump.requestHeaders())
      dumpHeaders(request, webResource, b);

    // request body
    if (request.getPayload() != null) {

      Class<?> payloadClass;
      if (request.getPayload() instanceof Entry)
        payloadClass = Entry.class;
      else if (request.getPayload() instanceof SingleLink)
        payloadClass = SingleLink.class;
      else
        throw new IllegalArgumentException("Unsupported payload: " + request.getPayload());

      StringWriter sw = new StringWriter();
      FormatWriter<Object> fw = (FormatWriter<Object>)
          FormatWriterFactory.getFormatWriter(payloadClass, null, this.getFormatType().toString(), null);
      fw.write(null, sw, request.getPayload());

      String entity = sw.toString();
      if (ODataConsumer.dump.requestBody())
        dump(entity);

      // allow the client to override the default format writer content-type
      String contentType = request.getHeaders().containsKey(ODataConstants.Headers.CONTENT_TYPE)
          ? request.getHeaders().get(ODataConstants.Headers.CONTENT_TYPE)
          : fw.getContentType();

      b.entity(entity, contentType);
    }

    // execute request
    ClientResponse response = null;
    try {
      response = b.method(request.getMethod(), ClientResponse.class);
    } catch (ClientHandlerException e) {
      Throwables.propagate(e);
    }

    if (ODataConsumer.dump.responseHeaders())
      dumpHeaders(response);
    StatusType status = response.getClientResponseStatus();
    for (StatusType expStatus : expectedResponseStatus)
      if (expStatus.getStatusCode() == status.getStatusCode())
        return new JerseyClientResponse(response);

    // the server responded with an unexpected status
    RuntimeException exception;
    String textEntity = response.getEntity(String.class); // input stream can only be consumed once
    try {
      // report error as ODataProducerException in case we get a well-formed OData error...
      MediaType contentType = response.getType();
      OError error = FormatParserFactory.getParser(OError.class, contentType, null).parse(new StringReader(textEntity));
      exception = ODataProducerExceptions.create(status, error);
    } catch (RuntimeException e) {
      // ... otherwise throw a RuntimeError
      exception = new RuntimeException(String.format("Expected status %s, found %s. Server response:",
          Enumerable.create(expectedResponseStatus).join(" or "), status) + "\n" + textEntity, e);
    }
    throw exception;
  }

  protected XMLEventReader2 toXml(ODataClientResponse response) {
    ClientResponse clientResponse = ((JerseyClientResponse) response).getClientResponse();

    if (ODataConsumer.dump.responseBody()) {
      String textEntity = clientResponse.getEntity(String.class);
      dumpResponseBody(textEntity, clientResponse.getType());
      return StaxUtil.newXMLEventReader(new BOMWorkaroundReader(new StringReader(textEntity)));
    }

    InputStream textEntity = clientResponse.getEntityInputStream();
    try {
      return StaxUtil.newXMLEventReader(new BOMWorkaroundReader(new InputStreamReader(textEntity, Charsets.Upper.UTF_8)));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  private void dumpResponseBody(String textEntity, MediaType type) {
    String logXml = textEntity;
    if (type.toString().contains("xml") || logXml != null && logXml.startsWith("<feed")) {
      try {
        logXml = XDocument.parse(logXml).toString(XmlFormat.INDENTED);
      } catch (Exception ignore) {}
    }
    dump(logXml);
  }

  private void dumpHeaders(ClientResponse response) {
    dump("Status: " + response.getStatus());
    dump(response.getHeaders());
  }

  private static boolean dontTryRequestHeaders;

  @SuppressWarnings("unchecked")
  private MultivaluedMap<String, Object> getRequestHeaders(WebResource.Builder b) {
    if (dontTryRequestHeaders)
      return null;

    //  protected MultivaluedMap<String, Object> metadata;
    try {
      Field f = PartialRequestBuilder.class.getDeclaredField("metadata");
      f.setAccessible(true);
      return (MultivaluedMap<String, Object>) f.get(b);
    } catch (Exception e) {
      dontTryRequestHeaders = true;
      return null;
    }

  }

  private void dumpHeaders(ODataClientRequest request, WebResource webResource, WebResource.Builder b) {
    dump(request.getMethod() + " " + webResource);
    dump(getRequestHeaders(b));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void dump(MultivaluedMap headers) {
    if (headers == null)
      return;

    for (Object header : headers.keySet())
      dump(header + ": " + headers.getFirst(header));
  }

  private static void dump(String message) {
    System.out.println(message);
  }

}
