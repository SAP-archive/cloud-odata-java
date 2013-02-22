package com.sap.core.odata.core.rest;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Encoded;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.rest.ODataSubLocator.InitParameter;

/**
 * Default OData root locator responsible to handle the whole path and delegate all calls to a sub locator:<p>
 * <code>/{odata path}  e.g. http://host:port/webapp/odata.svc/$metadata</code><br>
 * All path segments defined by a servlet mapping belong to the odata uri.
 * </p>
 * This behavior can be changed:<p>
 * <code>/{custom path}{odata path}  e.g. http://host:port/webapp/bmw/odata.svc/$metadata</code><br>
 * The first segment defined by a servlet mapping belong to customer context and the following segments are OData specific.
 * </p>
 * @author SAP AG
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
  @Context
  private HttpServletRequest servletRequest;

  /**
   * Default root behavior which will delegate all paths to a ODataLocator.
   * @param pathSegments        URI path segments - all segments have to be OData
   * @param xHttpMethod         HTTP Header X-HTTP-Method for tunneling through POST
   * @param xHttpMethodOverride HTTP Header X-HTTP-Method-Override for tunneling through POST
   * @return a locator handling OData protocol
   * @throws ODataException 
   * @throws ClassNotFoundException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   */
  @Path("/{pathSegments: .*}")
  public ODataLocator handleRequest(
      @Encoded @PathParam("pathSegments") List<PathSegment> pathSegments,
      @HeaderParam("X-HTTP-Method") String xHttpMethod,
      @HeaderParam("X-HTTP-Method-Override") String xHttpMethodOverride)
          throws ODataException, ClassNotFoundException, InstantiationException, IllegalAccessException {

    if (xHttpMethod != null && xHttpMethodOverride != null) {

      /*
       * X-HTTP-Method-Override : implemented by CXF
       * X-HTTP-Method          : implemented in ODataSubLocator:handlePost
       */

      if (!xHttpMethod.equalsIgnoreCase(xHttpMethodOverride)) {
        throw new ODataBadRequestException(ODataBadRequestException.AMBIGUOUS_XMETHOD);
      }
    }

    final ODataSubLocator odataLocator = new ODataSubLocator();

    if (servletRequest.getPathInfo() == null) {
      return handleRedirect();
    }

    final String factoryClassName = servletConfig.getInitParameter(ODataServiceFactory.FACTORY_LABEL);
    if (factoryClassName == null) {
      throw new ODataRuntimeException("servlet config missing: com.sap.core.odata.processor.factory");
    }
    final Class<?> factoryClass = Class.forName(factoryClassName);
    final ODataServiceFactory serviceFactory = (ODataServiceFactory) factoryClass.newInstance();

    int pathSplit = 0;
    final String pathSplitAsString = servletConfig.getInitParameter(ODataServiceFactory.PATH_SPLIT_LABEL);
    if (pathSplitAsString != null) {
      pathSplit = Integer.parseInt(pathSplitAsString);
    }

    final InitParameter param = odataLocator.new InitParameter();
    param.setServiceFactory(serviceFactory);
    param.setPathSegments(pathSegments);
    param.setHttpHeaders(httpHeaders);
    param.setUriInfo(uriInfo);
    param.setRequest(request);
    param.setServletRequest(servletRequest);
    param.setPathSplit(pathSplit);

    odataLocator.initialize(param);

    return odataLocator;
  }

  private ODataLocator handleRedirect() {
    return new ODataRedirectLocator();
  }
}