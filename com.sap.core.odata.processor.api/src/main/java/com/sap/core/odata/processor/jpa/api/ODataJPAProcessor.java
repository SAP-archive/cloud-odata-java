package com.sap.core.odata.processor.jpa.api;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.factory.JPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;

/**
 * Extend this class and implement an OData JPA processor if the default
 * behavior of OData JPA Processor library has to be overwritten.
 * 
 * @author SAP AG
 * 
 * 
 */
public abstract class ODataJPAProcessor extends ODataSingleProcessor {

	/**
	 * An instance of {@link ODataJPAContext} object
	 */
	protected ODataJPAContext oDataJPAContext;

	/**
	 * An instance of {@link JPAProcessor}. The instance is created using {@link JPAAccessFactory}.
	 */
	protected JPAProcessor jpaProcessor;

	public ODataJPAContext getOdataJPAContext() {
		return oDataJPAContext;
	}

	public void setOdataJPAContext(ODataJPAContext odataJPAContext) {
		this.oDataJPAContext = odataJPAContext;
	}

	/**
	 * Constructor
	 * 
	 * @param oDataJPAContext
	 *            non null OData JPA Context object
	 * @throws ODataJPARuntimeException 
	 */
	public ODataJPAProcessor(ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException {
		if(oDataJPAContext == null)
		{
			ODataJPARuntimeException.throwException(ODataJPARuntimeException.RUNTIME_EXCEPTION, null);
		}
		this.oDataJPAContext = oDataJPAContext;
		jpaProcessor = ODataJPAFactory.createFactory().getJPAAccessFactory()
				.getJPAProcessor(this.oDataJPAContext);
	}

}
