package com.sap.core.odata.processor.jpa.api.model;

import com.sap.core.odata.api.edm.provider.Schema;

public interface JPAEdmSchemaView extends JPAEdmBaseView {
	public Schema getEdmSchema();

	public JPAEdmEntityContainerView getJPAEdmEntityContainerView();

	public JPAEdmComplexTypeView getJPAEdmComplexTypeView();
	
	public JPAEdmAssociationView getJPAEdmAssociationView( );
	
}
