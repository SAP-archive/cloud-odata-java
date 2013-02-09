package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityType;

/**
 * A view on Java Persistence entity types and EDM entity types. Java
 * persistence entity types are converted into EDM entity types.
 * <p>
 * The implementation of the view provides access to EDM entity types for the
 * given JPA EDM model. The view acts as a container for consistent list of EDM
 * entity types. An EDM entity type is said to be consistent only if it has at
 * least one consistent EDM property and at least one consistent EDM key.
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see {@link JPAEdmPropertyView}, {@link JPAEdmKeyView},
 *      {@link JPAEdmNavigationPropertyView}
 * 
 */
public interface JPAEdmEntityTypeView extends JPAEdmBaseView {
	/**
	 * The method returns an EDM entity currently being processed.
	 * 
	 * @return an instance of type {@link EntityType}
	 */
	public EntityType getEdmEntityType();

	/**
	 * The method returns java persistence Entity type currently being
	 * processed.
	 * 
	 * @return an instance of type
	 *         {@link javax.persistence.metamodel.EntityType<?>}
	 */
	public javax.persistence.metamodel.EntityType<?> getJPAEntityType();

	/**
	 * The method returns a consistent list of EDM entity types for a given java
	 * persistence meta model.
	 * 
	 * @return a list of {@link EntityType}
	 */
	public List<EntityType> getConsistentEdmEntityTypes();

	/**
	 * The method searches in the consistent list of EDM entity types for the
	 * given EDM entity type's name.
	 * 
	 * @param jpaEntityTypeName
	 *            is the name of EDM entity type
	 * @return a reference to EDM entity type if found else null
	 */
	public EntityType searchEdmEntityType(String jpaEntityTypeName);

}
