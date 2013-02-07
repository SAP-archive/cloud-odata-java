package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityContainer;

public interface JPAEdmEntityContainerView extends JPAEdmBaseView {

	public EntityContainer getEdmEntityContainer();

	public List<EntityContainer> getConsistentEdmEntityContainerList();

	public JPAEdmEntitySetView getJPAEdmEntitySetView();

	public JPAEdmAssociationSetView getEdmAssociationSetView();
}
