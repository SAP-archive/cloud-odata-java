package com.sap.core.odata.api.uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.enums.InlineCount;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.resultviews.DeleteResultView;
import com.sap.core.odata.api.uri.resultviews.GetComplexPropertyView;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;
import com.sap.core.odata.api.uri.resultviews.GetFunctionImportView;
import com.sap.core.odata.api.uri.resultviews.GetMediaResourceView;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;
import com.sap.core.odata.api.uri.resultviews.PostResultView;
import com.sap.core.odata.api.uri.resultviews.PutMergePatchResultView;

/**
 * Parser results interface
 * @author SAP AG
 */
public interface UriParserResult extends GetServiceDocumentView, GetEntitySetView, GetEntityView, GetComplexPropertyView, GetSimplePropertyView,
    GetEntityLinkView, GetEntitySetLinksView, GetMetadataView, GetFunctionImportView, GetEntitySetCountView, GetEntityCountView, GetMediaResourceView, 
    GetEntityLinkCountView, GetEntitySetLinksCountView, PutMergePatchResultView, PostResultView, DeleteResultView {

  /**
   * @return {@link EdmEntityContainer} the target entity container
   */
  public EdmEntityContainer getEntityContainer();

  /**
   * @return {@link EdmEntitySet}
   */
  public EdmEntitySet getStartEntitySet();

  /**
   * @return {@link EdmEntitySet} target entity set
   */
  public EdmEntitySet getTargetEntitySet();

  /**
   * @return {@link EdmFunctionImport} the function import
   */
  public EdmFunctionImport getFunctionImport();

  /**
   * @return {@link EdmType} the target type of the entity set
   */
  public EdmType getTargetType();

  /**
   * @return list of {@link KeyPredicate}
   */
  public List<KeyPredicate> getKeyPredicates();

  /**
   * @return list of {@link NavigationSegment}
   */
  public List<NavigationSegment> getNavigationSegments();

  /**
   * @return list of {@link EdmProperty}
   */
  public List<EdmProperty> getPropertyPath();

  /**
   * @return whether $count has been used
   */
  public boolean isCount();

  /**
   * @return whether $value has been used
   */
  public boolean isValue();

  /**
   * @return whether $links has been used
   */
  public boolean isLinks();

  /**
   * @return {@link String} the content type
   */
  public String getContentType();

  /**
   * @return the filter expression
   */
  public FilterExpression getFilter();

  /**
   * @return {@link InlineCount} the inline count
   */
  public InlineCount getInlineCount();

  /**
   * @return the order-by expression
   */
  public OrderByExpression getOrderBy();

  /**
   * @return skip token
   */
  public String getSkipToken();

  /**
   * @return skip
   */
  public Integer getSkip();

  /**
   * @return top
   */
  public Integer getTop();

  /**
   * @return List of a list of {@link NavigationPropertySegment} to be expanded
   */
  public List<ArrayList<NavigationPropertySegment>> getExpand();

  /**
   * @return List of {@link SelectItem} to be selected
   */
  public List<SelectItem> getSelect();

  /**
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
