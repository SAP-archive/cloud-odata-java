package org.odata4j.cxf.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.core4j.Enumerable;
import org.odata4j.consumer.AbstractODataClient;
import org.odata4j.consumer.ODataClientRequest;
import org.odata4j.consumer.ODataClientResponse;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.ODataConstants.Charsets;
import org.odata4j.core.ODataHttpMethod;
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

/**
 * OData client based on Apache's HTTP client implementation.
 */
public class ODataCxfClient extends AbstractODataClient {

  private final OClientBehavior[] requiredBehaviors = new OClientBehavior[] { OClientBehaviors.methodTunneling("MERGE") };
  private final OClientBehavior[] behaviors;

  private final HttpClient httpClient;

  public ODataCxfClient(FormatType type, OClientBehavior... behaviors) {
    super(type);
    this.behaviors = Enumerable.create(requiredBehaviors).concat(Enumerable.create(behaviors)).toArray(OClientBehavior.class);
    this.httpClient = new DefaultHttpClient();

    if (System.getProperties().containsKey("http.proxyHost") && System.getProperties().containsKey("http.proxyPort")) {
      // support proxy settings
      String hostName = System.getProperties().getProperty("http.proxyHost");
      String hostPort = System.getProperties().getProperty("http.proxyPort");

      HttpHost proxy = new HttpHost(hostName, Integer.parseInt(hostPort));
      this.httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }
  }

  public Reader getFeedReader(ODataClientResponse response) {
    HttpResponse httpResponse = ((CxfClientResponse) response).getHttpResponse();
    try {
      InputStream textEntity = httpResponse.getEntity().getContent();
      return new BOMWorkaroundReader(new InputStreamReader(textEntity, Charsets.Upper.UTF_8));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public String requestBody(FormatType formatType, ODataClientRequest request) throws ODataProducerException {
    ODataClientResponse response = doRequest(formatType, request, Status.OK);
    String string = entityToString(((CxfClientResponse) response).getHttpResponse().getEntity());
    response.close();
    return string;
  }

  @SuppressWarnings("unchecked")
  protected ODataClientResponse doRequest(FormatType reqType, ODataClientRequest request, StatusType... expectedResponseStatus) throws ODataProducerException {
    UriBuilder uriBuilder = UriBuilder.fromPath(request.getUrl());
    for (String key : request.getQueryParams().keySet())
      uriBuilder = uriBuilder.queryParam(key, request.getQueryParams().get(key));
    URI uri = uriBuilder.build();

    if (this.behaviors != null) {
      for (OClientBehavior behavior : behaviors)
        request = behavior.transform(request);
    }

    HttpUriRequest httpRequest = this.getRequestByMethod(request.getMethod(), uri);

    // maybe something better is needed here
    String acceptHeader = "";
    for (int i = 0; i < reqType.getAcceptableMediaTypes().length; i++) {
      acceptHeader += reqType.getAcceptableMediaTypes()[i];
      if (i < reqType.getAcceptableMediaTypes().length - 1)
        acceptHeader += ", ";
    }
    if (acceptHeader.length() > 0)
      httpRequest.addHeader(HttpHeaders.ACCEPT, acceptHeader);

    for (String header : request.getHeaders().keySet())
      httpRequest.addHeader(header, request.getHeaders().get(header));

    if (!request.getHeaders().containsKey(ODataConstants.Headers.USER_AGENT))
      httpRequest.addHeader(ODataConstants.Headers.USER_AGENT, "odata4j.org");

    if (request.getPayload() != null && httpRequest instanceof HttpEntityEnclosingRequest) {
      HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) httpRequest;

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
      String entityString = sw.toString();

      // allow the client to override the default format writer content-type
      String contentType = request.getHeaders().containsKey(ODataConstants.Headers.CONTENT_TYPE)
          ? request.getHeaders().get(ODataConstants.Headers.CONTENT_TYPE)
          : fw.getContentType();

      try {
        StringEntity entity = new StringEntity(entityString);

        entity.setContentType(contentType);

        entityRequest.setEntity(entity);
      } catch (UnsupportedEncodingException e) {
        Throwables.propagate(e);
      }
    }

    // execute request
    HttpResponse httpResponse = null;
    try {
      httpResponse = this.httpClient.execute(httpRequest);
    } catch (IOException e) {
      Throwables.propagate(e);
    }

    StatusType status = Status.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
    if (status == null) {
      final StatusLine statusLine = httpResponse.getStatusLine();
      status = new StatusType() {

        public int getStatusCode() {
          return statusLine.getStatusCode();
        }

        public Family getFamily() {
          return null;
        }

        public String getReasonPhrase() {
          return statusLine.getReasonPhrase();
        }
      };
    }
    for (StatusType expStatus : expectedResponseStatus)
      if (expStatus.getStatusCode() == status.getStatusCode())
        return new CxfClientResponse(httpResponse);

    // the server responded with an unexpected status
    RuntimeException exception;
    String textEntity = entityToString(httpResponse.getEntity()); // input stream can only be consumed once
    try {
      // report error as ODataProducerException in case we get a well-formed OData error...
      MediaType contentType = MediaType.valueOf(httpResponse.getEntity().getContentType().getValue());
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
    HttpResponse httpResponse = ((CxfClientResponse) response).getHttpResponse();
    try {
      InputStream textEntity = httpResponse.getEntity().getContent();
      return StaxUtil.newXMLEventReader(new BOMWorkaroundReader(new InputStreamReader(textEntity, Charsets.Upper.UTF_8)));
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  private HttpUriRequest getRequestByMethod(String method, URI uri) {
    switch (ODataHttpMethod.fromString(method)) {
    case GET:
      return new HttpGet(uri);
    case PUT:
      return new HttpPut(uri);
    case POST:
      return new HttpPost(uri);
    case DELETE:
      return new HttpDelete(uri);
    case OPTIONS:
      return new HttpOptions(uri);
    case HEAD:
      return new HttpHead(uri);
    default:
      throw new RuntimeException("Method unknown: " + method);
    }
  }

  private String entityToString(HttpEntity entity) {
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), Charsets.Upper.UTF_8));
      StringBuilder stringBuilder = new StringBuilder();
      String line = null;

      while ((line = bufferedReader.readLine()) != null)
        stringBuilder.append(line);

      bufferedReader.close();
      return stringBuilder.toString();
    } catch (IOException e) {
      Throwables.propagate(e);
      return null;
    }
  }
}
