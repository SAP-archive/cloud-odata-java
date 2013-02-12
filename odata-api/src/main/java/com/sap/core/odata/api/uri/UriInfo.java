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
 * Structured parts of the request URI - the result of URI parsing.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 * @see UriParser
 */
public interface UriInfo extends GetServiceDocumentUriInfo, GetEntitySetUriInfo, GetEntityUriInfo, GetComplexPropertyUriInfo, GetSimplePropertyUriInfo,
    GetEntityLinkUriInfo, GetEntitySetLinksUriInfo, GetMetadataUriInfo, GetFunctionImportUriInfo, GetEntitySetCountUriInfo, GetEntityCountUriInfo, GetMediaResourceUriInfo,
    GetEntityLinkCountUriInfo, GetEntitySetLinksCountUriInfo, PutMergePatchUriInfo, PostUriInfo, DeleteUriInfo {

  /**
   * Gets the target entity container.
   * @return {@link EdmEntityContainer} the target entity container
   */
  @Override
  public EdmEntityContainer getEntityContainer();

  /**
   * Gets the start entity set - identical to the target entity set if no navigation
   * has been used.
   * @return {@link EdmEntitySet}
   */
  @Override
  public EdmEntitySet getStartEntitySet();

  /**
   * Gets the target entity set after navigation.
   * @return {@link EdmEntitySet} target entity set
   */
  @Override
  public EdmEntitySet getTargetEntitySet();

  /**
   * Gets the function import.
   * @return {@link EdmFunctionImport} the function import
   */
  @Override
  public EdmFunctionImport getFunctionImport();

  /**
   * Gets the target type of the request: an entity type, a simple type, or a complex type.
   * @return {@link EdmType} the target type
   */
  @Override
  public EdmType getTargetType();

  /**
   * Gets the key predicates used to select a single entity out of the start entity set,
   * or an empty list if not used.
   * @return List of {@link KeyPredicate}
   * @see #getStartEntitySet()
   */
  @Override
  public List<KeyPredicate> getKeyPredicates();

  /**
   * Gets the navigation segments, or an empty list if no navigation has been used.
   * @return List of {@link NavigationSegment}
   */
  @Override
  public List<NavigationSegment> getNavigationSegments();

  /**
   * Gets the path used to select a (simple or complex) property of an entity,
   * or an empty list if no property is accessed.
   * @return List of {@link EdmProperty}
   */
  @Override
  public List<EdmProperty> getPropertyPath();

  /**
   * Determines whether $count has been used in the request URI.
   * @return whether $count has been used
   */
  @Override
  public boolean isCount();

  /**
   * Determines whether $value has been used in the request URI.
   * @return whether $value has been used
   */
  @Override
  public boolean isValue();

  /**
   * Determines whether $links has been used in the request URI.
   * @return whether $links has been used
   */
  @Override
  public boolean isLinks();

  /**
   * Gets the value of the $format system query option.
   * @return the format (as set as <code>$format</code> query parameter) or null
   */
  @Override
  public String getFormat();

  /**
   * Gets the value of the $filter system query option as root object of the
   * expression tree built during URI parsing.
   * @return the filter expression or null
   */
  @Override
  public FilterExpression getFilter();

  /**
   * Gets the value of the $inlinecount system query option.
   * @return {@link InlineCount} the inline count or null
   */
  @Override
  public InlineCount getInlineCount();

  /**
   * Gets the value of the $orderby system query option as root object of the
   * expression tree built during URI parsing.
   * @return the order-by expression or null
   */
  @Override
  public OrderByExpression getOrderBy();

  /**
   * Gets the value of the $skiptoken system query option.
   * @return skip token or null
   */
  @Override
  public String getSkipToken();

  /**
   * Gets the value of the $skip system query option.
   * @return skip or null
   */
  @Override
  public Integer getSkip();

  /**
   * Gets the value of the $top system query option.
   * @return top or null
   */
  @Override
  public Integer getTop();

  /**
   * Gets the value of the $expand system query option as a list of
   * lists of navigation-property segments, or an empty list if not used.
   * @return List of a list of {@link NavigationPropertySegment} to be expanded
   */
  @Override
  public List<ArrayList<NavigationPropertySegment>> getExpand();

  /**
   * Gets the value of the $select system query option as a list of select items,
   * or an empty list if not used.
   * @return List of {@link SelectItem} to be selected
   */
  @Override
  public List<SelectItem> getSelect();

  /**
   * Gets the parameters of a function import as Map from parameter names to
   * their corresponding typed values, or an empty list if no function import
   * is used or no parameters are given in the URI.
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  @Override
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * Gets the custom query options as Map from option names to their
   * corresponding String values, or an empty list if no custom query options
   * are given in the URI.
   * @return Map of {@literal <String, String>} custom query options
   */
  @Override
  public Map<String, String> getCustomQueryOptions();
}
