package org.odata4j.cxf.producer.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.core4j.Enumerable;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.odata4j.core.Throwables;
import org.odata4j.producer.server.ODataServer;

/**
 * OData server using the CXF JAX-RS implementation and Jetty as HTTP server.
 */
public class ODataCxfServer implements ODataServer {

  private String appBaseUri;
  private Class<? extends Application> odataApp;
  private Class<? extends Application> rootApp;
  private final List<Handler> jettyRequestHandlers = new ArrayList<Handler>();
  private SecurityHandler jettySecurityHandler;
  private Server server;

  public ODataCxfServer(String appBaseUri) {
    this.appBaseUri = appBaseUri;

    // ensure that the correct JAX-RS implementation (CXF) is loaded
    if (!(RuntimeDelegate.getInstance() instanceof org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl))
      RuntimeDelegate.setInstance(new org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl());
  }

  public ODataCxfServer(String appBaseUri, Class<? extends Application> odataApp, Class<? extends Application> rootApp) {
    this(appBaseUri);
    this.odataApp = odataApp;
    this.rootApp = rootApp;
  }

  @Override
  public ODataServer setODataApplication(Class<? extends Application> odataApp) {
    this.odataApp = odataApp;
    return this;
  }

  @Override
  public ODataServer setRootApplication(Class<? extends Application> rootApp) {
    this.rootApp = rootApp;
    return this;
  }

  public ODataCxfServer addJettyRequestHandler(Handler handler) {
    jettyRequestHandlers.add(handler);
    return this;
  }

  public ODataCxfServer setJettySecurityHandler(SecurityHandler securityHandler) {
    jettySecurityHandler = securityHandler;
    return this;
  }

  @Override
  public ODataServer start() {
    if (odataApp == null)
      throw new RuntimeException("ODataApplication not set");

    URL url;
    try {
      url = new URL(appBaseUri);
    } catch (MalformedURLException e) {
      throw Throwables.propagate(e);
    }

    CXFNonSpringJaxrsServlet odataServlet = new CXFNonSpringJaxrsServlet();
    ServletHolder odataServletHolder = new ServletHolder(odataServlet);
    odataServletHolder.setInitParameter("javax.ws.rs.Application", odataApp.getCanonicalName());

    ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    contextHandler.addServlet(odataServletHolder, normalizePath(url.getPath()) + "/*");

    if (rootApp != null) {
      CXFNonSpringJaxrsServlet rootServlet = new CXFNonSpringJaxrsServlet();
      ServletHolder rootServletHolder = new ServletHolder(rootServlet);
      rootServletHolder.setInitParameter("javax.ws.rs.Application", rootApp.getCanonicalName());

      contextHandler.addServlet(rootServletHolder, "/*");
    }

    if (jettySecurityHandler != null)
      contextHandler.setSecurityHandler(jettySecurityHandler);

    server = new Server(url.getPort());
    server.setHandler(getHandlerCollection(contextHandler));

    try {

      server.start();
      return this;
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public ODataServer stop() {
    try {
      server.stop();
      return this;
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  private static String normalizePath(String path) {
    if (path.endsWith("/"))
      return path.substring(0, path.length() - 1);
    return path;
  }

  private HandlerCollection getHandlerCollection(ServletContextHandler contextHandler) {
    List<Handler> handlers = jettyRequestHandlers;
    handlers.add(contextHandler);

    HandlerCollection handlerCollection = new HandlerCollection();
    handlerCollection.setHandlers(Enumerable.create(handlers).toArray(Handler.class));
    return handlerCollection;
  }
}
