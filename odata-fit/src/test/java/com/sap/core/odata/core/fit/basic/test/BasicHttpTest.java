package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.aspect.EntitySet;
import com.sap.core.odata.core.uri.UriParserResultImpl;
import com.sap.core.testutils.HttpMerge;
import com.sap.core.testutils.StringHelper;

public class BasicHttpTest extends AbstractBasicTest {

  @Before
  public void before() throws Exception {
    super.before();
    EdmEntitySet edmEntitySet = mock(EdmEntitySet.class);
    EdmEntityContainer edmEntityContainer = mock(EdmEntityContainer.class);
    when(edmEntityContainer.getEntitySet("entityset")).thenReturn(edmEntitySet);
    Edm edm = this.getProcessor().getMetadataProcessor().getEdm();
    when(edm.getDefaultEntityContainer()).thenReturn(edmEntityContainer);
    EntitySet entitySet = this.getProcessor().getEntitySetProcessor();
    UriParserResultImpl uriParserResult = mock(UriParserResultImpl.class);
    when(entitySet.readEntitySet(uriParserResult)).thenReturn(ODataResponse.status(HttpStatus.OK).entity("entityset").build());
  }

  @Ignore("requires core adaption to new EDM api")
  public void testGet() throws ODataException, MalformedURLException, IOException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "entityset"));

    HttpResponse response = this.getHttpClient().execute(get);
    Header accept = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertEquals("entityset", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
    assertEquals(MediaType.TEXT_PLAIN, accept.getValue());
  }

  @Test
  public void testPut() throws MalformedURLException, IOException {
    HttpPut put = new HttpPut(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(put);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(put.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(post.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testDetele() throws MalformedURLException, IOException {
    HttpDelete delete = new HttpDelete(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(delete);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(delete.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMerge() throws MalformedURLException, IOException {
    HttpMerge merge = new HttpMerge(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(merge);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(merge.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatch() throws MalformedURLException, IOException {
    HttpPatch get = new HttpPatch(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(get.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMergeTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "MERGE");
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("MERGE"));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPatchTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "PATCH");
    HttpResponse response = this.getHttpClient().execute(post);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains("PATCH"));
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

}
