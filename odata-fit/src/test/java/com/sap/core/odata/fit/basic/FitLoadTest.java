package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;

public class FitLoadTest extends AbstractBasicTest {

  private static int LOOP_COUNT = 1;

  @Override
  ODataSingleProcessor createProcessor() throws ODataException {
    final ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }


  @Test
  public void useApacheHttpClient() throws ClientProtocolException, IOException {
    for (int i = 0; i < LOOP_COUNT; i++) {
      final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "$metadata"));
      HttpResponse response = getHttpClient().execute(get);
      assertEquals(200, response.getStatusLine().getStatusCode());
      get.abort();
    }
  }

  @Test
  public void useJavaHttpClient() throws IOException {
    for (int i = 0; i < LOOP_COUNT; i++) {
      URI uri = URI.create(getEndpoint().toString() + "$metadata");

      HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
      connection.setRequestMethod("GET");
      connection.connect();

      int code = connection.getResponseCode();
      assertEquals(200, code);
    }
  }

}
