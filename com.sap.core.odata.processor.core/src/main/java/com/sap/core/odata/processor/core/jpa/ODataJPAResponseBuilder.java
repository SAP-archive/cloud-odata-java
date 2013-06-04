package com.sap.core.odata.processor.core.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntitySet;
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
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.access.data.JPAEntityParser;
import com.sap.core.odata.processor.core.jpa.access.data.JPAExpandCallBack;

public final class ODataJPAResponseBuilder {

  /* Response for Read Entity Set */
  public static <T> ODataResponse build(final List<T> jpaEntities,
      final GetEntitySetUriInfo resultsView, final String contentType,
      final ODataJPAContext odataJPAContext) throws ODataJPARuntimeException {

    EdmEntityType edmEntityType = null;
    ODataResponse odataResponse = null;
    List<ArrayList<NavigationPropertySegment>> expandList = null;

    try {
      edmEntityType = resultsView.getTargetEntitySet().getEntityType();
      List<Map<String, Object>> edmEntityList = new ArrayList<Map<String, Object>>();
      Map<String, Object> edmPropertyValueMap = null;
      JPAEntityParser jpaResultParser = JPAEntityParser.create();
      final List<SelectItem> selectedItems = resultsView.getSelect();
      if (selectedItems != null && selectedItems.size() > 0) {
        for (Object jpaEntity : jpaEntities) {
          edmPropertyValueMap = jpaResultParser
              .parse2EdmPropertyValueMap(
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
          Map<String, Object> relationShipMap = edmEntityList.get(count);
          HashMap<String, Object> navigationMap = jpaResultParser.parse2EdmNavigationValueMap(
              jpaEntity, constructListofNavProperty(expandList));
          relationShipMap.putAll(navigationMap);
          count++;
        }
      }

      EntityProviderWriteProperties feedProperties = null;

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

  /* Response for Read Entity */
  public static ODataResponse build(final Object jpaEntity,
      final GetEntityUriInfo resultsView, final String contentType,
      final ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException,
      ODataNotFoundException {

    List<ArrayList<NavigationPropertySegment>> expandList = null;
    if (jpaEntity == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }
    EdmEntityType edmEntityType = null;
    ODataResponse odataResponse = null;

    try {

      edmEntityType = resultsView.getTargetEntitySet().getEntityType();
      Map<String, Object> edmPropertyValueMap = null;

      JPAEntityParser jpaResultParser = JPAEntityParser.create();
      final List<SelectItem> selectedItems = resultsView.getSelect();
      if (selectedItems != null && selectedItems.size() > 0) {
        edmPropertyValueMap = jpaResultParser
            .parse2EdmPropertyValueMap(
                jpaEntity,
                buildSelectItemList(selectedItems, resultsView
                    .getTargetEntitySet().getEntityType()));
      } else {
        edmPropertyValueMap = jpaResultParser
            .parse2EdmPropertyValueMap(jpaEntity, edmEntityType);
      }

      expandList = resultsView.getExpand();
      if (expandList != null && expandList.size() != 0)
      {
        HashMap<String, Object> navigationMap = jpaResultParser.parse2EdmNavigationValueMap(
            jpaEntity, constructListofNavProperty(expandList));
        edmPropertyValueMap.putAll(navigationMap);
      }
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

  /* Response for $count */
  public static ODataResponse build(final long jpaEntityCount,
      final ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException {

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

  /* Response for Create Entity */
  @SuppressWarnings("unchecked")
  public static ODataResponse build(final List<Object> createdObjectList,
      final PostUriInfo uriInfo, final String contentType,
      final ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException,
      ODataNotFoundException {

    if (createdObjectList == null || createdObjectList.size() == 0 || createdObjectList.get(0) == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    EdmEntityType edmEntityType = null;
    ODataResponse odataResponse = null;

    try {

      edmEntityType = uriInfo.getTargetEntitySet().getEntityType();
      Map<String, Object> edmPropertyValueMap = null;

      JPAEntityParser jpaResultParser = JPAEntityParser.create();
      edmPropertyValueMap = jpaResultParser.parse2EdmPropertyValueMap(
          createdObjectList.get(0), edmEntityType);

      List<ArrayList<NavigationPropertySegment>> expandList = null;
      if (createdObjectList.get(1) != null && ((Map<EdmNavigationProperty, EdmEntitySet>) createdObjectList.get(1)).size() > 0) {
        expandList = getExpandList((Map<EdmNavigationProperty, EdmEntitySet>) createdObjectList.get(1));
        HashMap<String, Object> navigationMap = jpaResultParser.parse2EdmNavigationValueMap(
            createdObjectList.get(0), constructListofNavProperty(expandList));
        edmPropertyValueMap.putAll(navigationMap);
      }
      EntityProviderWriteProperties feedProperties = null;
      try {
        feedProperties = getEntityProviderPropertiesforPost(oDataJPAContext, uriInfo, expandList);
      } catch (ODataException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.INNER_EXCEPTION, e);
      }

      odataResponse = EntityProvider.writeEntry(contentType,
          uriInfo.getTargetEntitySet(), edmPropertyValueMap,
          feedProperties);

      odataResponse = ODataResponse.fromResponse(odataResponse)
          .status(HttpStatusCodes.CREATED).build();

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

  /* Response for Update Entity */
  public static ODataResponse build(final Object updatedObject,
      final PutMergePatchUriInfo putUriInfo) throws ODataJPARuntimeException,
      ODataNotFoundException {
    if (updatedObject == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }
    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  /* Response for Delete Entity */
  public static ODataResponse build(final Object deletedObject,
      final DeleteUriInfo deleteUriInfo) throws ODataJPARuntimeException,
      ODataNotFoundException {

    if (deletedObject == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }
    return ODataResponse.status(HttpStatusCodes.OK).build();
  }

  /* Response for Function Import Single Result */
  public static ODataResponse build(final Object result,
      final GetFunctionImportUriInfo resultsView)
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
      } else {
        throw new ODataNotFoundException(ODataHttpException.COMMON);
      }
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

  /* Response for Function Import Multiple Result */
  public static ODataResponse build(final List<Object> resultList,
      final GetFunctionImportUriInfo resultsView, final String contentType,
      final ODataJPAContext oDataJPAContext) throws ODataJPARuntimeException,
      ODataNotFoundException {

    ODataResponse odataResponse = null;

    if (resultList != null && !resultList.isEmpty()) {
      JPAEntityParser jpaResultParser = JPAEntityParser.create();
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

    } else {
      throw new ODataNotFoundException(ODataHttpException.COMMON);
    }

    return odataResponse;
  }

  /* Response for Read Entity Link */
  public static ODataResponse build(final Object jpaEntity,
      final GetEntityLinkUriInfo resultsView, final String contentType, final ODataJPAContext oDataJPAContext)
      throws ODataNotFoundException, ODataJPARuntimeException {

    if (jpaEntity == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }
    EdmEntityType edmEntityType = null;
    ODataResponse odataResponse = null;

    try {

      EdmEntitySet entitySet = resultsView.getTargetEntitySet();
      edmEntityType = entitySet.getEntityType();
      Map<String, Object> edmPropertyValueMap = null;

      JPAEntityParser jpaResultParser = JPAEntityParser.create();
      edmPropertyValueMap = jpaResultParser
          .parse2EdmPropertyValueMap(
              jpaEntity,
              edmEntityType.getKeyProperties());

      EntityProviderWriteProperties entryProperties = EntityProviderWriteProperties
          .serviceRoot(oDataJPAContext.getODataContext().getPathInfo().getServiceRoot())
          .build();

      ODataResponse response = EntityProvider.writeLink(contentType, entitySet, edmPropertyValueMap, entryProperties);

      odataResponse = ODataResponse.fromResponse(response).build();

    } catch (ODataException e) {
      throw ODataJPARuntimeException.throwException(
          ODataJPARuntimeException.INNER_EXCEPTION, e);

    }

    return odataResponse;
  }

  /* Response for Read Entity Links */
  public static <T> ODataResponse build(final List<T> jpaEntities,
      final GetEntitySetLinksUriInfo resultsView, final String contentType,
      final ODataJPAContext oDataJPAContext)
      throws ODataJPARuntimeException {
    EdmEntityType edmEntityType = null;
    ODataResponse odataResponse = null;

    try {

      EdmEntitySet entitySet = resultsView.getTargetEntitySet();
      edmEntityType = entitySet.getEntityType();
      List<EdmProperty> keyProperties = edmEntityType.getKeyProperties();

      List<Map<String, Object>> edmEntityList = new ArrayList<Map<String, Object>>();
      Map<String, Object> edmPropertyValueMap = null;
      JPAEntityParser jpaResultParser = JPAEntityParser.create();

      for (Object jpaEntity : jpaEntities) {
        edmPropertyValueMap = jpaResultParser
            .parse2EdmPropertyValueMap(
                jpaEntity,
                keyProperties);
        edmEntityList.add(edmPropertyValueMap);
      }

      Integer count = null;
      if(resultsView.getInlineCount() != null){
    	  if((resultsView.getSkip() != null || resultsView.getTop() != null)){
    		  // when $skip and/or $top is present with $inlinecount
    		  count = getInlineCountForNonFilterQueryLinks(edmEntityList, resultsView);
    	  }else{
    		  // In all other cases
    		  count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityList
    		          .size() : null;
    	  }
      }

      ODataContext context = oDataJPAContext.getODataContext();
      EntityProviderWriteProperties entryProperties = EntityProviderWriteProperties
          .serviceRoot(context.getPathInfo().getServiceRoot())
          .inlineCountType(resultsView.getInlineCount())
          .inlineCount(count)
          .build();

      odataResponse = EntityProvider.writeLinks(contentType, entitySet, edmEntityList, entryProperties);

      odataResponse = ODataResponse.fromResponse(odataResponse).build();

    } catch (ODataException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }

    return odataResponse;

  }
  
  
  /*
   * This method handles $inlinecount request. It also modifies the list of results in case of 
   * $inlinecount and $top/$skip combinations. Specific to LinksUriInfo. //TODO
   * 
   * @param edmEntityList
   * @param resultsView 
   * 
   * @return
   */
  private static Integer getInlineCountForNonFilterQueryLinks(List<Map<String, Object>> edmEntityList, GetEntitySetLinksUriInfo resultsView) {
	  // when $skip and/or $top is present with $inlinecount, first get the total count
	  Integer count = null;
	  if(resultsView.getInlineCount() == InlineCount.ALLPAGES){
		  if(resultsView.getSkip() != null || resultsView.getTop() != null){
			  count = edmEntityList.size();
			  // Now update the list
			  if(resultsView.getSkip() != null){
				  // Index checks to avoid IndexOutOfBoundsException
				  if(resultsView.getSkip() > edmEntityList.size()){
					  edmEntityList.clear();
					  return count;
				  }
				  edmEntityList.subList(0, resultsView.getSkip()).clear();
			  }
			  if(resultsView.getTop() != null && resultsView.getTop() >= 0){
				  edmEntityList.subList(0, resultsView.getTop());
			  }
		  }
	  }// Inlinecount of None is handled by default - null
	return count;
  }
  

  /*
   * Method to build the entity provider Property.Callbacks for $expand would
   * be registered here
   */
  private static EntityProviderWriteProperties getEntityProviderProperties(
      final ODataJPAContext odataJPAContext, final GetEntitySetUriInfo resultsView,
      final List<Map<String, Object>> edmEntityList)
      throws ODataJPARuntimeException {
    ODataEntityProviderPropertiesBuilder entityFeedPropertiesBuilder = null;
    /*Integer count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityList
        .size() : null;*/
    
    Integer count = null;
    if(resultsView.getInlineCount() != null){
  	  if((resultsView.getSkip() != null || resultsView.getTop() != null)){
  		  // when $skip and/or $top is present with $inlinecount
  		  count = getInlineCountForNonFilterQueryEntitySet(edmEntityList, resultsView);
  	  }else{
  		  // In all other cases
  		  count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityList
  		          .size() : null;
  	  }
    }
    
    
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
  
  /*
   * This method handles $inlinecount request. It also modifies the list of results in case of 
   * $inlinecount and $top/$skip combinations. Specific to Entity Set. //TODO
   * 
   */
  private static Integer getInlineCountForNonFilterQueryEntitySet(List<Map<String, Object>> edmEntityList, GetEntitySetUriInfo resultsView) {
	  // when $skip and/or $top is present with $inlinecount, first get the total count
	  Integer count = null;
	  if(resultsView.getInlineCount() == InlineCount.ALLPAGES){
		  if(resultsView.getSkip() != null || resultsView.getTop() != null){
			  count = edmEntityList.size();
			  // Now update the list
			  if(resultsView.getSkip() != null){
				  // Index checks to avoid IndexOutOfBoundsException
				  if(resultsView.getSkip() > edmEntityList.size()){
					  edmEntityList.clear();
					  return count;
				  }
				  edmEntityList.subList(0, resultsView.getSkip()).clear();
			  }
			  if(resultsView.getTop() != null && resultsView.getTop() >= 0){
				  edmEntityList.retainAll(edmEntityList.subList(0, resultsView.getTop()));
			  }
		  }
	  }// Inlinecount of None is handled by default - null
	return count;
  }

  private static EntityProviderWriteProperties getEntityProviderProperties(
      final ODataJPAContext odataJPAContext, final GetEntityUriInfo resultsView)
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

  private static EntityProviderWriteProperties getEntityProviderPropertiesforPost(
      final ODataJPAContext odataJPAContext, final PostUriInfo resultsView, final List<ArrayList<NavigationPropertySegment>> expandList)
      throws ODataJPARuntimeException {
    ODataEntityProviderPropertiesBuilder entityFeedPropertiesBuilder = null;
    ExpandSelectTreeNode expandSelectTree = null;
    try {
      entityFeedPropertiesBuilder = EntityProviderWriteProperties
          .serviceRoot(odataJPAContext.getODataContext()
              .getPathInfo().getServiceRoot());
      expandSelectTree = UriParser.createExpandSelectTree(
          null, expandList);
      entityFeedPropertiesBuilder.expandSelectTree(expandSelectTree);
      entityFeedPropertiesBuilder.callbacks(JPAExpandCallBack
          .getCallbacks(odataJPAContext.getODataContext()
              .getPathInfo().getServiceRoot(), expandSelectTree,
              expandList));
    } catch (ODataException e) {
      throw ODataJPARuntimeException.throwException(
          ODataJPARuntimeException.INNER_EXCEPTION, e);
    }

    return entityFeedPropertiesBuilder.build();
  }

  private static List<ArrayList<NavigationPropertySegment>> getExpandList(final Map<EdmNavigationProperty, EdmEntitySet> navPropEntitySetMap) {
    List<ArrayList<NavigationPropertySegment>> expandList = new ArrayList<ArrayList<NavigationPropertySegment>>();
    ArrayList<NavigationPropertySegment> navigationPropertySegmentList = new ArrayList<NavigationPropertySegment>();
    for (Map.Entry<EdmNavigationProperty, EdmEntitySet> entry : navPropEntitySetMap.entrySet()) {
      final EdmNavigationProperty edmNavigationProperty = entry.getKey();
      final EdmEntitySet edmEntitySet = entry.getValue();
      NavigationPropertySegment navigationPropertySegment = new NavigationPropertySegment() {

        @Override
        public EdmEntitySet getTargetEntitySet() {
          return edmEntitySet;
        }

        @Override
        public EdmNavigationProperty getNavigationProperty() {
          return edmNavigationProperty;
        }
      };
      navigationPropertySegmentList.add(navigationPropertySegment);
    }
    expandList.add(navigationPropertySegmentList);
    return expandList;
  }

  private static List<EdmProperty> buildSelectItemList(
      final List<SelectItem> selectItems, final EdmEntityType entity) throws ODataJPARuntimeException {
    boolean flag = false;
    List<EdmProperty> selectPropertyList = new ArrayList<EdmProperty>();
    try {
      for (SelectItem selectItem : selectItems) {
        selectPropertyList.add(selectItem.getProperty());
      }
      for (EdmProperty keyProperty : entity.getKeyProperties()) {
        flag = true;
        for (SelectItem selectedItem : selectItems) {
          if (selectedItem.getProperty().equals(keyProperty)) {
            flag = false;
            break;
          }
        }
        if (flag == true) {
          selectPropertyList.add(keyProperty);
        }
      }

    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }
    return selectPropertyList;
  }

  private static List<EdmNavigationProperty> constructListofNavProperty(
      final List<ArrayList<NavigationPropertySegment>> expandList) {
    List<EdmNavigationProperty> navigationPropertyList = new ArrayList<EdmNavigationProperty>();
    for (ArrayList<NavigationPropertySegment> navpropSegment : expandList) {
      navigationPropertyList.add(navpropSegment.get(0)
          .getNavigationProperty());
    }
    return navigationPropertyList;
  }

}
