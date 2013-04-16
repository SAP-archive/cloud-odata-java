package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL AssociationSetEnd element
 * 
 * <p>EdmAssociationSetEnd defines one side of the relationship of two entity sets.
 * @author SAP AG
 */
public interface EdmAssociationSetEnd {

  /**
   * Get the role name
   * 
   * @return String
   */
  String getRole();

  /**
   * Get the entity set
   * 
   * @return {@link EdmEntitySet}
   * @throws EdmException
   */
  EdmEntitySet getEntitySet() throws EdmException;
}