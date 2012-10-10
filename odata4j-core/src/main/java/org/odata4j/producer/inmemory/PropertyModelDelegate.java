package org.odata4j.producer.inmemory;

import java.util.Collection;

import org.odata4j.core.Delegate;

public abstract class PropertyModelDelegate implements Delegate<PropertyModel>, PropertyModel {

  @Override
  public Object getPropertyValue(Object target, String propertyName) {
    return getDelegate().getPropertyValue(target, propertyName);
  }

  @Override
  public void setPropertyValue(Object target, String propertyName, Object value) {
    getDelegate().setPropertyValue(target, propertyName, value);
  }

  @Override
  public Iterable<String> getPropertyNames() {
    return getDelegate().getPropertyNames();
  }

  @Override
  public Class<?> getPropertyType(String propertyName) {
    return getDelegate().getPropertyType(propertyName);
  }

  @Override
  public Iterable<?> getCollectionValue(Object target, String collectionName) {
    return getDelegate().getCollectionValue(target, collectionName);
  }

  @Override
  public void setCollectionValue(Object target, String collectionName, Collection<?> value) {
    getDelegate().setCollectionValue(target, collectionName, value);
  }

  @Override
  public Iterable<String> getCollectionNames() {
    return getDelegate().getCollectionNames();
  }

  @Override
  public Class<?> getCollectionElementType(String collectionName) {
    return getDelegate().getCollectionElementType(collectionName);
  }

  @Override
  public Iterable<String> getDeclaredPropertyNames() {
    return getDelegate().getDeclaredPropertyNames();
  }

  @Override
  public Iterable<String> getDeclaredCollectionNames() {
    return getDelegate().getDeclaredCollectionNames();
  }
}
