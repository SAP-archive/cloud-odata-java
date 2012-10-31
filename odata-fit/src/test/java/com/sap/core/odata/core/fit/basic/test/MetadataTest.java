package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.testutils.StringHelper;



public class MetadataTest extends AbstractBasicTest {
  
  @Test
  public void readMetadata() throws ClientProtocolException, IOException, ODataException {   
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);
    
    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertEquals("metadata", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

}
