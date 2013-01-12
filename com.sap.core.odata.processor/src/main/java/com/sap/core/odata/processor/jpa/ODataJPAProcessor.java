package com.sap.core.odata.processor.jpa;

import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;

public class ODataJPAProcessor extends ODataSingleProcessor {

	private ODataJPAContext oDataJPAContext;
	private JPAProcessor jpaProcessor;

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
	
	public ODataJPAProcessor( ){
		
	}
	
	@Override
	public ODataResponse readEntitySet(GetEntitySetUriInfo uriParserResultView,
			String contentType) throws ODataException {
		
		//Process OData Request
		List<Object> jpaEntities = this.jpaProcessor.process(uriParserResultView);

		// Build OData Response out of a JPA Response
		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
				jpaEntities, uriParserResultView, contentType, oDataJPAContext);

		return oDataResponse;
	}

	@Override
	public ODataResponse readEntity(GetEntityUriInfo uriParserResultView,
			String contentType) throws ODataException {
		
		//Process OData Request
		Object jpaEntity = this.jpaProcessor.process(uriParserResultView);
		
		// Build OData Response out of a JPA Response
		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
				jpaEntity, uriParserResultView, contentType, oDataJPAContext);
		
		return oDataResponse;
		
		
	}

}
