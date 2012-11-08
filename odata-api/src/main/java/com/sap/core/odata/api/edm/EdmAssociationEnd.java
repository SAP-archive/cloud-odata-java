package com.sap.core.odata.api.edm;

/**
 * A CSDL AssociationEnd element
 * 
 * EdmAssociationEnd defines one side of the relationship of two entity types.
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmAssociationEnd {

  /**
   * Get the role name
   * 
   * @return String
   */
  String getRole();

  /**
   * Get the entity type
   * 
   * @return {@link EdmEntityType}
   * @throws EdmException
   */
  EdmEntityType getEntityType() throws EdmException;

  /**
   * Get the multiplicity
   * 
   * @return {@link EdmMultiplicity}
   */
  EdmMultiplicity getMultiplicity();
}