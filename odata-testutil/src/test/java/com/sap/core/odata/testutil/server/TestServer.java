package com.sap.core.odata.testutil.server;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.testutil.fit.FitStaticServiceFactory;

/**
 * @author SAP AG
 */
public class TestServer {

  public TestServer() {}

//  private static final Logger log = LoggerFactory.getLogger(TestServer.class);

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

        InetSocketAddress isa = new InetSocketAddress(HOST, port);
        try {
          ServerSocket ss = new ServerSocket(port);
          if(ss.isBound()) {
//            log.info("Created socket {}", ss.isBound());
            server = new Server(isa);
            server.setHandler(contextHandler);
            server.start();
            endpoint = new URI(SCHEME, null, HOST, isa.getPort(), PATH, null, null);
//          log.info("Started server at endpoint {}", endpoint.toASCIIString());
            break;
          }
        } catch (final BindException e) {
//          log.info("port is busy... " + isa.getPort() + " [{}]", e.getMessage());
        }
      }

      if (!server.isStarted()) {
        throw new BindException("no free port in range of [" + PORT_MIN + ".." + PORT_MAX + "]");
      }

    } catch (final Exception e) {
      throw new ServerException(e);
    }
  }

  public void startServer(ODataService service) {
    startServer(FitStaticServiceFactory.class);

    if(server != null && server.isStarted()) {
      FitStaticServiceFactory.bindService(this, service);
    }
  }
  
  public void stopServer() {
    try {
      if (server != null) {
        FitStaticServiceFactory.unbindService(this);
        server.stop();
//        if(endpoint == null) {
//          log.info("Stopped server");
//        } else {
//          log.info("Stopped server at endpoint {}", endpoint.toASCIIString());
//        }
      }
    } catch (final Exception e) {
      throw new ServerException(e);
    }
  }
}
