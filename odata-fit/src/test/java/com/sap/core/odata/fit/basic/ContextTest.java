package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;

/**
 * @author SAP AG
 */
public class ContextTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    final ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void checkContextExists() throws ClientProtocolException, IOException, ODataException {
    assertNull(getService().getProcessor().getContext());
    final HttpResponse response = executeGetRequest("$metadata");

    final ODataContext context = getService().getProcessor().getContext();
    assertNotNull(context);

    final ODataService service = context.getService();
    assertNotNull(service);

    assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    assertEquals("$metadata", context.getPathInfo().getODataSegments().get(0).getPath());
  }

  @Test
  public void checkBaseUriForServiceDocument() throws ClientProtocolException, IOException, ODataException {
    executeGetRequest("");

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);
    assertEquals(getEndpoint().toString(), ctx.getPathInfo().getServiceRoot().toASCIIString());
  }

  @Test
  public void checkServiceFactoryIsSet() throws ClientProtocolException, IOException, ODataException {
    executeGetRequest("");

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);
    assertNotNull(ctx.getServiceFactory());
  }

  @Test
  public void checkServiceIsSet() throws ClientProtocolException, IOException, ODataException {
    executeGetRequest("");

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);
    assertNotNull(ctx.getService());
  }

  @Test
  public void checkBaseUriForMetadata() throws ClientProtocolException, IOException, ODataException {
    executeGetRequest("$metadata");

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);
    assertEquals(getEndpoint().toString(), ctx.getPathInfo().getServiceRoot().toASCIIString());
  }

  @Test
  public void checkAcceptuablesLanguage() throws ODataException, ClientProtocolException, IOException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    get.setHeader("Accept-Language", "de, en");

    getHttpClient().execute(get);

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("[de, en]", ctx.getAcceptableLanguages().toString());
  }

  @Test
  public void checkAcceptuablesLanguagesNoHeader() throws ODataException, ClientProtocolException, IOException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    getHttpClient().execute(get);

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("[*]", ctx.getAcceptableLanguages().toString());
  }

  @Test
  public void checkRequestHeader() throws ClientProtocolException, IOException, ODataException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    get.setHeader("ConTenT-laNguaGe", "de, en");
    getHttpClient().execute(get);

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("de, en", ctx.getHttpRequestHeader(HttpHeaders.CONTENT_LANGUAGE));
    assertNull(ctx.getHttpRequestHeader("nonsens"));
  }

  @Test
  public void checkRequestHeaders() throws ClientProtocolException, IOException, ODataException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    get.setHeader("ConTenT-laNguaGe", "de, en");
    getHttpClient().execute(get);

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);

    final Map<String, String> header = ctx.getHttpRequestHeaders();
    assertEquals("de, en", header.get(HttpHeaders.CONTENT_LANGUAGE));
  }
  
  @Test
  public void checkNewRequestHeader() throws ClientProtocolException, IOException, ODataException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    get.setHeader("ConTenT-laNguaGe", "de, en");
    getHttpClient().execute(get);

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("de, en", ctx.getRequestHeader(HttpHeaders.CONTENT_LANGUAGE));
    assertNull(ctx.getRequestHeader("nonsens"));
  }

  @Test
  public void checkNewRequestHeaders() throws ClientProtocolException, IOException, ODataException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    get.setHeader("ConTenT-laNguaGe", "de, en");
    getHttpClient().execute(get);

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);

    final Map<String, List<String>> header = ctx.getRequestHeaders();
    assertNotNull(header);
    assertNotNull(header.get(HttpHeaders.CONTENT_LANGUAGE));
    assertEquals(1, header.get(HttpHeaders.CONTENT_LANGUAGE).size());
    assertEquals("de, en", header.get(HttpHeaders.CONTENT_LANGUAGE).get(0));
  }

  @Test
  public void checkHttpMethod() throws ClientProtocolException, IOException, ODataException {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    getHttpClient().execute(get);

    final ODataContext ctx = getService().getProcessor().getContext();
    assertNotNull(ctx);

    final String httpMethod = ctx.getHttpMethod();
    assertEquals("GET", httpMethod);
  }
}
