package com.sap.core.odata.testutils.fit;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.testutils.server.TestServer;

public abstract class AbstractFitTest {

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

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
  public void before() throws Exception {
    this.service = createService();
    FitStaticServiceFactory.setService(this.service);
    this.server.startServer(FitStaticServiceFactory.class);
  }

  @After
  public void after() throws Exception {
    try {
      this.server.stopServer();
    } finally {
      FitStaticServiceFactory.setService(null);
    }
  }
}
