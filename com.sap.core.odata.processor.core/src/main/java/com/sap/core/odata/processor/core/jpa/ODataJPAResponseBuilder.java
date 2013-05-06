/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.access.data.JPAExpandCallBack;

public final class ODataJPAResponseBuilder {

	public static <T> ODataResponse build(List<T> jpaEntities,
			GetEntitySetUriInfo resultsView, String contentType,
			ODataJPAContext odataJPAContext) throws ODataJPARuntimeException {

		EdmEntityType edmEntityType = null;
		ODataResponse odataResponse = null;
		List<ArrayList<NavigationPropertySegment>> expandList = null;

		try {
			edmEntityType = resultsView.getTargetEntitySet().getEntityType();
			List<Map<String, Object>> edmEntityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> edmPropertyValueMap = null;
			JPAResultParser jpaResultParser = JPAResultParser.create();
			final List<SelectItem> selectedItems = resultsView.getSelect();
			if (selectedItems != null && selectedItems.size() > 0) {
				for (Object jpaEntity : jpaEntities) {
					edmPropertyValueMap = jpaResultParser
							.parse2EdmPropertyValueMapFromList(
									jpaEntity,
									buildSelectItemList(selectedItems,
											edmEntityType));
					edmEntityList.add(edmPropertyValueMap);
				}
			} else {
				for (Object jpaEntity : jpaEntities) {
					edmPropertyValueMap = jpaResultParser
							.parse2EdmPropertyValueMap(jpaEntity, edmEntityType);
					edmEntityList.add(edmPropertyValueMap);
				}
			}
			expandList = resultsView.getExpand();
			if (expandList != null && expandList.size() != 0) {
				int count = 0;
				for (Object jpaEntity : jpaEntities) {
					jpaResultParser.parse2EdmPropertyListMap(
							edmEntityList.get(count), jpaEntity,
							constructListofNavProperty(expandList));
					count++;
				}
			}
			
			EntityProviderWriteProperties feedProperties = null;
			// Getting the entity feed properties
			feedProperties = getEntityProviderProperties(odataJPAContext,
					resultsView, edmEntityList);
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

		List<ArrayList<NavigationPropertySegment>> expandList = null;
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
			final List<SelectItem> selectedItems = resultsView.getSelect();
			if (selectedItems != null && selectedItems.size() > 0) {
				edmPropertyValueMap = jpaResultParser
						.parse2EdmPropertyValueMapFromList(
								jpaEntity,
								buildSelectItemList(selectedItems, resultsView
										.getTargetEntitySet().getEntityType()));
			} else
				edmPropertyValueMap = jpaResultParser
						.parse2EdmPropertyValueMap(jpaEntity, edmEntityType);

			expandList = resultsView.getExpand();
			if (expandList != null && expandList.size() != 0)
				jpaResultParser.parse2EdmPropertyListMap(edmPropertyValueMap,
						jpaEntity, constructListofNavProperty(expandList));
			EntityProviderWriteProperties feedProperties = null;
			feedProperties = getEntityProviderProperties(oDataJPAContext,
					resultsView);
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
	
	public static ODataResponse build(List<Object> createdObjectList,
			PostUriInfo uriInfo, String contentType,
			ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException,
			ODataNotFoundException {

		if (createdObjectList == null || createdObjectList.size() == 0 || createdObjectList.get(0) == null)
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
					createdObjectList.get(0), edmEntityType);

			EntityProviderWriteProperties feedProperties = null;
			try {
				feedProperties = EntityProviderWriteProperties.serviceRoot(
						oDataJPAContext.getODataContext().getPathInfo()
								.getServiceRoot()).expandSelectTree((ExpandSelectTreeNode) createdObjectList.get(1)).build();
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
				EntityProviderWriteProperties feedProperties = null;

				feedProperties = EntityProviderWriteProperties.serviceRoot(
						oDataJPAContext.getODataContext().getPathInfo()
								.getServiceRoot()).build();

				functionImport = resultsView.getFunctionImport();
				edmType = functionImport.getReturnType().getType();

				if (edmType.getKind().equals(EdmTypeKind.ENTITY)
						|| edmType.getKind().equals(EdmTypeKind.COMPLEX)) {
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

	/*
	 * Method to build the entity provider Property.Callbacks for $expand would
	 * be registered here
	 */
	private static EntityProviderWriteProperties getEntityProviderProperties(
			ODataJPAContext odataJPAContext, GetEntitySetUriInfo resultsView,
			List<Map<String, Object>> edmEntityList)
			throws ODataJPARuntimeException {
		ODataEntityProviderPropertiesBuilder entityFeedPropertiesBuilder = null;
		Integer count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityList
				.size() : null;
		try {
			entityFeedPropertiesBuilder = EntityProviderWriteProperties
					.serviceRoot(odataJPAContext.getODataContext()
							.getPathInfo().getServiceRoot());
			entityFeedPropertiesBuilder.inlineCount(count);
			entityFeedPropertiesBuilder.inlineCountType(resultsView
					.getInlineCount());
			ExpandSelectTreeNode expandSelectTree = UriParser
					.createExpandSelectTree(resultsView.getSelect(),
							resultsView.getExpand());
			entityFeedPropertiesBuilder.callbacks(JPAExpandCallBack
					.getCallbacks(odataJPAContext.getODataContext()
							.getPathInfo().getServiceRoot(), expandSelectTree,
							resultsView.getExpand()));
			entityFeedPropertiesBuilder.expandSelectTree(expandSelectTree);

		} catch (ODataException e) {
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.INNER_EXCEPTION, e);
		}

		return entityFeedPropertiesBuilder.build();
	}
	private static EntityProviderWriteProperties getEntityProviderProperties(
			ODataJPAContext odataJPAContext, GetEntityUriInfo resultsView)
			throws ODataJPARuntimeException {
		ODataEntityProviderPropertiesBuilder entityFeedPropertiesBuilder = null;
		ExpandSelectTreeNode expandSelectTree = null;
		try {
			entityFeedPropertiesBuilder = EntityProviderWriteProperties
					.serviceRoot(odataJPAContext.getODataContext()
							.getPathInfo().getServiceRoot());
			expandSelectTree = UriParser.createExpandSelectTree(
					resultsView.getSelect(), resultsView.getExpand());
			entityFeedPropertiesBuilder.expandSelectTree(expandSelectTree);
			entityFeedPropertiesBuilder.callbacks(JPAExpandCallBack
					.getCallbacks(odataJPAContext.getODataContext()
							.getPathInfo().getServiceRoot(), expandSelectTree,
							resultsView.getExpand()));
		} catch (ODataException e) {
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.INNER_EXCEPTION, e);
		}

		return entityFeedPropertiesBuilder.build();
	}


	private static List<EdmProperty> buildSelectItemList(
			List<SelectItem> selectItems, EdmEntityType entity) throws ODataJPARuntimeException {
		boolean flag = false;
		List<EdmProperty> selectPropertyList = new ArrayList<EdmProperty>();
		try {
			for (SelectItem selectItem : selectItems)
				selectPropertyList.add(selectItem.getProperty());
			for (EdmProperty keyProperty : entity.getKeyProperties()) {
				flag = true;
				for (SelectItem selectedItem : selectItems) {
					if (selectedItem.getProperty().equals(keyProperty)) {
						flag = false;
						break;
					}
				}
				if (flag == true)
					selectPropertyList.add(keyProperty);
			}

		} catch (EdmException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}
		return selectPropertyList;
	}
	private static List<EdmNavigationProperty> constructListofNavProperty(
			List<ArrayList<NavigationPropertySegment>> expandList) {
		List<EdmNavigationProperty> navigationPropertyList = new ArrayList<EdmNavigationProperty>();
		for (ArrayList<NavigationPropertySegment> navpropSegment : expandList)
			navigationPropertyList.add(navpropSegment.get(0)
					.getNavigationProperty());
		return navigationPropertyList;
	}

}
