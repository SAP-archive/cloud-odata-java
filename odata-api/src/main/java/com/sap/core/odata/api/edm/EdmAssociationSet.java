package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL AssociationSet element
 * 
 * <p>EdmAssociationSet defines the relationship of two entity sets.
 * @author SAP AG
 */
public interface EdmAssociationSet extends EdmNamed {

  /**
   * Get the association
   * 
   * @return {@link EdmAssociation}
   * @throws EdmException
   */
  EdmAssociation getAssociation() throws EdmException;

  /**
   * Get the association set end
   * 
   * @param role
   * @return {@link EdmAssociationSetEnd}
   * @throws EdmException
   */
  EdmAssociationSetEnd getEnd(String role) throws EdmException;

  /**
   * Get the entity container the association set is located in
   * 
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getEntityContainer() throws EdmException;
}