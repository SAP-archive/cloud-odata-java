package com.sap.core.odata.api.edm;

/**
 * A CSDL EntitySet element
 * 
 * EdmEntitySet is the container for entity type instances
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmEntitySet extends EdmNamed {

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