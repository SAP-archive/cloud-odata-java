package com.sap.core.odata.processor.jpa.model.mock;

import java.util.Set;

import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

public class JPAMetaModelMock implements Metamodel {

	@Override
	public <X> EmbeddableType<X> embeddable(Class<X> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> EntityType<X> entity(Class<X> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<EmbeddableType<?>> getEmbeddables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<EntityType<?>> getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ManagedType<?>> getManagedTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> ManagedType<X> managedType(Class<X> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
