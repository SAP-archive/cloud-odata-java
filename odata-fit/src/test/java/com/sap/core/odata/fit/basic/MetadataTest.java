package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.feature.Metadata;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.core.enums.ContentType;
import com.sap.core.odata.testutils.helper.StringHelper;

public class MetadataTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((Metadata) processor).readMetadata(any(GetMetadataUriInfo.class),any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void readMetadata() throws ClientProtocolException, IOException, ODataException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertEquals("metadata", payload);
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

}
