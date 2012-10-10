package org.odata4j.producer.inmemory;

import org.core4j.Enumerable;
import org.core4j.Func1;

public class EntityIdFunctionPropertyModelDelegate<TEntity, TKey> extends PropertyModelDelegate {

  private final PropertyModel propertyModel;
  private final String idPropertyName;
  private final Class<TKey> idPropertyType;
  private final Func1<TEntity, TKey> id;

  public EntityIdFunctionPropertyModelDelegate(PropertyModel propertyModel, String idPropertyName, Class<TKey> idPropertyType, Func1<TEntity, TKey> id) {
    this.propertyModel = propertyModel;
    this.idPropertyName = idPropertyName;
    this.idPropertyType = idPropertyType;
    this.id = id;
  }

  @Override
  public PropertyModel getDelegate() {
    return propertyModel;
  }

  @Override
  public Iterable<String> getPropertyNames() {
    return Enumerable.create(idPropertyName).concat(Enumerable.create(super.getPropertyNames()));
  }

  @Override
  public Class<?> getPropertyType(String propertyName) {
    if (propertyName.equals(idPropertyName))
      return idPropertyType;
    return super.getPropertyType(propertyName);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object getPropertyValue(Object target, String propertyName) {
    if (propertyName.equals(idPropertyName))
      return id.apply((TEntity) target);
    return super.getPropertyValue(target, propertyName);
  }

}