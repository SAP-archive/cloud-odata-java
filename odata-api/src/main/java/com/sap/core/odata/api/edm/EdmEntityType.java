package com.sap.core.odata.api.edm;

import java.util.List;

/**
 * <p>A CSDL EntityType element.</p>
 * <p>EdmEntityType holds a set of related information like {@link EdmSimpleType}
 * properties and {@link EdmComplexType} properties and in addition to a
 * {@link EdmComplexType complex type} it provides information about key properties,
 * customizable feed mappings and {@link EdmNavigationProperty navigation properties}. 
 * @author SAP AG
 * @com.sap.core.odata.DoNotImplement
 */
public interface EdmEntityType extends EdmStructuralType {

  /**
   * Gets all key property names.
   * @return collection of key property names of type List<String>
   * @throws EdmException
   */
  List<String> getKeyPropertyNames() throws EdmException;

  /**
   * Get all key properties as list of {@link EdmProperty}.
   * @return collection of key properties of type List<EdmProperty>
   * @throws EdmException
   */
  List<EdmProperty> getKeyProperties() throws EdmException;

  /**
   * Indicates if the entity type is treated as Media Link Entry
   * with associated Media Resource.
   * @return <code>true</code> if the entity type is a Media Link Entry  
   * @throws EdmException
   */
  boolean hasStream() throws EdmException;

  @Override
  EdmEntityType getBaseType() throws EdmException;

  /**
   * Gets the Customizable Feed Mappings of the entity type.
   * @return {@link EdmCustomizableFeedMappings}
   * @throws EdmException
   */
  EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException;

  /**
   * Gets all navigation property names.
   * @return collection of navigation properties of type List<String>
   * @throws EdmException
   */
  List<String> getNavigationPropertyNames() throws EdmException;
}