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

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.EdmProviderMock;


public class HttpExceptionResponseTest extends AbstractBasicTest {

  private ODataSingleProcessor processor;

  @Override
  protected ODataSingleProcessor createProcessorMock() throws ODataException {
    processor = super.createProcessorMock();
    //
    return processor;
  }
  
  @Override
  protected EdmProvider createEdmProviderMock() {
    EdmProviderMock edmProviderMock = new EdmProviderMock();
    return edmProviderMock;
  }

  @Test
  public void test404HttpNotFound() throws ClientProtocolException, IOException, ODataException {
     when(processor.readEntity(any(GetEntityView.class))).thenThrow(new ODataNotFoundException(ODataNotFoundException.ENTITY));

    HttpResponse response = executeGetRequest("Managers('199')");
    assertEquals(404, response.getStatusLine().getStatusCode());

    this.log.debug(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }
  
  
  /**
   * 
   * @param request
   * @return
   * @throws ClientProtocolException
   * @throws IOException
   */
  private HttpResponse executeGetRequest(String request) throws ClientProtocolException, IOException {
    String uri = getEndpoint().toString() + request;
    log.debug("Execute get request for uri '" + uri + "'");
    
    HttpGet get = new HttpGet(URI.create(uri));
    return getHttpClient().execute(get);
    
  }
}
