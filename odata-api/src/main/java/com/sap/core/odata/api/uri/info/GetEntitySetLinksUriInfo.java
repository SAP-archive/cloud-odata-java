package com.sap.core.odata.api.uri.info;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.expression.FilterExpression;

/**
 * @author SAP AG
 */
public interface GetEntitySetLinksUriInfo {
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
   * @return whether $links has been used in the URI
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
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
