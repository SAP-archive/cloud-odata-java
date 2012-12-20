package com.sap.core.odata.testutils.fit;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.testutils.server.TestServer;

public abstract class AbstractFitTest {

  private static final String CLASSNAME_ODATA_EXCEPTION_MAPPER = "com.sap.core.odata.core.ODataExceptionMapperImpl";

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  private TestServer server = new TestServer();

  private ODataSingleProcessorService service;

  private HttpClient httpClient = new DefaultHttpClient();

  private final Map<Class<?>, Level> disabledLoggings = new HashMap<Class<?>, Level>();

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

  
  /**
   * Disable logging for class with classname {@value #CLASSNAME_ODATA_EXCEPTION_MAPPER}.
   * If no class with this classname can be found an error log entry is written.
   * <br /> 
   * Disabled logging will be automatically re-enabled after test execution (see {@link #reEnableLogging()} and 
   * {@link #after()}).
   * 
   * @param classes
   */
  protected void disableLogging() {
    try {
      disableLogging(Class.forName(CLASSNAME_ODATA_EXCEPTION_MAPPER));
    } catch (ClassNotFoundException e) {
      log.error("Expected class was not found for disabling of logging.");
    }
  }
  
  /**
   * Disable logging for over handed classes.
   * Disabled logging will be automatically re-enabled after test execution (see {@link #reEnableLogging()} and 
   * {@link #after()}).
   * 
   * @param classes
   */
  protected void disableLogging(Class<?>... classes) {
    for (Class<?> clazz : classes) {
      org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(clazz);
      if (l != null) {
        disabledLoggings.put(clazz, l.getEffectiveLevel());
        l.setLevel(Level.OFF);
      }
    }
  }

  protected void reEnableLogging() {
    for (Entry<Class<?>, Level> entry : disabledLoggings.entrySet()) {
      org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(entry.getKey());
      l.setLevel(entry.getValue());
    }
    disabledLoggings.clear();
  }

  @Before
  public void before() throws Exception {
    this.service = createService();
    FitStaticServiceFactory.setService(this.service);
    this.server.startServer(FitStaticServiceFactory.class);
  }

  @After
  public void after() {
    reEnableLogging();

    try {
      this.server.stopServer();
    } finally {
      FitStaticServiceFactory.setService(null);
    }
  }
}
