package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityType;

public interface JPAEdmEntityTypeView extends JPAEdmBaseView {
	public EntityType getEdmEntityType();

	public javax.persistence.metamodel.EntityType<?> getJPAEntityType();

	public List<EntityType> getConsistentEdmEntityTypes();

}
