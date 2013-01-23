package com.sap.core.odata.processor.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmReferentialContraintView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmAssociation extends JPAEdmBaseViewImpl implements
		JPAEdmAssociationView {

	private JPAEdmAssociationEndView associationEndView;
	private List<Association> consistentAssociatonList;
	private Association currentAssociation;

	public JPAEdmAssociation(JPAEdmAssociationEndView view) {
		super(view);
		this.associationEndView = view;
		consistentAssociatonList = new ArrayList<Association>();
	}

	public JPAEdmAssociation(JPAEdmSchemaView view) {
		super(view);
		consistentAssociatonList = new ArrayList<Association>();
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmAssociationBuilder();
	}

	@Override
	public Association getEdmAssociation() {
		return currentAssociation;
	}

	@Override
	public List<Association> getConsistentEdmAssociationList() {
		return consistentAssociatonList;
	}

	@Override
	public Association searchAssociation(JPAEdmAssociationEndView view) {
		for (Association association : consistentAssociatonList)
			if (view.compare(association.getEnd1(),
					association.getEnd2()))
				return association;

		return null;
	}

	@Override
	public void addJPAEdmAssociationView(JPAEdmAssociationView associationView) {
		consistentAssociatonList.add(associationView.getEdmAssociation());

	}

	@Override
	public void addJPAEdmRefConstraintView(
			JPAEdmReferentialContraintView refView) {
		// TODO Auto-generated method stub

	}

	private class JPAEdmAssociationBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {
			
			if (searchAssociation(associationEndView) == null) {
				currentAssociation = new Association();
				currentAssociation.setEnd1(associationEndView.getAssociationEnd1());
				currentAssociation.setEnd2(associationEndView.getAssociationEnd2());
				
				JPAEdmNameBuilder.build(JPAEdmAssociation.this);
				consistentAssociatonList.add(currentAssociation);
			}

		}

	}

}
