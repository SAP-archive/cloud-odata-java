package com.sap.core.odata.ref.rest.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.odata4j.core.ODataConstants.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.ref.rest.ScenarioApplication;

public abstract class AbstractScenarioTest {

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  public Server getServer() {
    return server;
  }

  public void setServer(Server server) {
    this.server = server;
  }

  public URI getEndpoint() {
    return endpoint;
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  private Server server;
  private URI endpoint = URI.create("http://localhost:19080/ext/");
  private Class<?> applicationClass = ScenarioApplication.class;
  private HttpClient httpClient = new DefaultHttpClient();
  
  @Before
  public void before() throws Exception {
    this.startServer();
  }

  @After
  public void after() throws Exception {
    this.stopServer();
  }

  private void stopServer() throws Exception {
    this.server.stop();
  }

  private void startServer() throws Exception {
    this.log.debug("##################################");
    this.log.debug("## Starting server at endpoint");
    this.log.debug("## uri:         " + this.endpoint);
    this.log.debug("## application: " + this.applicationClass.getCanonicalName());
    this.log.debug("##################################");
    
    
    CXFNonSpringJaxrsServlet odataServlet = new CXFNonSpringJaxrsServlet();
    ServletHolder odataServletHolder = new ServletHolder(odataServlet);
    odataServletHolder.setInitParameter("javax.ws.rs.Application", this.applicationClass.getCanonicalName());

    ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    contextHandler.addServlet(odataServletHolder, this.endpoint.getPath() + "*");

    this.server = new Server(this.endpoint.getPort());
    this.server.setHandler(contextHandler);
    server.start();
  }
  
  protected String entityToString(HttpEntity entity) throws UnsupportedEncodingException, IllegalStateException, IOException {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), Charsets.Upper.UTF_8));
      StringBuilder stringBuilder = new StringBuilder();
      String line = null;

      while ((line = bufferedReader.readLine()) != null)
        stringBuilder.append(line);

      bufferedReader.close();
      return stringBuilder.toString();
  }

}
