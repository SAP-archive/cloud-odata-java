package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
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
public class UrlRewriteTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    final ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void testGetServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpGet.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPutServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpPut.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPostServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpPost.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testDeleteServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpDelete.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testOptionsServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpOptions.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testHeadServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpHead.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMergeServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpMerge.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatchServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpPatch.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.TEMPORARY_REDIRECT.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testSomethingUnsupportedServiceDocumentRedirect() throws Exception {
    final HttpRequestBase httpMethod = createRedirectRequest(HttpSomethingUnsupported.class);
    final HttpResponse response = getHttpClient().execute(httpMethod);
    assertEquals(HttpStatusCodes.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  private HttpRequestBase createRedirectRequest(final Class<? extends HttpRequestBase> clazz) throws Exception {
    String endpoint = getEndpoint().toASCIIString();
    endpoint = endpoint.substring(0, endpoint.length() - 1);

    final HttpRequestBase httpMethod = clazz.newInstance();
    httpMethod.setURI(URI.create(endpoint));

    final HttpParams params = new BasicHttpParams();
    params.setParameter("http.protocol.handle-redirects", false);
    httpMethod.setParams(params);
    return httpMethod;
  }

  @Test
  public void testGetServiceDocumentWithSlash() throws Exception {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString()));
    final HttpParams params = new BasicHttpParams();
    params.setParameter("http.protocol.handle-redirects", false);
    get.setParams(params);

    final HttpResponse response = getHttpClient().execute(get);

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("service document", payload);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testGetMetadata() throws Exception {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "$metadata"));
    final HttpParams params = new BasicHttpParams();
    params.setParameter("http.protocol.handle-redirects", false);
    get.setParams(params);

    final HttpResponse response = getHttpClient().execute(get);

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("metadata", payload);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

}