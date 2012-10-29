package com.sap.core.odata.testutils.server;

import java.net.URI;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.processor.ODataProcessorFactory;

public class TestServer {

  public TestServer() {}

  public TestServer(URI endpoint) {
    this.endpoint = endpoint;
  }

  private static final Logger log = LoggerFactory.getLogger(TestServer.class);

  private URI endpoint = URI.create("http://localhost:19080/test/");

  public URI getEndpoint() {
    return this.endpoint;
  }

  private Server server;

  public void startServer(Class<? extends ODataProcessorFactory> factoryClass) {
    try {
      TestServer.log.debug("##################################");
      TestServer.log.debug("## Starting server at endpoint");
      TestServer.log.debug("## uri:         " + this.endpoint);
      TestServer.log.debug("## factory:     " + factoryClass.getCanonicalName());
      TestServer.log.debug("##################################");

      CXFNonSpringJaxrsServlet odataServlet = new CXFNonSpringJaxrsServlet();
      ServletHolder odataServletHolder = new ServletHolder(odataServlet);
      odataServletHolder.setInitParameter("javax.ws.rs.Application", "com.sap.core.odata.core.rest.ODataApplication");
      odataServletHolder.setInitParameter("com.sap.core.odata.processor.factory", factoryClass.getCanonicalName());

      ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
      contextHandler.addServlet(odataServletHolder, this.endpoint.getPath() + "*");

      this.server = new Server(this.endpoint.getPort());
      this.server.setHandler(contextHandler);
      this.server.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void stopServer() {
    try {
      this.server.stop();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
