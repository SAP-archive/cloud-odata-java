package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.facet.Metadata;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.fit.StringStreamHelper;

public class MetadataTest extends AbstractBasicTest {
  
  @Test
  @Ignore("requires core adaption to new EDM api")
  public void readMetadata() throws ClientProtocolException, IOException, ODataError {
    
    Metadata metadata = this.getProducer().getMetadataProcessor();
    when(metadata.readMetadata()).thenReturn(ODataResponse.status(200).entity("metadata").build());
    
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);
    
    Header accept = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);

    String payload = StringStreamHelper.inputStreamToString(response.getEntity().getContent());

    assertEquals("metadata", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
    assertEquals(MediaType.TEXT_PLAIN, accept.getValue());

  }

}
