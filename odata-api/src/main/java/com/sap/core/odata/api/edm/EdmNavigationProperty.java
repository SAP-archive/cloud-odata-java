/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL NavigationProperty element
 * 
 * <p>EdmNavigationProperty allows navigation from one entity type to another via a relationship.
 * @author SAP AG
 */
public interface EdmNavigationProperty extends EdmTyped, EdmMappable {

  /**
   * Get the relationship of the navigation property
   * 
   * @return {@link EdmAssociation}
   * @throws EdmException
   */
  EdmAssociation getRelationship() throws EdmException;

  /**
   * Get the from role of the navigation property
   * 
   * @return from role as String
   * @throws EdmException
   */
  String getFromRole() throws EdmException;

  /**
   * Get the to role of the navigation property
   * 
   * @return to role as String
   * @throws EdmException
   */
  String getToRole() throws EdmException;
}