package com.sap.core.odata.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.testutils.helper.StringHelper;


public class ErrorResponseTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessorMock() throws ODataException {
    ODataSingleProcessor processor = super.createProcessorMock();

    when(((ServiceDocument) processor).readServiceDocument(any(GetServiceDocumentView.class))).thenThrow(new ODataRuntimeException("unit testing"));
    
    return processor;
  }

  @Test
  public void test500RuntimeError() throws ClientProtocolException, IOException, ODataException {

    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString()));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(500, response.getStatusLine().getStatusCode());

    this.log.debug(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

}
