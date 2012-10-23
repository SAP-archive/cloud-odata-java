package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

public class ContextTest extends AbstractBasicTest {

  @Test
  public void checkContextExists() throws ClientProtocolException, IOException {
    assertNull(this.getProcessor().getContext());
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertNotNull(this.getProcessor().getContext());
    assertEquals(204, response.getStatusLine().getStatusCode());
  }
  
}
