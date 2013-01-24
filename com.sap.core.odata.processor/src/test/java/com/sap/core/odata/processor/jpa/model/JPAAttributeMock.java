package com.sap.core.odata.processor.jpa.model;

import java.lang.reflect.Member;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

public abstract class JPAAttributeMock<X, Y> implements Attribute<X, Y> {

	@Override
	public ManagedType<X> getDeclaringType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Member getJavaMember() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Y> getJavaType() {
		// TODO Auto-generated method stub
		return null;
	}

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
	public boolean isAssociation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCollection() {
		// TODO Auto-generated method stub
		return false;
	}

}
