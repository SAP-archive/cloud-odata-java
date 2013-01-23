package com.sap.core.odata.testutil.fit;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;
import com.sap.core.odata.testutil.helper.TestutilException;
import com.sap.core.odata.testutil.server.TestServer;

/**
 * @author SAP AG
 */
public abstract class AbstractFitTest extends BaseTest {

  private final TestServer server = new TestServer();

  private ODataSingleProcessorService service;

  private final HttpClient httpClient = new DefaultHttpClient();

  protected URI getEndpoint() {
    return server.getEndpoint();
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
      service = createService();
      FitStaticServiceFactory.setService(service);
      server.startServer(FitStaticServiceFactory.class);
    } catch (final ODataException e) {
      throw new TestutilException(e);
    }
  }

  @After
  public void after() {
    try {
      server.stopServer();
    } finally {
      FitStaticServiceFactory.setService(null);
    }
  }
}
