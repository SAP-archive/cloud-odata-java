package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL AssociationEnd element
 * 
 * <p>EdmAssociationEnd defines one side of the relationship of two entity types.
 * @author SAP AG
 */
public interface EdmAssociationEnd {

  /**
   * @return the role of this {@link EdmAssociationEnd} as a String.
   */
  String getRole();

  /**
   * @return {@link EdmEntityType} this association end points to.
   * @throws EdmException
   */
  EdmEntityType getEntityType() throws EdmException;

  /**
   * See {@link EdmMultiplicity} for more information about possible multiplicities.
   * @return {@link EdmMultiplicity}
   */
  EdmMultiplicity getMultiplicity();
}