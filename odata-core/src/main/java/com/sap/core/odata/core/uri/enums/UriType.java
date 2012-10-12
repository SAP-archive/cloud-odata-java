package com.sap.core.odata.core.uri.enums;

public enum UriType {
  /**
   * Service document
   */
  URI0,
  /**
   * Entity set
   */
  URI1,
  /**
   * Entity set with key predicate
   */
  URI2,
  /**
   * Complex property of an entity
   */
  URI3,
  /**
   * Simple property of a complex property of an entity
   */
  URI4,
  /**
   * Simple property of an entity
   */
  URI5,
  /**
   * Navigation property of an entity with target multiplicity '1' or '0..1'
   */
  URI6A,
  /**
   * Navigation property of an entity with target multiplicity '*'
   */
  URI6B,
  /**
   * Link to a single entity
   */
  URI7A,
  /**
   * Link to multiple entities
   */
  URI7B,
  /**
   * Metadata document
   */
  URI8,
  /**
   * Batch request
   */
  URI9,
  /**
   * Function import returning a single entity
   */
  URI10,
  /**
   * Function import returning a collection of complex type values
   */
  URI11,
  /**
   * Function import returning a single instance of a complex type
   */
  URI12,
  /**
   * Function import returning a collection of primitive type values
   */
  URI13,
  /**
   * Function import returning a single primitive type value
   */
  URI14,
  /**
   * Count of an entity set
   */
  URI15,
  /**
   * Count of a single entity
   */
  URI16,
  /**
   * Media resource of an entity
   */
  URI17,
  /**
   * Count of link to a single entity
   */
  URI50A,
  /**
   * Count of links to multiple entities
   */
  URI50B;
}
