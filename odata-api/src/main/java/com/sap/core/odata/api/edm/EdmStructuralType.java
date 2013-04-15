/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

import java.util.List;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmStructuralType is the base for a complex type or an entity type.
 * <p>Complex types and entity types are described in the Conceptual Schema Definition of the OData protocol.
 * @author SAP AG
 */
public interface EdmStructuralType extends EdmMappable, EdmType {

  /**
   * Get property by name
   * @param name
   * @return simple or complex property as {@link EdmTyped}
   * @throws EdmException
   */
  EdmTyped getProperty(String name) throws EdmException;

  /**
   * Get all property names
   * 
   * @return property names as type List<String>
   * @throws EdmException
   */
  List<String> getPropertyNames() throws EdmException;

  /**
   * Base types are described in the OData protocol specification.
   * 
   * @return {@link EdmStructuralType}
   * @throws EdmException
   */
  EdmStructuralType getBaseType() throws EdmException;
}