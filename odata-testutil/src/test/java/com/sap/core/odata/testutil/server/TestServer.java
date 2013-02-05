package com.sap.core.odata.testutil.server;

import java.net.BindException;
import java.net.URI;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.testutil.helper.ProcessLocker;

/**
 * @author SAP AG
 */
public class TestServer {

  public TestServer() {}

  private static final Logger log = LoggerFactory.getLogger(TestServer.class);

  private static final int PORT_MIN = 19000;
  private static final int PORT_MAX = 19200;
  private static final int PORT_INC = 1;

  private static final String SCHEME = "http";
  private static final String HOST = "localhost";
  private static final String PATH = "/test";

  private URI endpoint; //= URI.create("http://localhost:19080/test"); // no slash at the end !!!

  private int pathSplit = 0;

  public int getPathSplit() {
    return pathSplit;
  }

  public void setPathSplit(int pathSplit) {
    this.pathSplit = pathSplit;
  }

  public URI getEndpoint() {
    return URI.create(endpoint + "/");
  }

  private Server server;

  public void startServer(Class<? extends ODataServiceFactory> factoryClass) {
    try {
      for (int port = PORT_MIN; port <= PORT_MAX; port += PORT_INC) {
        final CXFNonSpringJaxrsServlet odataServlet = new CXFNonSpringJaxrsServlet();
        final ServletHolder odataServletHolder = new ServletHolder(odataServlet);
        odataServletHolder.setInitParameter("javax.ws.rs.Application", "com.sap.core.odata.core.rest.app.ODataApplication");
        odataServletHolder.setInitParameter(ODataServiceFactory.FACTORY_LABEL, factoryClass.getCanonicalName());

        if (pathSplit > 0) {
          odataServletHolder.setInitParameter(ODataServiceFactory.PATH_SPLIT_LABEL, Integer.toString(pathSplit));
        }

        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(odataServletHolder, PATH + "/*");

        endpoint = new URI(SCHEME, null, HOST, port, PATH, null, null);
        server = new Server(port);
        server.setHandler(contextHandler);
        try {
          server.start();
          break;
        } catch (final BindException e) {
          TestServer.log.info("port is busy... " + port);
        }
      }

      if (!server.isStarted()) {
        throw new BindException("no free port in range of [" + PORT_MIN + ".." + PORT_MAX + "]");
      }

    } catch (final Exception e) {
      throw new ServerException(e);
    }
  }

  public void stopServer() {
    try {
      if (server != null) {
        server.stop();
      }
    } catch (final Exception e) {
      throw new ServerException(e);
    }
  }
}
