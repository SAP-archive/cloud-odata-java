package com.sap.core.odata.processor.api.jpa.model.mapping;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * com.sap.core.odata.processor.api.jpa.model.mapping package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class JPAEdmMappingModelFactory {

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package:
   * com.sap.core.odata.processor.api.jpa.model.mapping
   * 
   */
  public JPAEdmMappingModelFactory() {}

  /**
   * Create an instance of {@link JPARelationshipMapType }
   * 
   */
  public JPARelationshipMapType createJPARelationshipMapType() {
    return new JPARelationshipMapType();
  }

  /**
   * Create an instance of {@link JPAAttributeMapType }
   * 
   */
  public JPAAttributeMapType createJPAAttributeMapType() {
    return new JPAAttributeMapType();
  }

  /**
   * Create an instance of {@link JPAEDMMappingModel }
   * 
   */
  public JPAEdmMappingModel createJPAEDMMappingModel() {
    return new JPAEdmMappingModel();
  }

  /**
   * Create an instance of {@link JPAPersistenceUnitMapType }
   * 
   */
  public JPAPersistenceUnitMapType createJPAPersistenceUnitMapType() {
    return new JPAPersistenceUnitMapType();
  }

  /**
   * Create an instance of {@link JPAEmbeddableTypeMapType }
   * 
   */
  public JPAEmbeddableTypeMapType createJPAEmbeddableTypeMapType() {
    return new JPAEmbeddableTypeMapType();
  }
}