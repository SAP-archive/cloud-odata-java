package com.sap.core.odata.api.service;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;

/**
 * Implement this interface to create own instance of ODataProcessor. Usually the factory implementation is passed as servlet
 * init parameter to a JAX-RS runtime which will instantiate a ODataProcessor implementation using this factory:<p>
 *  
 * <pre>
 * {@code
 * <servlet>
 *  <servlet-name>ReferenceScenarioServlet</servlet-name><br>
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
 *  } 
 *  </pre>
 * 
 * com.sap.core.odata.processor.factory
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
   * Create instance of custom processor.
   * @return A new processor instance.
   * @throws ODataException is thrown in any case of error.
   */
  ODataProcessor createProcessor() throws ODataException;
  
  /**
   * Create instance of custom provider.
   * @return A new provider instance.
   * @throws ODataException is thrown in any case of error.
   */
  EdmProvider createProvider() throws ODataException;
}