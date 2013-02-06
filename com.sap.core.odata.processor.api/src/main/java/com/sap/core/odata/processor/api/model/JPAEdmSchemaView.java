package com.sap.core.odata.processor.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Schema;

public interface JPAEdmSchemaView extends JPAEdmBaseView {
	public Schema getEdmSchema();

	public JPAEdmEntityContainerView getJPAEdmEntityContainerView();

	public JPAEdmComplexTypeView getJPAEdmComplexTypeView();
	
	public JPAEdmAssociationView getJPAEdmAssociationView( );

	public List<String> getNonKeyComplexTypeList();

	public void addNonKeyComplexName(String complexTypeName);
	
}
