package com.sap.core.odata.processor.core.jpa.mock.model;

import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

public class JPAEmbeddableTypeMock<X> implements EmbeddableType<X> {

  @Override
  public Attribute<? super X, ?> getAttribute(final String arg0) {
    return null;
  }

  @Override
  public Set<Attribute<? super X, ?>> getAttributes() {
    return null;
  }

  @Override
  public CollectionAttribute<? super X, ?> getCollection(final String arg0) {
    return null;
  }

  @Override
  public <E> CollectionAttribute<? super X, E> getCollection(final String arg0,
      final Class<E> arg1) {
    return null;
  }

  @Override
  public Attribute<X, ?> getDeclaredAttribute(final String arg0) {
    return null;
  }

  @Override
  public Set<Attribute<X, ?>> getDeclaredAttributes() {
    return null;
  }

  @Override
  public CollectionAttribute<X, ?> getDeclaredCollection(final String arg0) {
    return null;
  }

  @Override
  public <E> CollectionAttribute<X, E> getDeclaredCollection(final String arg0,
      final Class<E> arg1) {
    return null;
  }

  @Override
  public ListAttribute<X, ?> getDeclaredList(final String arg0) {
    return null;
  }

  @Override
  public <E> ListAttribute<X, E> getDeclaredList(final String arg0, final Class<E> arg1) {
    return null;
  }

  @Override
  public MapAttribute<X, ?, ?> getDeclaredMap(final String arg0) {
    return null;
  }

  @Override
  public <K, V> MapAttribute<X, K, V> getDeclaredMap(final String arg0,
      final Class<K> arg1, final Class<V> arg2) {
    return null;
  }

  @Override
  public Set<PluralAttribute<X, ?, ?>> getDeclaredPluralAttributes() {
    return null;
  }

  @Override
  public SetAttribute<X, ?> getDeclaredSet(final String arg0) {
    return null;
  }

  @Override
  public <E> SetAttribute<X, E> getDeclaredSet(final String arg0, final Class<E> arg1) {
    return null;
  }

  @Override
  public SingularAttribute<X, ?> getDeclaredSingularAttribute(final String arg0) {
    return null;
  }

  @Override
  public <Y> SingularAttribute<X, Y> getDeclaredSingularAttribute(
      final String arg0, final Class<Y> arg1) {
    return null;
  }

  @Override
  public Set<SingularAttribute<X, ?>> getDeclaredSingularAttributes() {
    return null;
  }

  @Override
  public ListAttribute<? super X, ?> getList(final String arg0) {
    return null;
  }

  @Override
  public <E> ListAttribute<? super X, E> getList(final String arg0, final Class<E> arg1) {
    return null;
  }

  @Override
  public MapAttribute<? super X, ?, ?> getMap(final String arg0) {
    return null;
  }

  @Override
  public <K, V> MapAttribute<? super X, K, V> getMap(final String arg0,
      final Class<K> arg1, final Class<V> arg2) {
    return null;
  }

  @Override
  public Set<PluralAttribute<? super X, ?, ?>> getPluralAttributes() {
    return null;
  }

  @Override
  public SetAttribute<? super X, ?> getSet(final String arg0) {
    return null;
  }

  @Override
  public <E> SetAttribute<? super X, E> getSet(final String arg0, final Class<E> arg1) {
    return null;
  }

  @Override
  public SingularAttribute<? super X, ?> getSingularAttribute(final String arg0) {
    return null;
  }

  @Override
  public <Y> SingularAttribute<? super X, Y> getSingularAttribute(
      final String arg0, final Class<Y> arg1) {
    return null;
  }

  @Override
  public Set<SingularAttribute<? super X, ?>> getSingularAttributes() {
    return null;
  }

  @Override
  public Class<X> getJavaType() {
    return null;
  }

  @Override
  public javax.persistence.metamodel.Type.PersistenceType getPersistenceType() {
    return null;
  }

}
