package com.sap.core.odata.processor.jpa.api.model;

import com.sap.core.odata.api.edm.provider.AssociationEnd;

public interface JPAEdmAssociationEndView extends JPAEdmBaseView {
	
	AssociationEnd getAssociationEnd2( );

	AssociationEnd getAssociationEnd1();
	
}
