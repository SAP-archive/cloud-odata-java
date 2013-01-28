package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;

public interface JPAEdmAssociationView extends JPAEdmBaseView {
	
	public Association getEdmAssociation( );
	public List<Association> getConsistentEdmAssociationList( );
	public void addJPAEdmAssociationView(JPAEdmAssociationView associationView);
	public Association searchAssociation(JPAEdmAssociationEndView view);
	public void addJPAEdmRefConstraintView(JPAEdmReferentialConstraintView refView);
	JPAEdmReferentialConstraintView getJPAEdmReferentialConstraintView();
}
