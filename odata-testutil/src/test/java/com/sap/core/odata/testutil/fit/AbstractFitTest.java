package com.sap.core.odata.testutil.fit;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.testutil.helper.TestutilException;
import com.sap.core.odata.testutil.server.TestServer;

/**
 * @author SAP AG
 */
public abstract class AbstractFitTest extends BaseTest {

  private final TestServer server;

  private ODataService service;

  private final HttpClient httpClient = new DefaultHttpClient();

  public AbstractFitTest() {
    server = new TestServer(this.getClass().getSimpleName());
  }
  
  protected URI getEndpoint() {
    return server.getEndpoint();
  }

  protected HttpClient getHttpClient() {
    return httpClient;
  }

  protected ODataService getService() {
    return service;
  }

  protected abstract ODataService createService() throws ODataException;

  @Before
  public void before() {
    try {
      service = createService();
      server.startServer(service);
    } catch (final ODataException e) {
      throw new TestutilException(e);
    } 
  }

  @After
  public void after() {
    try {
      server.stopServer();
    } catch (final Exception e) {
      throw new TestutilException(e);
    } 
  }
}
