package org.odata4j.producer.jpa;

import org.odata4j.edm.EdmSimpleType;

public interface PropertyResult<T> extends JPAResult {
  public String getName();

  public EdmSimpleType<T> getType();

  public Object getValue();
}