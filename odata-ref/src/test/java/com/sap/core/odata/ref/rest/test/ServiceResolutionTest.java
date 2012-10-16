package com.sap.core.odata.ref.rest.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.ref.test.AbstractScenarioTest;

public class ServiceResolutionTest extends AbstractScenarioTest {

  @Test
  public void testServiceResolution() throws ClientProtocolException, IOException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "aaa/bbb/$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);
    Header accept = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    
    assertEquals(200, response.getStatusLine().getStatusCode());
    assertEquals(MediaType.TEXT_PLAIN, accept.getValue());
    
    assertEquals("aaa", this.getScenarioProducer().getSegment1());
    assertEquals("bbb", this.getScenarioProducer().getSegment2());

  }
  
  
}
