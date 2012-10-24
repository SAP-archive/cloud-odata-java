package com.sap.core.odata.api.edm;

/**
 * A CSDL Association element
 * 
 * EdmAssociation defines the relationship of two entity types.
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmAssociation extends EdmNamed, EdmType {

  /**
   * Get the association end by role
   * 
   * @param role
   * @return {@link EdmAssociationEnd}
   * @throws EdmException
   */
  EdmAssociationEnd getEnd(String role) throws EdmException;
}