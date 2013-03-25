package com.sap.core.odata.processor.core.jpa;

import java.io.InputStream;
import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.ODataJPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAException;

public class ODataJPAProcessorDefault extends ODataJPAProcessor {

	public ODataJPAProcessorDefault(ODataJPAContext oDataJPAContext){
		super(oDataJPAContext);
		if(oDataJPAContext == null){
			throw new IllegalArgumentException(ODataJPAException.ODATA_JPACTX_NULL);
		}
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

	@Override
	public ODataResponse countEntitySet(GetEntitySetCountUriInfo uriInfo,
			String contentType) throws ODataException {
		
		//Process OData Request
		long jpaEntityCount = this.jpaProcessor.process(uriInfo);
		 
		// Build OData Response out of a JPA Response
		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
					jpaEntityCount, uriInfo, contentType, oDataJPAContext);
		 
		return oDataResponse;
	}
	
	@Override
	public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content,
			String requestContentType, String contentType)
			throws ODataException {
		//Process OData Request
				Object jpaEntity = this.jpaProcessor.process(uriInfo, content,requestContentType);
				uriInfo.getTargetEntitySet().getEntityType();
				// Build OData Response out of a JPA Response
				ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
						jpaEntity, uriInfo, contentType, oDataJPAContext);
				
				return oDataResponse;
	}
	
	

}
