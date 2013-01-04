package com.sap.core.odata.api;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;

/**
 * <p>Implement this interface to create own instance of {@link ODataService}.</p>
 * <p>Usually the factory implementation is passed as servlet init parameter
 * to a JAX-RS runtime which will instantiate a {@link ODataService}
 * implementation using this factory.
 * For convenience it is recommended to sub class {@link ODataSingleProcessorService}.</p>
 *
 * <pre>
 * {@code
 * <servlet>
 *  <servlet-name>ReferenceScenarioServlet</servlet-name>
 *  <servlet-class>org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet</servlet-class>
 *  <init-param>
 *    <param-name>javax.ws.rs.Application</param-name>
 *    <param-value>com.sap.core.odata.core.rest.ODataApplication</param-value>
 *  </init-param>
 *  <init-param>
 *    <param-name>com.sap.core.odata.processor.factory</param-name>
 *    <param-value>com.sap.sample.processor.SampleProcessorFactory</param-value>
 *  </init-param>
 *  <init-param>
 *    <param-name>com.sap.core.odata.path.split</param-name>
 *    <param-value>2</param-value>
 *  </init-param>
 *  <load-on-startup>1</load-on-startup>
 * </servlet>
 * }
 * </pre>
 */
public interface ODataServiceFactory {

  /**
   * Label used in web.xml to assign servlet init parameter to factory class instance.
   */
  public static final String FACTORY_LABEL = "com.sap.core.odata.service.factory";

  /**
   * Label used in web.xml to assign servlet init parameter for a path split (service resolution).
   */
  public static final String PATH_SPLIT_LABEL = "com.sap.core.odata.path.split";

  /**
   * Create instance of custom {@link ODataService}.
   * @param ctx OData context object
   * @return A new service instance.
   * @throws ODataException in case of error
   */
  ODataService createService(ODataContext ctx) throws ODataException;
}