package com.sap.core.odata.processor.core.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

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

			final List<SelectItem> selectedItems = resultsView.getSelect();
			if (selectedItems != null && selectedItems.size() > 0) {
				for (Object jpaEntity : jpaEntities) {
					edmPropertyValueMap = jpaResultParser
							.parse2EdmPropertyValueMap(jpaEntity, selectedItems);
					edmEntityList.add(edmPropertyValueMap);
				}
			} else {
				for (Object jpaEntity : jpaEntities) {
					edmPropertyValueMap = jpaResultParser
							.parse2EdmPropertyValueMap(jpaEntity, edmEntityType);
					edmEntityList.add(edmPropertyValueMap);
				}
			}

			EntityProviderProperties feedProperties = null;

			try {
				final Integer count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityList
						.size() : null;
				feedProperties = EntityProviderProperties
						.serviceRoot(
								odataJPAContext.getODataContext().getPathInfo()
										.getServiceRoot()).inlineCount(count)
						.inlineCountType(resultsView.getInlineCount()).build();
			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL.addContent(e
								.getMessage()), e);
			}

			odataResponse = EntityProvider.writeFeed(contentType,
					resultsView.getTargetEntitySet(), edmEntityList,
					feedProperties);
			odataResponse = ODataResponse.fromResponse(odataResponse)
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
			ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException,
			ODataNotFoundException {

		if (jpaEntity == null)
			throw new ODataNotFoundException(ODataNotFoundException.ENTITY); // Need
																				// to
																				// throw
																				// 404
																				// with
																				// Message
																				// body

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
						ODataJPARuntimeException.INNER_EXCEPTION, e);
			}

			odataResponse = EntityProvider.writeEntry(contentType,
					resultsView.getTargetEntitySet(), edmPropertyValueMap,
					feedProperties);

			odataResponse = ODataResponse.fromResponse(odataResponse)
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

	public static ODataResponse build(long jpaEntityCount,
			GetEntitySetCountUriInfo uriInfo, String contentType,
			ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException {

		ODataResponse odataResponse = null;
		try {
			odataResponse = EntityProvider.writeText(String
					.valueOf(jpaEntityCount));
			odataResponse = ODataResponse.fromResponse(odataResponse).build();
		} catch (EntityProviderException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}
		return odataResponse;
	}

	public static ODataResponse build(Object createdObject,
			PostUriInfo uriInfo, String contentType,
			ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException,
			ODataNotFoundException {

		if (createdObject == null)
			throw new ODataNotFoundException(ODataNotFoundException.ENTITY); // Need
																				// to
																				// throw
																				// 404
																				// with
																				// Message
																				// body

		EdmEntityType edmEntityType = null;
		ODataResponse odataResponse = null;

		try {

			edmEntityType = uriInfo.getTargetEntitySet().getEntityType();
			Map<String, Object> edmPropertyValueMap = null;

			JPAResultParser jpaResultParser = JPAResultParser.create();
			edmPropertyValueMap = jpaResultParser.parse2EdmPropertyValueMap(
					createdObject, edmEntityType);

			EntityProviderProperties feedProperties = null;
			try {
				feedProperties = EntityProviderProperties.serviceRoot(
						oDataJPAContext.getODataContext().getPathInfo()
								.getServiceRoot()).build();
			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.INNER_EXCEPTION, e);
			}

			odataResponse = EntityProvider.writeEntry(contentType,
					uriInfo.getTargetEntitySet(), edmPropertyValueMap,
					feedProperties);

			odataResponse = ODataResponse.fromResponse(odataResponse)
					.status(HttpStatusCodes.CREATED).build(); // Send status
																// code along
																// with body of
																// created
																// content

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

	public static ODataResponse build(Object updatedObject,
			PutMergePatchUriInfo putUriInfo) throws ODataJPARuntimeException,
			ODataNotFoundException {
		if (updatedObject == null)
			throw new ODataNotFoundException(ODataNotFoundException.ENTITY); // Need
																				// to
																				// throw
																				// 404
																				// with
																				// Message
																				// body

		return ODataResponse.status(HttpStatusCodes.ACCEPTED).build(); // Body
																		// is
																		// not
																		// needed
																		// for
																		// successful
																		// update;
	}

	public static ODataResponse build(Object deletedObject,
			DeleteUriInfo deleteUriInfo) throws ODataJPARuntimeException,
			ODataNotFoundException {

		if (deletedObject == null) {
			throw new ODataNotFoundException(ODataNotFoundException.ENTITY); // Need
																				// to
																				// throw
																				// 404
																				// with
																				// Message
																				// body
		}
		return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build(); // Body
																			// is
																			// not
																			// needed
																			// for
																			// successful
																			// delete;
	}

	public static ODataResponse build(Object result,
			GetFunctionImportUriInfo resultsView)
			throws ODataJPARuntimeException {

		try {
			final EdmFunctionImport functionImport = resultsView
					.getFunctionImport();
			final EdmSimpleType type = (EdmSimpleType) functionImport
					.getReturnType().getType();

			if (result != null) {
				ODataResponse response = null;

				final String value = type.valueToString(result,
						EdmLiteralKind.DEFAULT, null);
				response = EntityProvider.writeText(value);

				return ODataResponse.fromResponse(response).build();
			} else
				throw new ODataNotFoundException(ODataNotFoundException.COMMON);
		} catch (EdmException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (EntityProviderException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (ODataException e) {
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.INNER_EXCEPTION, e);
		}
	}

	public static ODataResponse build(List<Object> resultList,
			GetFunctionImportUriInfo resultsView, String contentType,
			ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException,
			ODataNotFoundException {

		ODataResponse odataResponse = null;

		if (resultList != null && !resultList.isEmpty()) {
			JPAResultParser jpaResultParser = JPAResultParser.create();
			EdmType edmType = null;
			EdmFunctionImport functionImport = null;
			Map<String, Object> edmPropertyValueMap = null;
			List<Map<String, Object>> edmEntityList = null;
			Object result = null;
			try {
				EntityProviderProperties feedProperties = null;

				feedProperties = EntityProviderProperties.serviceRoot(
						oDataJPAContext.getODataContext().getPathInfo()
								.getServiceRoot()).build();

				functionImport = resultsView.getFunctionImport();
				edmType = functionImport.getReturnType().getType();

				if (edmType.getKind().equals(EdmTypeKind.ENTITY) || edmType.getKind().equals(EdmTypeKind.COMPLEX)) {
					if (functionImport.getReturnType().getMultiplicity()
							.equals(EdmMultiplicity.MANY)) {
						edmEntityList = new ArrayList<Map<String, Object>>();
						for (Object jpaEntity : resultList) {
							edmPropertyValueMap = jpaResultParser
									.parse2EdmPropertyValueMap(jpaEntity,
											(EdmStructuralType) edmType);
							edmEntityList.add(edmPropertyValueMap);
						}
						result = edmEntityList;
					} else {

						Object resultObject = resultList.get(0);
						edmPropertyValueMap = jpaResultParser
								.parse2EdmPropertyValueMap(resultObject,
										(EdmStructuralType) edmType);

						result = edmPropertyValueMap;
					}

				} else if (edmType.getKind().equals(EdmTypeKind.SIMPLE)) {
					result = resultList.get(0);
				}

				odataResponse = EntityProvider
						.writeFunctionImport(contentType,
								resultsView.getFunctionImport(), result,
								feedProperties);
				odataResponse = ODataResponse.fromResponse(odataResponse)
						.status(HttpStatusCodes.OK).build();

			} catch (EdmException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL.addContent(e
								.getMessage()), e);
			} catch (EntityProviderException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL.addContent(e
								.getMessage()), e);
			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.INNER_EXCEPTION, e);
			}

		} else
			throw new ODataNotFoundException(ODataNotFoundException.COMMON);

		return odataResponse;
	}
}
