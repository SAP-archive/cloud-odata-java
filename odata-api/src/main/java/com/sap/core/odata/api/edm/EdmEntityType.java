package com.sap.core.odata.api.edm;

import java.util.List;

/**
 * A CSDL EntityType element
 * 
 * EdmEntityType holds a set of related information like {@link EdmSimpleType} properties and {@link EdmComplexType} properties
 * and in addition to a complex type {@link EdmComplexType} it provided information about key properties, customizable feed mappings
 * and navigation properties {@link EdmNavigationProperty} 
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmEntityType extends EdmStructuralType {

  /**
   * Get all key property names
   * 
   * @return collection of key property names of type List<String>
   * @throws EdmException
   */
  List<String> getKeyPropertyNames() throws EdmException;

  /**
   * Get all key properties as list of {@link EdmProperty} 
   * 
   * @return collection of key properties of type List<EdmProperty>
   * @throws EdmException
   */
  List<EdmProperty> getKeyProperties() throws EdmException;

  /**
   * Indicates if the entity type is treated as Media Link Entry with associated Media Resource
   * 
   * @return <code>true</code> if the entity type is a Media Link Entry  
   * @throws EdmException
   */
  boolean hasStream() throws EdmException;

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmStructuralType#getBaseType()
   */
  @Override
  EdmEntityType getBaseType() throws EdmException;

  /**
   * Get the Customizable Feed Mappings of the entity type
   * 
   * @return {@link EdmCustomizableFeedMappings}
   * @throws EdmException
   */
  EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException;

  /**
   * Get all navigation property names
   * 
   * @return collection of navigation properties of type List<String>
   * @throws EdmException
   */
  List<String> getNavigationPropertyNames() throws EdmException;
}