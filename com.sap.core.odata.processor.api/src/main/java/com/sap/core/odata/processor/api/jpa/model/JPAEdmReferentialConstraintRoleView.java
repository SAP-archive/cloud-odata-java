package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.ReferentialConstraintRole;

public interface JPAEdmReferentialConstraintRoleView extends JPAEdmBaseView {
	
	public enum RoleType{
		PRINCIPAL,DEPENDENT
	}
	
	RoleType getRoleType( );
	
	ReferentialConstraintRole getEdmReferentialConstraintRole();
	
	String getJPAColumnName( );
	
	String getEdmEntityTypeName( );
	
	String getEdmAssociationName( );

	boolean isExists();
	
}
