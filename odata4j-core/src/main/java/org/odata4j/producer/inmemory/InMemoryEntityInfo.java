package org.odata4j.producer.inmemory;

import java.util.HashMap;

import org.core4j.Func;
import org.core4j.Func1;
import org.odata4j.producer.inmemory.InMemoryProducer.RequestContext;

public class InMemoryEntityInfo<TEntity> {

  String entitySetName;
  String entityTypeName;
  String[] keys;
  Class<TEntity> entityClass;
  Func<Iterable<TEntity>> get;
  Func1<RequestContext, Iterable<TEntity>> getWithContext;
  Func1<Object, HashMap<String, Object>> id;
  PropertyModel properties;
  boolean hasStream;

  public String getEntitySetName() {
    return entitySetName;
  }

  public String getEntityTypeName() {
    return entityTypeName;
  }

  public String[] getKeys() {
    return keys;
  }

  public Class<TEntity> getEntityClass() {
    return entityClass;
  }

  public Func<Iterable<TEntity>> getGet() {
    return get;
  }

  public Func1<RequestContext, Iterable<TEntity>> getGetWithContext() {
    return getWithContext;
  }

  public Func1<Object, HashMap<String, Object>> getId() {
    return id;
  }

  public PropertyModel getPropertyModel() {
    return properties;
  }

  public boolean getHasStream() {
    return hasStream;
  }

  public Class<?> getSuperClass() {
    return entityClass.getSuperclass() != null && !entityClass.getSuperclass().equals(Object.class) ? entityClass.getSuperclass() : null;
  }
}
