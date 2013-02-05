package com.sap.core.odata.processor.core.jpa;

import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.ODataJPAProcessor;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;

public class ODataJPAProcessorDefault extends ODataJPAProcessor {

	public ODataJPAProcessorDefault(ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException {
		super(oDataJPAContext);
	}

	@Override
	public ODataResponse readEntitySet(GetEntitySetUriInfo uriParserResultView,
			String contentType) throws ODataException {
		
		//Process OData Request
		List<?> jpaEntities = this.jpaProcessor.process(uriParserResultView);

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
