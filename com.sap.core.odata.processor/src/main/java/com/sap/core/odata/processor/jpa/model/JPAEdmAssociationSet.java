package com.sap.core.odata.processor.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmAssociationSet extends JPAEdmBaseViewImpl implements JPAEdmAssociationSetView {
	
	private JPAEdmEntityContainerView entityContainerView;
	private AssociationSet currentAssociationSet;
	private List<AssociationSet> associationSetList;
	
	public JPAEdmAssociationSet(JPAEdmEntityContainerView view) {
		super(view);
		this.entityContainerView = view;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmAssociationSetBuilder();
	}
	
	@Override
	public List<AssociationSet> getConsistentEdmAssociationList() {
		return associationSetList;
	}
	
	private class JPAEdmAssociationSetBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {
			//JPAEdmAssociationView associationView = new JPAEdmAssociation(JPAEdmAssociationSetView.this);
			//currentAssociationSet.setAssociation(association)
			
		}
		
	}

	@Override
	public JPAEdmAssociationView getJPAEdmAssociationView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssociationSet> getConsistentEdmAssociationSetList() {
		// TODO Auto-generated method stub
		return null;
	}




}
