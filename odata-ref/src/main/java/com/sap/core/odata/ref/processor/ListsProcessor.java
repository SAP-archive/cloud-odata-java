package com.sap.core.odata.ref.processor;

import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.enums.InlineCount;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;

/**
 * Implementation of the centralized parts of OData processing,
 * allowing to use the simplified {@link ListsDataSource}
 * for the actual data handling
 * @author SAP AG
 */
public class ListsProcessor extends ODataSingleProcessor {

  private final ListsDataSource dataSource;

  public ListsProcessor(ListsDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Edm getEntityDataModel() throws ODataException {
    return new Edm() {

      @Override
      public EdmEntityContainer getEntityContainer(String name) throws EdmException {
        return getDefaultEntityContainer();
      }

      @Override
      public EdmEntityType getEntityType(String namespace, String name) throws EdmException {
        return null;
      }

      @Override
      public EdmComplexType getComplexType(String namespace, String name) throws EdmException {
        return null;
      }

      @Override
      public EdmAssociation getAssociation(String namespace, String name) throws EdmException {
        return null;
      }

      @Override
      public EdmServiceMetadata getServiceMetadata() {
        return new EdmServiceMetadata() {

          @Override
          public byte[] getMetadata() {
            return null;
          }

          @Override
          public String getDataServiceVersion() {
            return "2.0";
          }
        };
      }

      @Override
      public EdmEntityContainer getDefaultEntityContainer() throws EdmException {
        return new EdmEntityContainer() {

          @Override
          public String getName() throws EdmException {
            return null;
          }

          @Override
          public EdmFunctionImport getFunctionImport(String name) throws EdmException {
            return null;
          }

          @Override
          public EdmEntitySet getEntitySet(final String name) throws EdmException {
            return new EdmEntitySet() {

              @Override
              public String getName() throws EdmException {
                return name;
              }

              @Override
              public EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty) throws EdmException {
                return null;
              }

              @Override
              public EdmEntityType getEntityType() throws EdmException {
                return null;
              }

              @Override
              public EdmEntityContainer getEntityContainer() throws EdmException {
                return null;
              }
            };
          }

          @Override
          public EdmAssociationSet getAssociationSet(EdmEntitySet sourceEntitySet, EdmNavigationProperty navigationProperty) throws EdmException {
            return null;
          }
        };
      }

    };
  }

  @Override
  public ODataResponse readEntitySet(final GetEntitySetView uriParserResultView) throws ODataException {
    List<?> data = (List<?>) retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getNavigationSegments());

    applySystemQueryOptions(
        uriParserResultView.getTargetEntitySet(),
        data,
        uriParserResultView.getInlineCount(),
        uriParserResultView.getFilter(),
        uriParserResultView.getOrderBy(),
        uriParserResultView.getSkipToken(),
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(data.toString()).build();
  }

  @Override
  public ODataResponse countEntitySet(final GetEntitySetCountView uriParserResultView) throws ODataException {
    List<?> data = (List<?>) retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getNavigationSegments());

    applySystemQueryOptions(
        uriParserResultView.getTargetEntitySet(),
        data,
        null,
        uriParserResultView.getFilter(),
        null,
        null,
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(String.valueOf(data.size())).build();
  }

  @Override
  public ODataResponse readEntity(final GetEntityView uriParserResultView) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getNavigationSegments());

    if (appliesFilter(data, uriParserResultView.getFilter()))
      return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(data.toString()).build();
    else
      throw new ODataNotFoundException(ODataNotFoundException.USER);
  }

  @Override
  public ODataResponse existsEntity(final GetEntityCountView uriParserResultView) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getNavigationSegments());

    return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(
        appliesFilter(data, uriParserResultView.getFilter()) ? "1" : "0")
        .build();
  }

  private HashMap<String, Object> mapKey(final List<KeyPredicate> keys) throws EdmException {
    HashMap<String, Object> keyMap = new HashMap<String, Object>();
    for (KeyPredicate key : keys) {
      final EdmProperty property = key.getProperty();
      final EdmSimpleType type = (EdmSimpleType) property.getType();
      keyMap.put(property.getName(), type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets()));
    }
    return keyMap;
  }

  private Object retrieveData(final EdmEntitySet startEntitySet, final List<KeyPredicate> keyPredicates, final List<NavigationSegment> navigationSegments) throws ODataException {
    Object data;
    final HashMap<String, Object> keys = mapKey(keyPredicates);

    if (keys.isEmpty())
      data = dataSource.readData(startEntitySet);
    else
      data = dataSource.readData(startEntitySet, keys);

    for (NavigationSegment navigationSegment : navigationSegments)
      data = dataSource.readRelatedData(
          navigationSegment.getEntitySet(),
          data,
          navigationSegment.getEntitySet().getRelatedEntitySet(navigationSegment.getNavigationProperty()),
          mapKey(navigationSegment.getKeyPredicates()));

    return data;
  }

  private Integer applySystemQueryOptions(final EdmEntitySet targetEntitySet, List<?> data, final InlineCount inlineCount, final String filter, final String orderBy, final String skipToken, final int skip, final Integer top) {
    if (filter != null)
      for (Object element : data)
        if (!appliesFilter(element, filter))
          data.remove(element);

    final Integer count = inlineCount == InlineCount.ALLPAGES ? data.size() : null;

    if (orderBy != null)
      ;
    else if (skipToken != null || skip != 0 || top != null)
    ;

    if (skipToken != null)
    ;

    for (int i = 0; i < skip; i++)
      data.remove(0);

    if (top != null)
      while (data.size() > top)
        data.remove(top.intValue());

    return count;
  }

  private boolean appliesFilter(final Object data, final String filter) {
    return true;
  }
}
