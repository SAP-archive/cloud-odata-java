package com.sap.core.odata.processor.jpa.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmReferentialContraintView;

public class JPAEdmReferentialConstraint extends JPAEdmBaseViewImpl implements
		JPAEdmReferentialContraintView {

	private JPAEdmAssociationView associationView;
	private JPAEdmProperty propertyView;

	public JPAEdmReferentialConstraint(JPAEdmAssociationView associationView, JPAEdmProperty propertyView) {
		super(associationView);
		this.associationView = associationView;
		this.propertyView = propertyView;
	}

	@Override
	public String getpUnitName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Metamodel getJPAMetaModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConsistent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub

	}

}
