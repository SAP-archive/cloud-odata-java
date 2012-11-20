package com.sap.core.odata.testutils.fit;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.testutils.server.TestServer;

public abstract class AbstractFitTest {

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  private TestServer server = new TestServer();
 
  private ODataService service;
  
  private HttpClient httpClient = new DefaultHttpClient();

  private EdmProvider edmProvider;

  protected URI getEndpoint() {
    return this.server.getEndpoint();
  }

  protected HttpClient getHttpClient() {
    return httpClient;
  }

  protected ODataProcessor getProcessor() throws ODataException {
    return this.service.getProcessor();
  }

  protected EdmProvider getEdmProvider() {
    return this.edmProvider;
  }

  @Before
  public void before() throws Exception {
    ODataSingleProcessor processor = this.createProcessorMock();
    this.edmProvider = this.createEdmProviderMock();
    
    this.service = new ODataSingleProcessorService(this.edmProvider, (ODataSingleProcessor) processor) {};
    ServiceFactory.setService(this.service);
    
    this.server.startServer(ServiceFactory.class);
  }

  /**
   * @return mock provider
   */
  protected abstract EdmProvider createEdmProviderMock();

  /**
   * @return mock processor
   */
  protected abstract ODataSingleProcessor createProcessorMock() throws ODataException;


  @After
  public void after() throws Exception {
    try {
      this.server.stopServer();
    } finally {
      ServiceFactory.setService(null);
    }
  }
}
