/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

/**
 * @author SAP AG
 */
public class FitLoadTest extends AbstractBasicTest {

  /*
   * increase for load analysis > 10.000
   */
  private static final int LOOP_COUNT = 1;

  @Override
  ODataSingleProcessor createProcessor() throws ODataException {
    ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void useApacheHttpClient() throws ClientProtocolException, IOException {
    final URI uri = URI.create(getEndpoint().toString() + "$metadata");
    for (int i = 0; i < LOOP_COUNT; i++) {
      HttpGet get = new HttpGet(uri);
      HttpResponse response = getHttpClient().execute(get);
      assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
      get.abort();
    }
  }

  @Test
  public void useJavaHttpClient() throws IOException {
    final URI uri = URI.create(getEndpoint().toString() + "$metadata");
    for (int i = 0; i < LOOP_COUNT; i++) {
      HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      assertEquals(HttpStatusCodes.OK.getStatusCode(), connection.getResponseCode());
    }
  }

}
