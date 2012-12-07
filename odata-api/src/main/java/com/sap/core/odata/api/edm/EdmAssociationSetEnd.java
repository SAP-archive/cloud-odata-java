package com.sap.core.odata.api.edm;

/**
 * A CSDL AssociationSetEnd element
 * 
 * EdmAssociationSetEnd defines one side of the relationship of two entity sets.
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 * 
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