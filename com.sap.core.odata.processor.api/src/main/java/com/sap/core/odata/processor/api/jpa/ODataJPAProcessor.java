package com.sap.core.odata.processor.api.jpa;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

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
	 * An instance of
	 * {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext} object
	 */
	protected ODataJPAContext oDataJPAContext;

	/**
	 * An instance of
	 * {@link com.sap.core.odata.processor.api.jpa.access.JPAProcessor}. The
	 * instance is created using
	 * {@link com.sap.core.odata.processor.api.jpa.factory.JPAAccessFactory}.
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
	 */
	public ODataJPAProcessor(ODataJPAContext oDataJPAContext) {
		if (oDataJPAContext == null) {
			throw new IllegalArgumentException(
					ODataJPAException.ODATA_JPACTX_NULL);
		}
		this.oDataJPAContext = oDataJPAContext;
		jpaProcessor = ODataJPAFactory.createFactory().getJPAAccessFactory()
				.getJPAProcessor(this.oDataJPAContext);
	}

}
