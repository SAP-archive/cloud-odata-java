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

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;
import com.sap.core.odata.testutils.helper.HttpMerge;
import com.sap.core.odata.testutils.helper.HttpSomethingUnsupported;
import com.sap.core.odata.testutils.helper.StringHelper;

public class UrlRewriteTest extends AbstractBasicTest {

  @Override
  ODataSingleProcessor createProcessor() throws ODataException {
    ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((Metadata) processor).readMetadata(any(GetMetadataView.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocument) processor).readServiceDocument(any(GetServiceDocumentView.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void testGetServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpGet.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPutServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpPut.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPostServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpPost.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testDeleteServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpDelete.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testOptionsServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpOptions.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testHeadServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpHead.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMergeServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpMerge.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatchServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpPatch.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(307, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testSomethingUnsupportedServiceDocumentRedirect() throws Exception {
    HttpRequestBase httpMethod = createRedirectRequest(HttpSomethingUnsupported.class);
    HttpResponse response = this.getHttpClient().execute(httpMethod);
    assertEquals(405, response.getStatusLine().getStatusCode());
  }

  private HttpRequestBase createRedirectRequest(Class<? extends HttpRequestBase> clazz) throws Exception {
    String endpoint = this.getEndpoint().toASCIIString();
    endpoint = endpoint.substring(0, endpoint.length() - 1);

    HttpRequestBase httpMethod = clazz.newInstance();
    httpMethod.setURI(URI.create(endpoint));

    HttpParams params = new BasicHttpParams();
    params.setParameter("http.protocol.handle-redirects", false);
    httpMethod.setParams(params);
    return httpMethod;
  }

  @Test
  public void testGetServiceDocumentWithSlash() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString()));
    HttpParams params = new BasicHttpParams();
    params.setParameter("http.protocol.handle-redirects", false);
    get.setParams(params);

    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("service document", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());

  }

  @Test
  public void testGetMetadata() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));
    HttpParams params = new BasicHttpParams();
    params.setParameter("http.protocol.handle-redirects", false);
    get.setParams(params);

    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("metadata", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());

  }

}