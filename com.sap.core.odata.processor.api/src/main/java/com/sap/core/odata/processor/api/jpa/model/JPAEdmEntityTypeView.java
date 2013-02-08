package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityType;

/**
 * This class provides view to the implementation class which has functionality to search and get EDM Entity Type.
 * @author AG
 *
 */
public interface JPAEdmEntityTypeView extends JPAEdmBaseView {
	public EntityType getEdmEntityType();

	public javax.persistence.metamodel.EntityType<?> getJPAEntityType();

	public List<EntityType> getConsistentEdmEntityTypes();

	public EntityType searchEdmEntityType(String jpaEntityTypeName);

}
