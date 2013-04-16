/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.testutil.server;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.log4j.Logger;
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
  private static final Logger log = Logger.getLogger(TestServer.class);

  private static final int PORT_MIN = 19000;
  private static final int PORT_MAX = 19200;
  private static final int PORT_INC = 1;

  private static final String DEFAULT_SCHEME = "http";
  private static final String DEFAULT_HOST = "localhost";
  private static final String DEFAULT_PATH = "/test";

  private URI endpoint; //= URI.create("http://localhost:19080/test"); // no slash at the end !!!
  private final String path;

  private int pathSplit = 0;

  public TestServer() {
    this(DEFAULT_PATH);
  }

  public TestServer(final String path) {
    if (path.startsWith("/")) {
      this.path = path;
    } else {
      this.path = "/" + path;
    }
  }

  public int getPathSplit() {
    return pathSplit;
  }

  public void setPathSplit(final int pathSplit) {
    this.pathSplit = pathSplit;
  }

  public URI getEndpoint() {
    return URI.create(endpoint + "/");
  }

  private Server server;

  public void startServer(final Class<? extends ODataServiceFactory> factoryClass) {
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
        contextHandler.addServlet(odataServletHolder, path + "/*");

        try {
          final InetSocketAddress isa = new InetSocketAddress(DEFAULT_HOST, port);
          server = new Server(isa);
          server.setHandler(contextHandler);
          server.start();
          endpoint = new URI(DEFAULT_SCHEME, null, DEFAULT_HOST, isa.getPort(), path, null, null);
          log.trace("Started server at endpoint " + endpoint.toASCIIString());
          break;
        } catch (final BindException e) {
          log.trace("port is busy... " + port + " [" + e.getMessage() + "]");
        }
      }

      if (!server.isStarted()) {
        throw new BindException("no free port in range of [" + PORT_MIN + ".." + PORT_MAX + "]");
      }

    } catch (final Exception e) {
      log.error(e);
      throw new ServerRuntimeException(e);
    }
  }

  public void startServer(final ODataService service) {
    startServer(FitStaticServiceFactory.class);

    if ((server != null) && server.isStarted()) {
      FitStaticServiceFactory.bindService(this, service);
    }
  }

  public void stopServer() {
    try {
      if (server != null) {
        FitStaticServiceFactory.unbindService(this);
        server.stop();
        log.trace("Stopped server at endpoint " + getEndpoint().toASCIIString());
      }
    } catch (final Exception e) {
      throw new ServerRuntimeException(e);
    }
  }
}
