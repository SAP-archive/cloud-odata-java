package com.sap.core.odata.processor.jpa.api.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;

public interface JPAEdmBaseView {
	public String getpUnitName();
	public Metamodel getJPAMetaModel();
	public JPAEdmBuilder getBuilder();
	
	public boolean isConsistent( );
	public void clean();
}
