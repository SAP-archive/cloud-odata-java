package org.odata4j.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.odata4j.core.Throwables;
import org.odata4j.cxf.producer.server.ODataCxfServer;
import org.odata4j.jersey.producer.resources.ODataApplication;
import org.odata4j.jersey.producer.server.ODataJerseyServer;
import org.odata4j.producer.resources.DefaultODataApplication;
import org.odata4j.producer.resources.RootApplication;
import org.odata4j.producer.server.ODataServer;

public class ODataServerFactory {

  final JaxRsImplementation impl;

  public ODataServerFactory(JaxRsImplementation impl) {
    this.impl = impl;
  }

  public void hostODataServer(String baseUri) {
    ODataServer server = null;
    try {
      server = startODataServer(baseUri);

      System.out.println("Press any key to exit");
      new BufferedReader(new InputStreamReader(System.in)).readLine();
    } catch (IOException e) {
      throw Throwables.propagate(e);
    } finally {
      if (server != null)
        server.stop();
    }
  }

  public ODataServer startODataServer(String baseUri) {
    return createODataServer(baseUri).start();
  }

  public ODataServer createODataServer(String baseUri) {
    switch (impl) {
    case JERSEY:
      return new ODataJerseyServer(baseUri, ODataApplication.class, RootApplication.class);
    case CXF:
      return new ODataCxfServer(baseUri, DefaultODataApplication.class, RootApplication.class);
    }
    return null;
  }
}
