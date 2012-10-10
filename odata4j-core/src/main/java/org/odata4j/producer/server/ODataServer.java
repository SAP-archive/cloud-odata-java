package org.odata4j.producer.server;

import javax.ws.rs.core.Application;

import org.odata4j.producer.resources.AbstractODataApplication;
import org.odata4j.producer.resources.DefaultODataApplication;
import org.odata4j.producer.resources.RootApplication;

/**
 * Generic OData server
 */
public interface ODataServer {

  /**
   * Starts the OData server.
   *
   * @return this server
   */
  ODataServer start();

  /**
   * Stops the OData server.
   *
   * @return this server
   */
  ODataServer stop();

  /**
   * Sets the OData application.
   *
   * @param odataApp  the OData application class
   * @return this server
   * @see AbstractODataApplication
   * @see DefaultODataApplication
   */
  ODataServer setODataApplication(Class<? extends Application> odataApp);

  /**
   * Sets the root application.
   *
   * @param rootApp  the root application class
   * @return this server
   * @see RootApplication
   */
  ODataServer setRootApplication(Class<? extends Application> rootApp);
}
