/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

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
		if (this.builder == null)
			this.builder = new JPAEdmAssociationSetBuilder();
		
		return builder;
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
