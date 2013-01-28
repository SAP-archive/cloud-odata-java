package com.sap.core.odata.processor.jpa.api;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

/**
 * <p>
 * Extend this class and create own instance of {@link ODataService}. The
 * factory class instantiates instances of type {@link EdmProvider} and
 * {@link ODataSingleProcessor}.
 * </p>
 * <p>
 * Implement this interface to create own instance of {@link ODataService}.
 * Usually the factory implementation is passed as servlet init parameter to a
 * JAX-RS runtime which will instantiate a {@link ODataService} implementation
 * using this factory.
 * </p>
 * 
 * <p>
 * Implement the abstract method initializeJPAContext. Fill
 * {@link ODataJPAContext} with context values
 * </p>
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
	 * {@link ODataJPAContext} and {@link ODataContext}.
	 */
	public final ODataService createService(ODataContext ctx) throws ODataException {

		// Initialize OData JPA Context
		oDataJPAContext = initializeJPAContext();

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
	 * to set an instance of type {@link EntitManagerFactory} into the context.
	 * An exception of type {@link ODataJPARuntimeException} is thrown if
	 * EntityManagerFactory is not initialized. <br>
	 * <br>
	 * <b>Sample Code:</b> <code>
	 * 	<p>public class JPAReferenceServiceFactory extends ODataJPAServiceFactory{</p>
	 * 	
	 * 	<blockquote>private static final String PUNIT_NAME = "punit";
	 * <br>
	 * public ODataJPAContext initializeJPAContext() { 
	 * <blockquote>ODataJPAContext oDataJPAContext = this.getODataJPAContext();
	 * <br>
	 * EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNIT_NAME);
	 * <br>
	 * oDataJPAContext.setEntityManagerFactory(emf);
	 * oDataJPAContext.setPersistenceUnitName(PUNIT_NAME);
	 * <br> return oDataJPAContext;</blockquote>
	 * }</blockquote>
	 * } </code> <br>
	 * 
	 * @return an instance of type {@link ODataJPAContext}
	 */
	public abstract ODataJPAContext initializeJPAContext();

	/**
	 * @return an instance of type {@link ODataJPAContext}
	 */
	public final ODataJPAContext getODataJPAContext() {
		if (oDataJPAContext == null) {
			oDataJPAContext = ODataJPAFactory.createFactory()
					.getODataJPAAccessFactory().createODataJPAContext();
		}
		return oDataJPAContext;

	}
}
