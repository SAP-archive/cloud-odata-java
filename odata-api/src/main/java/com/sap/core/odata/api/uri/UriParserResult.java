package com.sap.core.odata.api.uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.enums.InlineCount;
import com.sap.core.odata.api.enums.UriType;

/**
 * @author SAP AG
 * Parser results interface
 */
public interface UriParserResult {

  /**
   * @return {@link UriType} the uri type
   */
  public UriType getUriType();

  /**
   * @return {@link EdmEntityContainer} the target entity container
   */
  public EdmEntityContainer getEntityContainer();

  /**
   * @return {@link EdmEntitySet}
   */
  public EdmEntitySet getEntitySet();

  /**
   * @return {@link EdmEntitySet} target entity set
   */
  public EdmEntitySet getTargetEntitySet();

  /**
   * @return {@link EdmFunctionImport} the funktion import
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
   * @return boolean
   */
  public boolean isCount();

  /**
   * @return boolean
   */
  public boolean isValue();

  /**
   * @return boolean
   */
  public boolean isLinks();

  /**
   * @return {@link Format} the format
   */
  public Format getFormat();

  /**
   * @return String the customer format
   */
  public String getCustomFormat();

  /**
   * @return String the filter
   */
  public String getFilter();

  /**
   * @return {@link InlineCount} the inline count
   */
  public InlineCount getInlineCount();

  /**
   * @return String order by
   */
  public String getOrderBy();

  /**
   * @return String skip token
   */
  public String getSkipToken();

  /**
   * @return String skip
   */
  public int getSkip();

  /**
   * @return int top
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
   * @return Map of {@literal <String,} {@link UriLiteral}{@literal >} function import parameters
   */
  public Map<String, UriLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
