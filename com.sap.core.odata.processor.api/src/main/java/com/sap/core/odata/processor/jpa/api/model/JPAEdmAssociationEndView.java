package com.sap.core.odata.processor.jpa.api.model;

import com.sap.core.odata.api.edm.provider.AssociationEnd;

/**
 * A View on Java Persistence API and Entity Data Model Association End
 * The implementation of View provides access to 
 * @author SAP AG
 * @DoNotImplement
 *
 */
public interface JPAEdmAssociationEndView extends JPAEdmBaseView {
	
	AssociationEnd getEdmAssociationEnd2( );

	AssociationEnd getEdmAssociationEnd1();

	boolean compare(AssociationEnd end1, AssociationEnd end2);
	
}
