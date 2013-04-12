package com.sap.core.odata.processor.core.jpa;

import java.io.InputStream;
import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.ODataJPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAException;

public class ODataJPAProcessorDefault extends ODataJPAProcessor {

	public ODataJPAProcessorDefault(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
		if (oDataJPAContext == null) {
			throw new IllegalArgumentException(
					ODataJPAException.ODATA_JPACTX_NULL);
		}
	}

	@Override
	public ODataResponse readEntitySet(GetEntitySetUriInfo uriParserResultView,
			String contentType) throws ODataException {

		List<?> jpaEntities = this.jpaProcessor.process(uriParserResultView);

		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
				jpaEntities, uriParserResultView, contentType, oDataJPAContext);

		return oDataResponse;
	}

	@Override
	public ODataResponse readEntity(GetEntityUriInfo uriParserResultView,
			String contentType) throws ODataException {

		Object jpaEntity = this.jpaProcessor.process(uriParserResultView);

		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(jpaEntity,
				uriParserResultView, contentType, oDataJPAContext);

		return oDataResponse;
	}

	@Override
	public ODataResponse countEntitySet(GetEntitySetCountUriInfo uriParserResultView,
			String contentType) throws ODataException {

		long jpaEntityCount = this.jpaProcessor.process(uriParserResultView);

		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
				jpaEntityCount, oDataJPAContext);

		return oDataResponse;
	}
	

	@Override
	public ODataResponse existsEntity(GetEntityCountUriInfo uriInfo,
			String contentType) throws ODataException {
		
		long jpaEntityCount = this.jpaProcessor.process(uriInfo);

		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
				jpaEntityCount, oDataJPAContext);

		return oDataResponse;
	}

	@Override
	public ODataResponse createEntity(PostUriInfo uriParserResultView, InputStream content,
			String requestContentType, String contentType)
			throws ODataException {
		
		Object jpaEntity = this.jpaProcessor.process(uriParserResultView, content,
				requestContentType);

		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(jpaEntity,
				uriParserResultView, contentType, oDataJPAContext);

		return oDataResponse;
	}

	@Override
	public ODataResponse updateEntity(PutMergePatchUriInfo uriParserResultView,
			InputStream content, String requestContentType, boolean merge,
			String contentType) throws ODataException {

		Object jpaEntity = this.jpaProcessor.process(uriParserResultView, content,
				requestContentType);
		
		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(jpaEntity,
				uriParserResultView);

		return oDataResponse;
	}

	@Override
	public ODataResponse deleteEntity(DeleteUriInfo uriParserResultView,
			String contentType) throws ODataException {

		Object deletedObj = this.jpaProcessor.process(uriParserResultView,
				contentType);

		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(deletedObj,
				uriParserResultView);
		return oDataResponse;
	}

	@Override
	public ODataResponse executeFunctionImport(
			GetFunctionImportUriInfo uriParserResultView,
			String contentType) throws ODataException {

		List<Object> resultEntity = this.jpaProcessor
				.process(uriParserResultView);


		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
				resultEntity, uriParserResultView, contentType,
				oDataJPAContext);

		return oDataResponse;
	}

	@Override
	public ODataResponse executeFunctionImportValue(
			GetFunctionImportUriInfo uriParserResultView,
			String contentType) throws ODataException {

		List<Object> result = this.jpaProcessor.process(uriParserResultView);

		ODataResponse oDataResponse = ODataJPAResponseBuilder.build(result,
				uriParserResultView, contentType, oDataJPAContext);

		return oDataResponse;
	}

}
