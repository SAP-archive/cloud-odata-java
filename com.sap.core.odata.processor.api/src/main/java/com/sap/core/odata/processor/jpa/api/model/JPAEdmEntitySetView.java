package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntitySet;

public interface JPAEdmEntitySetView extends JPAEdmBaseView {

	public EntitySet getEdmEntitySet();
	
	public List<EntitySet> getConsistentEdmEntitySetList();

	public JPAEdmEntityTypeView getJPAEdmEntityTypeView();

}
