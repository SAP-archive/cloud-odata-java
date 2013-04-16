package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL EntitySet element
 * <p>EdmEntitySet is the container for entity type instances as described in the OData protocol. 
 * @author SAP AG
 */
public interface EdmEntitySet extends EdmMappable, EdmNamed {

  /**
   * Get the entity type
   * 
   * @return {@link EdmEntityType}
   * @throws EdmException
   */
  EdmEntityType getEntityType() throws EdmException;

  /**
   * Get the related entity set by providing the navigation property
   * 
   * @param navigationProperty of type {@link EdmNavigationProperty}
   * @return {@link EdmEntitySet}
   * @throws EdmException
   */
  EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty) throws EdmException;

  /**
   * Get the entity container the entity set is contained in
   * 
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getEntityContainer() throws EdmException;
}