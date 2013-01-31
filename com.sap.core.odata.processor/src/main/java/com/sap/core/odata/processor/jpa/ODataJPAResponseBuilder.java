package com.sap.core.odata.processor.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;

public final class ODataJPAResponseBuilder {

	public static <T> ODataResponse build(List<T> jpaEntities,
			GetEntitySetUriInfo resultsView, String contentType,
			ODataJPAContext odataJPAContext) throws ODataJPARuntimeException {

		EdmEntityType edmEntityType = null;
		ODataResponse odataResponse = null;

		try {
			edmEntityType = resultsView.getTargetEntitySet().getEntityType();

			List<Map<String, Object>> edmEntityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> edmPropertyValueMap = null;

			JPAResultParser jpaResultParser = JPAResultParser.create();
			for (Object jpaEntity : jpaEntities) {
				edmPropertyValueMap = jpaResultParser
						.parse2EdmPropertyValueMap(jpaEntity, edmEntityType);
				edmEntityList.add(edmPropertyValueMap);
			}

			EntityProviderProperties feedProperties = null;
			;
			try {
				final Integer count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityList
						.size() : null;
				feedProperties = EntityProviderProperties
						.serviceRoot(
								odataJPAContext.getODataContext().getPathInfo()
										.getServiceRoot()).inlineCount(count)
						.skipToken("").build();
			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL.addContent(e
								.getMessage()), e);
			}

			odataResponse = ODataResponse
					.fromResponse(
							EntityProvider.writeFeed(contentType,
									resultsView.getTargetEntitySet(),
									edmEntityList, feedProperties))
					.status(HttpStatusCodes.OK).build();

		} catch (EntityProviderException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (EdmException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}

		return odataResponse;
	}

	public static ODataResponse build(Object jpaEntity,
			GetEntityUriInfo resultsView, String contentType,
			ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException {

		if (jpaEntity == null)
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.RESOURCE_NOT_FOUND,
					new ODataNotFoundException(ODataNotFoundException.ENTITY));

		EdmEntityType edmEntityType = null;
		ODataResponse odataResponse = null;

		try {

			edmEntityType = resultsView.getTargetEntitySet().getEntityType();
			Map<String, Object> edmPropertyValueMap = null;

			JPAResultParser jpaResultParser = JPAResultParser.create();
			edmPropertyValueMap = jpaResultParser.parse2EdmPropertyValueMap(
					jpaEntity, edmEntityType);

			EntityProviderProperties feedProperties = null;
			try {
				feedProperties = EntityProviderProperties.serviceRoot(
						oDataJPAContext.getODataContext().getPathInfo()
								.getServiceRoot()).build();
			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL.addContent(e
								.getMessage()), e);
			}

			odataResponse = ODataResponse
					.fromResponse(
							EntityProvider.writeEntry(contentType,
									resultsView.getTargetEntitySet(),
									edmPropertyValueMap, feedProperties))
					.status(HttpStatusCodes.OK).build();

		} catch (EntityProviderException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (EdmException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}

		return odataResponse;
	}
}
