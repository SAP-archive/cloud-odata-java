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
import javax.ws.rs.core.Response;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmEntitySet;
import com.sap.core.odata.core.producer.EntitySet;
import com.sap.core.odata.fit.HttpMerge;
import com.sap.core.odata.fit.StringStreamHelper;

public class BasicHttpTest extends AbstractBasicTest {

  @Test
  public void testGet() throws MalformedURLException, IOException {
    EdmEntitySet edmEntitySet = mock(EdmEntitySet.class);
    EdmEntityContainer edmEntityContainer = mock(EdmEntityContainer.class);
    when(edmEntityContainer.getEntitySet("entityset")).thenReturn(edmEntitySet);
    Edm edm = this.getProducer().getMetadata().getEdm();
    when(edm.getDefaultEntityContainer()).thenReturn(edmEntityContainer);

    EntitySet entitySet = this.getProducer().getEntitySet();
    when(entitySet.read()).thenReturn(Response.ok().entity("entityset").build());

    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "entityset"));

    HttpResponse response = this.getHttpClient().execute(get);
    Header accept = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);

    String payload = StringStreamHelper.inputStreamToString(response.getEntity().getContent());

    assertEquals("entityset", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
    assertEquals(MediaType.TEXT_PLAIN, accept.getValue());
  }

  @Test
  public void testPut() throws MalformedURLException, IOException {
    HttpPut put = new HttpPut(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    this.responseCheck(put);
  }

  @Test
  public void testPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    this.responseCheck(post);
  }

  @Test
  public void testDetele() throws MalformedURLException, IOException {
    HttpDelete delete = new HttpDelete(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    this.responseCheck(delete);
  }

  @Test
  public void testMerge() throws MalformedURLException, IOException {
    HttpMerge merge = new HttpMerge(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    this.responseCheck(merge);
  }

  @Test
  public void testPatch() throws MalformedURLException, IOException {
    HttpPatch get = new HttpPatch(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    this.responseCheck(get);
  }

  @Test
  public void testMergeTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "MERGE");
    this.tunneledResponseCheck(post, "MERGE");
  }

  @Test
  public void testPatchTunneledByPost() throws MalformedURLException, IOException {
    HttpPost post = new HttpPost(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    post.setHeader("X-HTTP-Method", "PATCH");
    this.tunneledResponseCheck(post, "PATCH");
  }

  private void tunneledResponseCheck(HttpRequestBase method, String xmethod) throws ClientProtocolException, IOException {
    HttpResponse response = this.getHttpClient().execute(method);
    Header accept = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);

    String payload = StringStreamHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(xmethod));
    assertEquals(200, response.getStatusLine().getStatusCode());
    assertEquals(MediaType.TEXT_PLAIN, accept.getValue());
  }

  private void responseCheck(HttpRequestBase method) throws ClientProtocolException, IOException {
    HttpResponse response = this.getHttpClient().execute(method);
    Header accept = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);

    String payload = StringStreamHelper.inputStreamToString(response.getEntity().getContent());

    assertTrue(payload.contains(method.getMethod()));
    assertEquals(200, response.getStatusLine().getStatusCode());
    assertEquals(MediaType.TEXT_PLAIN, accept.getValue());
  }

}
