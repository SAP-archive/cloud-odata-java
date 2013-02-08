package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.EntitySet;

/**
 * This class provides view to the implementation class which has functionality to get EDM Entity Set and EDM Entity Types.
 * @author AG
 *
 */
public interface JPAEdmEntitySetView extends JPAEdmBaseView {

	public EntitySet getEdmEntitySet();
	
	public List<EntitySet> getConsistentEdmEntitySetList();

	public JPAEdmEntityTypeView getJPAEdmEntityTypeView();

}
