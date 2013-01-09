package com.sap.core.odata.api.uri.info;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;

public interface GetEntityLinkCountUriInfo {
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
   * @return list of {@link KeyPredicate} or EmptyList
   */
  public List<KeyPredicate> getKeyPredicates();

  /**
   * @return list of {@link NavigationSegment} or EmptyList
   */
  public List<NavigationSegment> getNavigationSegments();

  /**
   * @return boolean
   */
  public boolean isCount();

  /**
   * @return boolean
   */
  public boolean isLinks();

  /**
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters or EmptyMap
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options or EmptyMap
   */
  public Map<String, String> getCustomQueryOptions();
}
