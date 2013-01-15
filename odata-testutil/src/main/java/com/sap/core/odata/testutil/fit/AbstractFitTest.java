package com.sap.core.odata.testutil.fit;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;
import com.sap.core.odata.testutil.server.TestServer;

/**
 * @author SAP AG
 */
public abstract class AbstractFitTest extends BaseTest {

  private TestServer server = new TestServer();

  private ODataSingleProcessorService service;

  private HttpClient httpClient = new DefaultHttpClient();

  protected URI getEndpoint() {
    return this.server.getEndpoint();
  }

  protected HttpClient getHttpClient() {
    return httpClient;
  }

  protected ODataSingleProcessorService getService() {
    return service;
  }

  protected abstract ODataSingleProcessorService createService() throws ODataException;

  @Before
  public void before() {
    try {
      this.service = createService();
      FitStaticServiceFactory.setService(this.service);
      this.server.startServer(FitStaticServiceFactory.class);
    } catch (ODataException e) {
      throw new RuntimeException(e);
    }
  }

  @After
  public void after() {
    try {
      this.server.stopServer();
    } finally {
      FitStaticServiceFactory.setService(null);
    }
  }
}
