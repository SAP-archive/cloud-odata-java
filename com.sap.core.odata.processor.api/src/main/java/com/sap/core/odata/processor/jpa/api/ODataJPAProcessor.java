package com.sap.core.odata.processor.jpa.api;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;

public abstract class ODataJPAProcessor extends ODataSingleProcessor {

	protected ODataJPAContext oDataJPAContext;
	protected JPAProcessor jpaProcessor;

	public ODataJPAContext getOdataJPAContext() {
		return oDataJPAContext;
	}

	public void setOdataJPAContext(ODataJPAContext odataJPAContext) {
		this.oDataJPAContext = odataJPAContext;
	}
	
	public ODataJPAProcessor(ODataJPAContext oDataJPAContext){
		this.oDataJPAContext = oDataJPAContext;
		jpaProcessor = ODataJPAFactory.createFactory().getJPAAccessFactory().getJPAProcessor(this.oDataJPAContext);
	}

}
