package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityContainer;

/**
 * This class provides view to the implementation class which has functionality to get EDM Entity Containers, EDM Entity Set, EDM Association Set.
 * @author AG
 *
 */
public interface JPAEdmEntityContainerView extends JPAEdmBaseView {

	public EntityContainer getEdmEntityContainer();

	public List<EntityContainer> getConsistentEdmEntityContainerList();

	public JPAEdmEntitySetView getJPAEdmEntitySetView();

	public JPAEdmAssociationSetView getEdmAssociationSetView();
}
