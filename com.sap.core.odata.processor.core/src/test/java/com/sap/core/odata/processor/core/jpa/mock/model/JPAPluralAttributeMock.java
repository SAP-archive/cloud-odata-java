package com.sap.core.odata.processor.core.jpa.mock.model;

import java.lang.reflect.Member;
import java.util.ArrayList;

import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.Type;

public class JPAPluralAttributeMock implements PluralAttribute<Object, ArrayList<String>, String> {

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public javax.persistence.metamodel.Attribute.PersistentAttributeType getPersistentAttributeType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ManagedType<Object> getDeclaringType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<ArrayList<String>> getJavaType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Member getJavaMember() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isAssociation() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isCollection() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public javax.persistence.metamodel.Bindable.BindableType getBindableType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<String> getBindableJavaType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public javax.persistence.metamodel.PluralAttribute.CollectionType getCollectionType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Type<String> getElementType() {
    // TODO Auto-generated method stub
    return null;
  }

}
