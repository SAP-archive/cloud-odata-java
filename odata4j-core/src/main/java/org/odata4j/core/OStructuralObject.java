package org.odata4j.core;

import java.util.List;

/**
 * An OData instance object with properties.
 *
 * @see OComplexObject
 * @see OEntity
 */
public interface OStructuralObject extends OObject {

  /**
   * Gets all properties of this instance.
   *
   * @return the properties
   */
  List<OProperty<?>> getProperties();

  /**
   * Gets a property by name.
   *
   * @param propName  the property name
   * @return the property
   */
  OProperty<?> getProperty(String propName);

  /**
   * Gets a property by name as a strongly-typed OProperty.
   *
   * @param <T>  the java-type of the property
   * @param propName  the property name
   * @param propClass  the java-type of the property
   * @return the property
   */
  <T> OProperty<T> getProperty(String propName, Class<T> propClass);

}
