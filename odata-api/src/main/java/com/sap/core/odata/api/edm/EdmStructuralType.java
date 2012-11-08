package com.sap.core.odata.api.edm;

import java.util.Collection;

/**
 * EdmStructuralType is the base for a complex type or an entity type
 * 
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmStructuralType extends EdmType {

  /**
   * Get property by name
   * 
   * @param name
   * @return simple or complex property as {@link EdmTyped}
   * @throws EdmException
   */
  EdmTyped getProperty(String name) throws EdmException;

  /**
   * Get all property names
   * 
   * @return property names as type Collection<String>
   * @throws EdmException
   */
  Collection<String> getPropertyNames() throws EdmException;

  /**
   * Get the base type
   * 
   * @return {@link EdmStructuralType}
   * @throws EdmException
   */
  EdmStructuralType getBaseType() throws EdmException;
}