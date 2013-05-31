package com.sap.core.odata.processor.core.jpa.mock.model;

import java.lang.reflect.Member;

import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

public class JPASingularAttributeMock<X, T> implements SingularAttribute<X, T> {

	@Override
	public ManagedType<X> getDeclaringType() {
		return null;
	}

	@Override
	public Member getJavaMember() {
		return null;
	}

	@Override
	public Class<T> getJavaType() {
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

	@Override
	public Class<T> getBindableJavaType() {
		return null;
	}

	@Override
	public javax.persistence.metamodel.Bindable.BindableType getBindableType() {
		return null;
	}

	@Override
	public Type<T> getType() {
		return null;
	}

	@Override
	public boolean isId() {
		return false;
	}

	@Override
	public boolean isOptional() {
		return false;
	}

	@Override
	public boolean isVersion() {
		return false;
	}

}
