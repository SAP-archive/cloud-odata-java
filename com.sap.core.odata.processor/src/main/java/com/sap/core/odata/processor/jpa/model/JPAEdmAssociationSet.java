package com.sap.core.odata.processor.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;

public class JPAEdmAssociationSet extends JPAEdmBaseViewImpl implements
		JPAEdmAssociationSetView {

	private JPAEdmSchemaView schemaView;
	private AssociationSet currentAssociationSet;
	private List<AssociationSet> associationSetList;
	private Association currentAssociation;

	public JPAEdmAssociationSet(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmAssociationSetBuilder();
	}

	@Override
	public List<AssociationSet> getConsistentEdmAssociationSetList() {
		return associationSetList;
	}

	@Override
	public AssociationSet getEdmAssociationSet() {
		return currentAssociationSet;
	}

	@Override
	public Association getEdmAssociation() {
		return currentAssociation;
	}

	private class JPAEdmAssociationSetBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {

			if (associationSetList == null) {
				associationSetList = new ArrayList<AssociationSet>();
			}

			JPAEdmAssociationView associationView = schemaView
					.getJPAEdmAssociationView();
			JPAEdmEntitySetView entitySetView = schemaView
					.getJPAEdmEntityContainerView().getJPAEdmEntitySetView();

			List<EntitySet> entitySetList = entitySetView
					.getConsistentEdmEntitySetList();
			if (associationView.isConsistent()) {
				for (Association association : associationView
						.getConsistentEdmAssociationList()) {

					currentAssociation = association;

					FullQualifiedName fQname = new FullQualifiedName(schemaView
							.getEdmSchema().getNamespace(),
							association.getName());
					currentAssociationSet = new AssociationSet();
					currentAssociationSet.setAssociation(fQname);

					int endCount = 0;
					short endFlag = 0;
					for (EntitySet entitySet : entitySetList) {
						fQname = entitySet.getEntityType();
						endFlag = 0;
						if (fQname.equals(association.getEnd1().getType())
								|| ++endFlag > 1
								|| fQname.equals(association.getEnd2()
										.getType())) {

							AssociationSetEnd end = new AssociationSetEnd();
							end.setEntitySet(entitySet.getName());
							if (endFlag == 0) {
								currentAssociationSet.setEnd1(end);
								end.setRole(association.getEnd1().getRole());
								endCount++;
							} else {
								endCount++;
								currentAssociationSet.setEnd2(end);
								end.setRole(association.getEnd2().getRole());
							}

							if (endCount == 2)
								break;
						}
					}
					if (endCount == 2) {
						JPAEdmNameBuilder.build(JPAEdmAssociationSet.this);
						associationSetList.add(currentAssociationSet);
					}

				}

			}
		}
	}
}
