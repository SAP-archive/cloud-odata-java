package com.sap.core.odata.processor.api.jpa.factory;

import java.util.Locale;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAMessageService;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

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
			ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException;

	/**
	 * 
	 * @param oDataJPAContext
	 *            an instance of type {@link ODataJPAContext}. The context
	 *            should be initialized properly.
	 * @return An implementation of JPA EdmProvider. EdmProvider handles
	 *         metadata.
	 */
	public EdmProvider createJPAEdmProvider(ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException;

	/**
	 * 
	 * @return
	 */
	public ODataJPAContext createODataJPAContext() throws ODataJPARuntimeException;
	
	public ODataJPAMessageService getODataJPAMessageService(Locale locale) throws ODataJPARuntimeException;
}
