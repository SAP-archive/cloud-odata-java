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
	public Attribute<? super X, ?> getAttribute(String arg0) {
		return null;
	}

	@Override
	public Set<Attribute<? super X, ?>> getAttributes() {
		return null;
	}

	@Override
	public CollectionAttribute<? super X, ?> getCollection(String arg0) {
		return null;
	}

	@Override
	public <E> CollectionAttribute<? super X, E> getCollection(String arg0,
			Class<E> arg1) {
		return null;
	}

	@Override
	public Attribute<X, ?> getDeclaredAttribute(String arg0) {
		return null;
	}

	@Override
	public Set<Attribute<X, ?>> getDeclaredAttributes() {
		return null;
	}

	@Override
	public CollectionAttribute<X, ?> getDeclaredCollection(String arg0) {
		return null;
	}

	@Override
	public <E> CollectionAttribute<X, E> getDeclaredCollection(String arg0,
			Class<E> arg1) {
		return null;
	}

	@Override
	public ListAttribute<X, ?> getDeclaredList(String arg0) {
		return null;
	}

	@Override
	public <E> ListAttribute<X, E> getDeclaredList(String arg0, Class<E> arg1) {
		return null;
	}

	@Override
	public MapAttribute<X, ?, ?> getDeclaredMap(String arg0) {
		return null;
	}

	@Override
	public <K, V> MapAttribute<X, K, V> getDeclaredMap(String arg0,
			Class<K> arg1, Class<V> arg2) {
		return null;
	}

	@Override
	public Set<PluralAttribute<X, ?, ?>> getDeclaredPluralAttributes() {
		return null;
	}

	@Override
	public SetAttribute<X, ?> getDeclaredSet(String arg0) {
		return null;
	}

	@Override
	public <E> SetAttribute<X, E> getDeclaredSet(String arg0, Class<E> arg1) {
		return null;
	}

	@Override
	public SingularAttribute<X, ?> getDeclaredSingularAttribute(String arg0) {
		return null;
	}

	@Override
	public <Y> SingularAttribute<X, Y> getDeclaredSingularAttribute(
			String arg0, Class<Y> arg1) {
		return null;
	}

	@Override
	public Set<SingularAttribute<X, ?>> getDeclaredSingularAttributes() {
		return null;
	}

	@Override
	public ListAttribute<? super X, ?> getList(String arg0) {
		return null;
	}

	@Override
	public <E> ListAttribute<? super X, E> getList(String arg0, Class<E> arg1) {
		return null;
	}

	@Override
	public MapAttribute<? super X, ?, ?> getMap(String arg0) {
		return null;
	}

	@Override
	public <K, V> MapAttribute<? super X, K, V> getMap(String arg0,
			Class<K> arg1, Class<V> arg2) {
		return null;
	}

	@Override
	public Set<PluralAttribute<? super X, ?, ?>> getPluralAttributes() {
		return null;
	}

	@Override
	public SetAttribute<? super X, ?> getSet(String arg0) {
		return null;
	}

	@Override
	public <E> SetAttribute<? super X, E> getSet(String arg0, Class<E> arg1) {
		return null;
	}

	@Override
	public SingularAttribute<? super X, ?> getSingularAttribute(String arg0) {
		return null;
	}

	@Override
	public <Y> SingularAttribute<? super X, Y> getSingularAttribute(
			String arg0, Class<Y> arg1) {
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
