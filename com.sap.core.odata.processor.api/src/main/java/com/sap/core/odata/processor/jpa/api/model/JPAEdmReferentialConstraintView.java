package com.sap.core.odata.processor.jpa.api.model;

import com.sap.core.odata.api.edm.provider.ReferentialConstraint;

public interface JPAEdmReferentialConstraintView extends JPAEdmBaseView {
	ReferentialConstraint getEdmReferentialConstraint();
	boolean isExists( );
	String getRelationShipName();
}
