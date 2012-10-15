package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

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
import com.sap.core.odata.core.edm.EdmServiceMetadata;
import com.sap.core.odata.core.producer.Entity;
import com.sap.core.odata.core.producer.Metadata;
import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.fit.AbstractFitTest;
import com.sap.core.odata.fit.HttpMerge;
import com.sap.core.odata.fit.StringStreamHelper;

public class BasicHttpTest extends AbstractFitTest {

  @Override
  protected ODataProducer createProducer() {
    ODataProducer producer = mock(ODataProducer.class);

    EdmServiceMetadata edmsm = mock(EdmServiceMetadata.class);
    when(edmsm.getDataServiceVersion()).thenReturn("2.0");
    
    Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(edmsm);
    
    Metadata metadata = mock(Metadata.class);
    when(metadata.getEdm()).thenReturn(edm);
    
    Entity entity = mock(Entity.class);
    
    when(producer.getMetadata()).thenReturn(metadata);
    when(producer.getEntity()).thenReturn(entity);
    
    return producer;
  }

  @Test
  public void testGet() throws MalformedURLException, IOException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "aaa/bbb/ccc"));
    this.responseCheck(get);
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
