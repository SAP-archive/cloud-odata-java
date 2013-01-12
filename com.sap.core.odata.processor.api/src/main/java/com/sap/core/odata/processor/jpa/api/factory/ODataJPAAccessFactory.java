package com.sap.core.odata.processor.jpa.api.factory;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

/**
 * Factory Interface for creating following instances
 * 
 * <p>
 * <li>OData JPA Processor of type {@link ODataSingleProcessor}</li>
 * <li>JPA Edm Provider of type {@link EdmProvider}</li>
 * <li>OData JPA Context {@link ODataJPAContext}</li>
 * </p>
 * 
 * @author SAP AG
 * 
 */
public interface ODataJPAAccessFactory {
	/**
	 * Method creates an instance of type {@link ODataSingleProcessor}. The
	 * processor handles runtime behavior of an OData service.
	 * 
	 * @param oDataJPAContext
	 *            an instance of type {@link ODataJPAContext}. The context
	 *            should be initialized properly.
	 * @return An implementation of OData JPA Processor.
	 */
	public ODataSingleProcessor createODataProcessor(
			ODataJPAContext oDataJPAContext);

	/**
	 * 
	 * @param oDataJPAContext
	 *            an instance of type {@link ODataJPAContext}. The context
	 *            should be initialized properly.
	 * @return An implementation of JPA EdmProvider. EdmProvider handles
	 *         metadata.
	 */
	public EdmProvider createJPAEdmProvider(ODataJPAContext oDataJPAContext);

	/**
	 * 
	 * @return
	 */
	public ODataJPAContext createODataJPAContext();
}
