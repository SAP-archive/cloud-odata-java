package org.odata4j.producer.inmemory;

/**
 * information about a POJO-based complex type.
 */
public class InMemoryComplexTypeInfo<TEntity> {
  String typeName;
  Class<TEntity> entityClass;
  PropertyModel propertyModel;

  public String getTypeName() {
    return typeName;
  }

  public Class<TEntity> getEntityClass() {
    return entityClass;
  }

  public PropertyModel getPropertyModel() {
    return propertyModel;
  }

}
