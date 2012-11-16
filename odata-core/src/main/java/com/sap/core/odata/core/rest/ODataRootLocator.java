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

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataServiceFactory;
import com.sap.core.odata.core.rest.ODataLocatorImpl.InitParameter;

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
   * @throws ODataException 
   * @throws ClassNotFoundException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   */
  @Path("/{pathSegments: .*}")
  public ODataLocatorImpl handleRequest(@PathParam("pathSegments") List<PathSegment> pathSegments) throws ODataException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    ODataLocatorImpl odataLocator = new ODataLocatorImpl();

    String factoryClassName = this.servletConfig.getInitParameter(ODataServiceFactory.FACTORY_LABEL);
    if (factoryClassName == null) {
      throw new RuntimeException("servlet config missing: com.sap.core.odata.processor.factory");
    }
    Class<?> factoryClass = Class.forName(factoryClassName);
    ODataServiceFactory serviceFactory = (ODataServiceFactory) factoryClass.newInstance();

    int pathSplit = 0;
    String pathSplitAsString = this.servletConfig.getInitParameter(ODataServiceFactory.PATH_SPLIT_LABEL);
    if (pathSplitAsString != null) {
      pathSplit = Integer.parseInt(pathSplitAsString);
    }
    
    InitParameter param = odataLocator.new InitParameter();
    param.setServiceFactory(serviceFactory);
    param.setPathSegments(pathSegments);
    param.setHttpHeaders(httpHeaders);
    param.setUriInfo(uriInfo);
    param.setRequest(request);
    param.setPathSplit(pathSplit);
    
    odataLocator.initializeService(param);

    return odataLocator;
  }
}