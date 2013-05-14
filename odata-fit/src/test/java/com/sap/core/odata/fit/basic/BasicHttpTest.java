package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.HttpMethod;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;
import com.sap.core.odata.testutil.helper.HttpMerge;
import com.sap.core.odata.testutil.helper.HttpSomethingUnsupported;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class BasicHttpTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    final ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class)))
        .thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), any(String.class)))
        .thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void getServiceDocument() throws Exception {
    final HttpResponse response = executeGetRequest("/");
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
    assertEquals("service document", StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

  @Test
  public void getServiceDocumentWithRedirect() throws Exception {
    final HttpResponse response = executeGetRequest("");
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
    assertEquals("service document", StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

  @Test
  public void get() throws Exception {
    HttpResponse response = executeGetRequest("$metadata");

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
    assertEquals("metadata", StringHelper.inputStreamToString(response.getEntity().getContent()));

    response = executeGetRequest("//////$metadata");
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
    StringHelper.inputStreamToString(response.getEntity().getContent());
    response = executeGetRequest("/./$metadata");
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    StringHelper.inputStreamToString(response.getEntity().getContent());
    response = executeGetRequest("$metadata/./");
    assertEquals(HttpStatusCodes.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void put() throws Exception {
    final HttpPut put = new HttpPut(URI.create(getEndpoint().toString() + "aaa/bbb/ccc"));
    final HttpResponse response = getHttpClient().execute(put);

    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void putWithContent() throws Exception {
    final HttpPut put = new HttpPut(URI.create(getEndpoint().toString()));
    final String xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<entry xmlns=\"" + Edm.NAMESPACE_ATOM_2005 + "\"" +
            " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\"" +
            " xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\"" +
            " xml:base=\"https://server.at.some.domain.com/path.to.some.service/ReferenceScenario.svc/\">" +
            "</entry>";
    final HttpEntity entity = new StringEntity(xml);
    put.setEntity(entity);
    final HttpResponse response = getHttpClient().execute(put);

    assertEquals(HttpStatusCodes.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatusLine().getStatusCode());
    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void postMethodNotAllowedWithContent() throws Exception {
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString()));
    final String xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<entry xmlns=\"" + Edm.NAMESPACE_ATOM_2005 + "\"" +
            " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\"" +
            " xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\"" +
            " xml:base=\"https://server.at.some.domain.com/path.to.some.service/ReferenceScenario.svc/\">" +
            "</entry>";
    final HttpEntity entity = new StringEntity(xml);
    post.setEntity(entity);
    final HttpResponse response = getHttpClient().execute(post);

    assertEquals(HttpStatusCodes.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatusLine().getStatusCode());
    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void postNotFound() throws Exception {
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "aaa/bbb/ccc"));
    final HttpResponse response = getHttpClient().execute(post);

    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void delete() throws Exception {
    final HttpDelete delete = new HttpDelete(URI.create(getEndpoint().toString() + "aaa/bbb/ccc"));
    final HttpResponse response = getHttpClient().execute(delete);

    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void merge() throws Exception {
    final HttpMerge merge = new HttpMerge(URI.create(getEndpoint().toString() + "aaa/bbb/ccc"));
    final HttpResponse response = getHttpClient().execute(merge);

    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void patch() throws Exception {
    HttpPatch get = new HttpPatch(URI.create(getEndpoint().toString() + "aaa/bbb/ccc"));
    final HttpResponse response = getHttpClient().execute(get);

    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void unsupportedMethod() throws Exception {
    HttpResponse response = getHttpClient().execute(new HttpHead(getEndpoint()));
    assertEquals(HttpStatusCodes.NOT_IMPLEMENTED.getStatusCode(), response.getStatusLine().getStatusCode());

    response = getHttpClient().execute(new HttpOptions(getEndpoint()));
    assertEquals(HttpStatusCodes.NOT_IMPLEMENTED.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void unknownMethod() throws Exception {
    HttpSomethingUnsupported request = new HttpSomethingUnsupported(getEndpoint() + "aaa/bbb/ccc");
    final HttpResponse response = getHttpClient().execute(request);
    assertEquals(HttpStatusCodes.NOT_IMPLEMENTED.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void tunneledByPost() throws Exception {
    tunnelPost("X-HTTP-Method", ODataHttpMethod.MERGE);
    tunnelPost("X-HTTP-Method", ODataHttpMethod.PATCH);
    tunnelPost("X-HTTP-Method", ODataHttpMethod.DELETE);
    tunnelPost("X-HTTP-Method", ODataHttpMethod.PUT);
    tunnelPost("X-HTTP-Method", ODataHttpMethod.GET);
    tunnelPost("X-HTTP-Method", ODataHttpMethod.POST);

    tunnelPost("X-HTTP-Method-Override", ODataHttpMethod.MERGE);
    tunnelPost("X-HTTP-Method-Override", ODataHttpMethod.PATCH);
    tunnelPost("X-HTTP-Method-Override", ODataHttpMethod.DELETE);
    tunnelPost("X-HTTP-Method-Override", ODataHttpMethod.PUT);
    tunnelPost("X-HTTP-Method-Override", ODataHttpMethod.GET);
    tunnelPost("X-HTTP-Method-Override", ODataHttpMethod.POST);
  }

  private void tunnelPost(final String header, final ODataHttpMethod method) throws IOException {
    tunnelPost(header, method.toString(), HttpStatusCodes.NOT_FOUND);
  }

  private void tunnelPost(final String header, final String method, final HttpStatusCodes expectedStatus) throws IOException {
    HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "aaa/bbb/ccc"));
    post.setHeader(header, method);
    final HttpResponse response = getHttpClient().execute(post);
    assertEquals(expectedStatus.getStatusCode(), response.getStatusLine().getStatusCode());

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void tunneledBadRequest() throws Exception {
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "MERGE");
    post.setHeader("X-HTTP-Method-Override", "PATCH");
    final HttpResponse response = getHttpClient().execute(post);
    assertEquals(HttpStatusCodes.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("error"));
  }

  @Test
  public void tunneledUnsupportedMethod() throws Exception {
    tunnelPost("X-HTTP-Method", HttpMethod.HEAD, HttpStatusCodes.NOT_IMPLEMENTED);
    tunnelPost("X-HTTP-Method-Override", HttpMethod.HEAD, HttpStatusCodes.NOT_IMPLEMENTED);
    tunnelPost("X-HTTP-Method", HttpMethod.OPTIONS, HttpStatusCodes.NOT_IMPLEMENTED);
    tunnelPost("X-HTTP-Method-Override", HttpMethod.OPTIONS, HttpStatusCodes.NOT_IMPLEMENTED);
  }

  @Test
  public void tunneledUnknownMethod() throws Exception {
    tunnelPost("X-HTTP-Method", "xxx", HttpStatusCodes.NOT_IMPLEMENTED);
  }

  @Test
  public void tunneledUnknownMethodOverride() throws Exception {
    tunnelPost("X-HTTP-Method-Override", "xxx", HttpStatusCodes.NOT_IMPLEMENTED);
  }
}
