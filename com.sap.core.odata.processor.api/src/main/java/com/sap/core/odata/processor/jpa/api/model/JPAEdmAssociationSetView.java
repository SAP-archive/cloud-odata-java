package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;

public interface JPAEdmAssociationSetView extends JPAEdmBaseView {

	List<AssociationSet> getConsistentEdmAssociationSetList();
	AssociationSet getEdmAssociationSet( );
	Association getEdmAssociation();

}
