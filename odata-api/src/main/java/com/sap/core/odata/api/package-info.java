/**
 * <h1>OData library API.</h1>
 *
 * While using this library a custom OData service can be implemented
 * for providing metadata and offering CRUD operations on data sources
 * following the OData V2.0 protocol specification.
 * See <a href="http://odata.org/">odata.org</a>. 
 * <p>
 * Start using this library with a standard JEE web application.
 * Additionally to this library a JAX-RS runtime library is required
 * which can be, for instance, Jersey or CXF or any other implementation.
 * This library is proofed and used with CXF.<p>
 * <p>
 * Define a JAX-RS servlet in web.xml and set following init parameters:
 * 
 * <table border="1">
 *  <tr>
 *    <td>
 *    javax.ws.rs.Application
 *    </td>
 *    <td>
 *    com.sap.core.odata.core.rest.app.ODataApplication
 *    </td>
 *    <td>
 *    a standard JAX-RS application which is provided by the library
 *    </td>
 *  </tr>
 *  <tr>
 *    <td>
 *    com.sap.core.odata.service.factor
 *    </td>
 *    <td>
 *    com.sap.sample.ServiceFactory
 *    </td>
 *    <td>
 *    A factory implementation which is the starting point for custom OData services.
 *    </td>
 *  </tr>
 * </table>
 * 
 * An OData service consists of a {@link EdmProvider} for serving metadata
 * and a processor for handling data. 
 * For convenience this library offers an {@link ODataSingleProcessorService}
 * and an {@link ODataSingleProcessor} and it is recommended to use and subclass
 * this classes for custom service implementation. 
 *
 * <pre>
 * {@code
 * <servlet>
 *  <servlet-name>ReferenceScenarioServlet</servlet-name>
 *  <servlet-class>org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet</servlet-class>
 *  <init-param>
 *    <param-name>javax.ws.rs.Application</param-name>
 *    <param-value>com.sap.core.odata.core.rest.app.ODataApplication</param-value>
 *  </init-param>
 *  <init-param>
 *    <param-name>com.sap.core.odata.service.factory</param-name>
 *    <param-value>com.sap.sample.ServiceFactory</param-value>
 *  </init-param>
 *  <load-on-startup>1</load-on-startup>
 * </servlet>
 * }
 * </pre>
 */
package com.sap.core.odata.api;
