package com.sap.core.odata.processor.core.jpa.mock.model;

import java.lang.reflect.Member;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

public abstract class JPAAttributeMock<X, Y> implements Attribute<X, Y> {

  @Override
  public ManagedType<X> getDeclaringType() {
    return null;
  }

  @Override
  public Member getJavaMember() {
    return null;
  }

  @Override
  public Class<Y> getJavaType() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public javax.persistence.metamodel.Attribute.PersistentAttributeType getPersistentAttributeType() {
    return null;
  }

  @Override
  public boolean isAssociation() {
    return false;
  }

  @Override
  public boolean isCollection() {
    return false;
  }

}
