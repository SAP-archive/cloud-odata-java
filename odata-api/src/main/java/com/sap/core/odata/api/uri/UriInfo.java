package com.sap.core.odata.api.uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetComplexPropertyUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.GetMediaResourceUriInfo;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;

/**
 * Parser results interface
 * @author SAP AG
 */
public interface UriInfo extends GetServiceDocumentUriInfo, GetEntitySetUriInfo, GetEntityUriInfo, GetComplexPropertyUriInfo, GetSimplePropertyUriInfo,
    GetEntityLinkUriInfo, GetEntitySetLinksUriInfo, GetMetadataUriInfo, GetFunctionImportUriInfo, GetEntitySetCountUriInfo, GetEntityCountUriInfo, GetMediaResourceUriInfo,
    GetEntityLinkCountUriInfo, GetEntitySetLinksCountUriInfo, PutMergePatchUriInfo, PostUriInfo, DeleteUriInfo {

  /**
   * @return {@link EdmEntityContainer} the target entity container
   */
  @Override
  public EdmEntityContainer getEntityContainer();

  /**
   * @return {@link EdmEntitySet}
   */
  @Override
  public EdmEntitySet getStartEntitySet();

  /**
   * @return {@link EdmEntitySet} target entity set
   */
  @Override
  public EdmEntitySet getTargetEntitySet();

  /**
   * @return {@link EdmFunctionImport} the function import
   */
  @Override
  public EdmFunctionImport getFunctionImport();

  /**
   * @return {@link EdmType} the target type of the entity set
   */
  @Override
  public EdmType getTargetType();

  /**
   * @return list of {@link KeyPredicate} or EmptyList
   */
  @Override
  public List<KeyPredicate> getKeyPredicates();

  /**
   * @return list of {@link NavigationSegment} or EmptyList
   */
  @Override
  public List<NavigationSegment> getNavigationSegments();

  /**
   * @return list of {@link EdmProperty} or EmptyList
   */
  @Override
  public List<EdmProperty> getPropertyPath();

  /**
   * @return whether $count has been used
   */
  @Override
  public boolean isCount();

  /**
   * @return whether $value has been used
   */
  @Override
  public boolean isValue();

  /**
   * @return whether $links has been used
   */
  @Override
  public boolean isLinks();

  /**
   * @return the format (as set as <code>$format</code> query parameter) or null
   */
  @Override
  public String getFormat();

  /**
   * @return the filter expression or null
   */
  @Override
  public FilterExpression getFilter();

  /**
   * @return {@link InlineCount} the inline count or null
   */
  @Override
  public InlineCount getInlineCount();

  /**
   * @return the order-by expression or null
   */
  @Override
  public OrderByExpression getOrderBy();

  /**
   * @return skip token or null
   */
  @Override
  public String getSkipToken();

  /**
   * @return skip or null
   */
  @Override
  public Integer getSkip();

  /**
   * @return top or null
   */
  @Override
  public Integer getTop();

  /**
   * @return List of a list of {@link NavigationPropertySegment} to be expanded or EmptyList
   */
  @Override
  public List<ArrayList<NavigationPropertySegment>> getExpand();

  /**
   * @return List of {@link SelectItem} to be selected or EmptyList
   */
  @Override
  public List<SelectItem> getSelect();

  /**
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters or EmptyMap
   */
  @Override
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options or EmptyMap
   */
  @Override
  public Map<String, String> getCustomQueryOptions();
}
