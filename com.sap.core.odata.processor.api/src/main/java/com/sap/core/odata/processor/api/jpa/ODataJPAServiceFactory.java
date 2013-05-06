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
package com.sap.core.odata.processor.api.jpa;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAAccessFactory;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

/**
 * <p>
 * Extend this factory class and create own instance of
 * {@link com.sap.core.odata.api.ODataService} that transforms Java Persistence
 * Models into an OData Service. The factory class instantiates instances of
 * type {@link com.sap.core.odata.api.edm.provider.EdmProvider} and
 * {@link com.sap.core.odata.api.processor.ODataSingleProcessor}. The OData
 * JPA Processor library provides a default implementation for EdmProvider and
 * OData Single Processor.
 * </p>
 * <p>
 * The factory implementation is passed as servlet init parameter to a JAX-RS
 * runtime which will instantiate a {@link com.sap.core.odata.api.ODataService}
 * implementation using this factory.
 * </p>
 * 
 * <p>
 * <b>Mandatory:</b> Implement the abstract method initializeODataJPAContext. Fill
 * {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext} with context
 * values.
 * </p>
 * 
 * <b>Sample Configuration:</b>
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

public abstract class ODataJPAServiceFactory extends ODataServiceFactory {

	private ODataJPAContext oDataJPAContext;

	/**
	 * Creates an OData Service based on the values set in
	 * {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext} and
	 * {@link com.sap.core.odata.api.processor.ODataContext}.
	 */
	public final ODataService createService(ODataContext ctx)
			throws ODataException {

		// Initialize OData JPA Context
		oDataJPAContext = initializeODataJPAContext();

		validatePreConditions();

		ODataJPAFactory factory = ODataJPAFactory.createFactory();
		ODataJPAAccessFactory accessFactory = factory
				.getODataJPAAccessFactory();

		// OData JPA Processor
		oDataJPAContext.setODataContext(ctx);
		ODataSingleProcessor odataJPAProcessor = accessFactory
				.createODataProcessor(oDataJPAContext);

		// OData Entity Data Model Provider based on JPA
		EdmProvider edmProvider = accessFactory
				.createJPAEdmProvider(oDataJPAContext);

		return createODataSingleProcessorService(edmProvider, odataJPAProcessor);
	}

	private void validatePreConditions() throws ODataJPARuntimeException {

		if (oDataJPAContext.getEntityManagerFactory() == null) {
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.ENTITY_MANAGER_NOT_INITIALIZED,
					null);
		}

	}

	/**
	 * Implement this method and initialize OData JPA Context. It is mandatory
	 * to set an instance of type {@link javax.persistence.EntityManagerFactory}
	 * into the context. An exception of type
	 * {@link com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException}
	 * is thrown if EntityManagerFactory is not initialized. <br>
	 * <br>
	 * <b>Sample Code:</b> <code>
	 * 	<p>public class JPAReferenceServiceFactory extends ODataJPAServiceFactory{</p>
	 * 	
	 * 	<blockquote>private static final String PUNIT_NAME = "punit";
	 * <br>
	 * public ODataJPAContext initializeODataJPAContext() { 
	 * <blockquote>ODataJPAContext oDataJPAContext = this.getODataJPAContext();
	 * <br>
	 * EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNIT_NAME);
	 * <br>
	 * oDataJPAContext.setEntityManagerFactory(emf);
	 * oDataJPAContext.setPersistenceUnitName(PUNIT_NAME);
	 * <br> return oDataJPAContext;</blockquote>
	 * }</blockquote>
	 * } </code>
	 * <p>
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext}
	 * @throws ODataJPARuntimeException
	 */
	public abstract ODataJPAContext initializeODataJPAContext()
			throws ODataJPARuntimeException;

	/**
	 * @return an instance of type {@link ODataJPAContext}
	 * @throws ODataJPARuntimeException
	 */
	public final ODataJPAContext getODataJPAContext()
			throws ODataJPARuntimeException {
		if (oDataJPAContext == null) {
			oDataJPAContext = ODataJPAFactory.createFactory()
					.getODataJPAAccessFactory().createODataJPAContext();
		}
		return oDataJPAContext;

	}
}
