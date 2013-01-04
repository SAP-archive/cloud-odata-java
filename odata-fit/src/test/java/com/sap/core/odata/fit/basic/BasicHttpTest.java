package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;
import com.sap.core.odata.core.enums.ContentType;
import com.sap.core.odata.testutils.helper.HttpMerge;
import com.sap.core.odata.testutils.helper.StringHelper;

/**
 * @author SAP AG
 */
public class BasicHttpTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((Metadata) processor).readMetadata(any(GetMetadataView.class),any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocument) processor).readServiceDocument(any(GetServiceDocumentView.class),any(String.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void testGetServiceDocument() throws ODataException, MalformedURLException, IOException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("service document", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testGetServiceDocumentWithRedirect() throws ODataException, MalformedURLException, IOException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString()));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("service document", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testGet() throws ODataException, MalformedURLException, IOException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("metadata", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPut() throws MalformedURLException, IOException {
    HttpPut put = new HttpPut(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(put);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(put.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(post.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testDelete() throws MalformedURLException, IOException {
    HttpDelete delete = new HttpDelete(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(delete);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMerge() throws MalformedURLException, IOException {
    HttpMerge merge = new HttpMerge(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(merge);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(merge.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatch() throws MalformedURLException, IOException {
    HttpPatch get = new HttpPatch(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(get.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMergeTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "MERGE");
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("MERGE"));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatchTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "PATCH");
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("PATCH"));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

}
