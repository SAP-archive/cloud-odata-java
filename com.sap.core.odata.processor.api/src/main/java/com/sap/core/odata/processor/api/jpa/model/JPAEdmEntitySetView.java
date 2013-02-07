package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntitySet;

public interface JPAEdmEntitySetView extends JPAEdmBaseView {

	public EntitySet getEdmEntitySet();
	
	public List<EntitySet> getConsistentEdmEntitySetList();

	public JPAEdmEntityTypeView getJPAEdmEntityTypeView();

}
