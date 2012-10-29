package com.sap.core.odata.core.rest;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;

import org.odata4j.producer.ODataProducerFactory;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataProcessorFactory;

/**
 * Default OData root locator responsible to handle the whole path and delegate all calls to a sub locator:<p>
 * <code>/{odata path}  e.g. http://host:port/webapp/odata.svc/$metadata</code><br>
 * All path segments defined by a servlet mapping belong to the odata uri.
 * </p>
 * This behavior can be changed:<p>
 * <code>/{custom path}{odata path}  e.g. http://host:port/webapp/bmw/odata.svc/$metadata</code><br>
 * The first segment defined by a servlet mapping belong to customer context and the following segments are OData specific.
 * </p>
 */
public class ODataRootLocator {

  @Context
  private HttpHeaders httpHeaders;
  @Context
  private UriInfo uriInfo;
  @Context
  private Request request;
  
  @Context
  private ServletConfig servletConfig;

  /**
   * Default root behavior which will delegate all paths to a ODataLocator.
   * @param odataPathSegments all segments have to be OData
   * @return a locator handling OData protocol
   * @throws ODataError 
   * @throws ClassNotFoundException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   */
  @Path("/{odataPathSegments: .*}")
  public ODataLocatorImpl getSubLocator(@PathParam("odataPathSegments") List<PathSegment> odataPathSegments) throws ODataError, ClassNotFoundException, InstantiationException, IllegalAccessException {
    ODataLocatorImpl odataLocator = new ODataLocatorImpl();

    odataLocator.setPathSegments(odataPathSegments);
    odataLocator.setHttpHeaders(this.httpHeaders);
    odataLocator.setUriInfo(this.uriInfo);

    String factoryClassName = this.servletConfig.getInitParameter("com.sap.core.odata.processor.factory");
    if (factoryClassName == null) {
      throw new RuntimeException("servlet config missing: com.sap.core.odata.processor.factory");
    }
    Class<?> factoryClass = Class.forName(factoryClassName);
    ODataProcessorFactory processorFactory = (ODataProcessorFactory) factoryClass.newInstance();
    ODataProcessor processor = processorFactory.create();
    odataLocator.setProcessor(processor);
    
    odataLocator.beforeRequest();

    return odataLocator;

  }
}