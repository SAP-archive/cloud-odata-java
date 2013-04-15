/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL Association element
 * 
 * <p>EdmAssociation defines the relationship of two entity types. 
 * @author SAP AG
 */
public interface EdmAssociation extends EdmType {

  /**
   * Get the {@link EdmAssociationEnd} by role
   * @param role
   * @return {@link EdmAssociationEnd}
   * @throws EdmException
   */
  EdmAssociationEnd getEnd(String role) throws EdmException;
}