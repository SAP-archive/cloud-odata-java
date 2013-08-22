package com.sap.core.odata.processor.core.jpa.access.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import com.sap.core.odata.api.ep.callback.OnWriteEntryContent;
import com.sap.core.odata.api.ep.callback.OnWriteFeedContent;
import com.sap.core.odata.api.ep.callback.WriteCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class JPAExpandCallBack implements OnWriteFeedContent, OnWriteEntryContent, ODataCallback {

  private URI baseUri;
  private List<ArrayList<NavigationPropertySegment>> expandList;
  private EdmEntitySet nextEntitySet = null;

  private JPAExpandCallBack(final URI baseUri, final List<ArrayList<NavigationPropertySegment>> expandList) {
    super();
    this.baseUri = baseUri;
    this.expandList = expandList;
  }

  @Override
  public WriteEntryCallbackResult retrieveEntryResult(
      final WriteEntryCallbackContext context) {
    WriteEntryCallbackResult result = new WriteEntryCallbackResult();
    Map<String, Object> entry = context.getEntryData();
    Map<String, Object> edmPropertyValueMap = null;
    List<EdmNavigationProperty> currentNavPropertyList = null;
    Map<String, ExpandSelectTreeNode> navigationLinks = null;
    JPAEntityParser jpaResultParser = new JPAEntityParser();
    EdmNavigationProperty currentNavigationProperty = context.getNavigationProperty();
    try {
      Object inlinedEntry = entry.get(currentNavigationProperty.getName());
      if (nextEntitySet == null) {
        nextEntitySet = context.getSourceEntitySet().getRelatedEntitySet(currentNavigationProperty);
      }
      edmPropertyValueMap = jpaResultParser.parse2EdmPropertyValueMap(inlinedEntry, nextEntitySet.getEntityType());
      result.setEntryData(edmPropertyValueMap);
      navigationLinks = context.getCurrentExpandSelectTreeNode().getLinks();
      if (navigationLinks.size() > 0)
      {
        currentNavPropertyList = new ArrayList<EdmNavigationProperty>();
        currentNavPropertyList.add(getNextNavigationProperty(context.getSourceEntitySet().getEntityType(), context.getNavigationProperty()));
        HashMap<String, Object> navigationMap = jpaResultParser.parse2EdmNavigationValueMap(inlinedEntry, currentNavPropertyList);
        edmPropertyValueMap.putAll(navigationMap);
        result.setEntryData(edmPropertyValueMap);
      }
      result.setInlineProperties(getInlineEntityProviderProperties(context));
    } catch (EdmException e) {

    } catch (ODataJPARuntimeException e) {

    }

    return result;
  }

  @Override
  public WriteFeedCallbackResult retrieveFeedResult(
      final WriteFeedCallbackContext context) {
    WriteFeedCallbackResult result = new WriteFeedCallbackResult();
    HashMap<String, Object> inlinedEntry = (HashMap<String, Object>) context.getEntryData();
    List<Map<String, Object>> edmEntityList = new ArrayList<Map<String, Object>>();
    Map<String, Object> edmPropertyValueMap = null;
    JPAEntityParser jpaResultParser = new JPAEntityParser();
    List<EdmNavigationProperty> currentNavPropertyList = null;
    EdmNavigationProperty currentNavigationProperty = context.getNavigationProperty();
    try {
      @SuppressWarnings({ "unchecked" })
      List<Object> listOfItems = (List<Object>) inlinedEntry.get(context.getNavigationProperty().getName());
      if (nextEntitySet == null) {
        nextEntitySet = context.getSourceEntitySet().getRelatedEntitySet(currentNavigationProperty);
      }
      for (Object object : listOfItems)
      {
        edmPropertyValueMap = jpaResultParser.parse2EdmPropertyValueMap(object, nextEntitySet.getEntityType());
        edmEntityList.add(edmPropertyValueMap);
      }
      result.setFeedData(edmEntityList);
      if (context.getCurrentExpandSelectTreeNode().getLinks().size() > 0)
      {
        currentNavPropertyList = new ArrayList<EdmNavigationProperty>();
        currentNavPropertyList.add(getNextNavigationProperty(context.getSourceEntitySet().getEntityType(), context.getNavigationProperty()));
        int count = 0;
        for (Object object : listOfItems)
        {
          HashMap<String, Object> navigationMap = jpaResultParser.parse2EdmNavigationValueMap(object, currentNavPropertyList);
          edmEntityList.get(count).putAll(navigationMap);
          count++;
        }
        result.setFeedData(edmEntityList);
      }
      result.setInlineProperties(getInlineEntityProviderProperties(context));
    } catch (EdmException e) {

    } catch (ODataJPARuntimeException e) {

    }
    return result;
  }

  private EdmNavigationProperty getNextNavigationProperty(
      final EdmEntityType sourceEntityType, final EdmNavigationProperty navigationProperty) throws EdmException {
    int count;
    for (ArrayList<NavigationPropertySegment> navPropSegments : expandList)
    {
      count = 0;
      for (NavigationPropertySegment navPropSegment : navPropSegments)
      {
        EdmNavigationProperty navProperty = navPropSegment.getNavigationProperty();
        if (navProperty.getFromRole().equalsIgnoreCase(sourceEntityType.getName()) && navProperty.getName().equals(navigationProperty.getName())) {
          return navPropSegments.get(count + 1).getNavigationProperty();
        } else {
          count++;
        }

      }
    }
    return null;
  }

  public static <T> Map<String, ODataCallback> getCallbacks(final URI baseUri, final ExpandSelectTreeNode expandSelectTreeNode, final List<ArrayList<NavigationPropertySegment>> expandList) throws EdmException {
    Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();

    for (String navigationPropertyName : expandSelectTreeNode.getLinks().keySet()) {
      callbacks.put(navigationPropertyName, new JPAExpandCallBack(baseUri, expandList));
    }

    return callbacks;

  }

  private EntityProviderWriteProperties getInlineEntityProviderProperties(final WriteCallbackContext context) throws EdmException {
    ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties.serviceRoot(baseUri);
    propertiesBuilder.callbacks(getCallbacks(baseUri, context.getCurrentExpandSelectTreeNode(), expandList));
    propertiesBuilder.expandSelectTree(context.getCurrentExpandSelectTreeNode());
    return propertiesBuilder.build();
  }

}
