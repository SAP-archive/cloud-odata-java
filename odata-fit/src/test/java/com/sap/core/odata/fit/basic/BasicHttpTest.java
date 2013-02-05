package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class BasicHttpTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void testGetServiceDocument() throws ODataException, MalformedURLException, IOException {
    HttpResponse response = executeGetRequest("/");

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("service document", payload);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testGetServiceDocumentWithRedirect() throws ODataException, MalformedURLException, IOException {
    HttpResponse response = executeGetRequest("");

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("service document", payload);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testGet() throws ODataException, MalformedURLException, IOException {
    HttpResponse response = executeGetRequest("$metadata");

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("metadata", payload);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPut() throws MalformedURLException, IOException {
    HttpPut put = new HttpPut(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(put);

//    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
//    assertTrue(payload.contains(put.getMethod()));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
  }
  
  @Test
  public void testPutWithContent() throws MalformedURLException, IOException {
    HttpPut put = new HttpPut(URI.create(this.getEndpoint().toString()));
    String xml = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/\">" + 
        "</entry>";
    HttpEntity entity = new StringEntity(xml);
    put.setEntity(entity);
    HttpResponse response = this.getHttpClient().execute(put);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatusLine().getStatusCode());
  }


  @Test
  public void testPostMethodNotAllowedWithContent() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString()));
    String xml = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/\">" + 
        "</entry>";
    HttpEntity entity = new StringEntity(xml);
    post.setEntity(entity);
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatusLine().getStatusCode());
  }
  
  @Test
  public void testPostNotFound() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
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

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatch() throws MalformedURLException, IOException {
    HttpPatch get = new HttpPatch(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMergeTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "MERGE");
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatchTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "/aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "PATCH");
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("error"));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
  }

}
