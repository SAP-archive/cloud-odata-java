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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmAssociation extends JPAEdmBaseViewImpl implements
		JPAEdmAssociationView {

	private JPAEdmAssociationEndView associationEndView;

	private Association currentAssociation;
	private List<Association> consistentAssociatonList;
	private HashMap<String, Association> associationMap;
	private List<JPAEdmReferentialConstraintView> inconsistentRefConstraintViewList;

	public JPAEdmAssociation(JPAEdmAssociationEndView associationEndview,
			JPAEdmEntityTypeView entityTypeView, JPAEdmPropertyView propertyView) {
		super(associationEndview);
		this.associationEndView = associationEndview;
		init();
	}

	public JPAEdmAssociation(JPAEdmSchemaView view) {
		super(view);
		init();
	}

	private void init() {
		isConsistent = false;
		consistentAssociatonList = new ArrayList<Association>();
		inconsistentRefConstraintViewList = new LinkedList<JPAEdmReferentialConstraintView>();
		associationMap = new HashMap<String, Association>();
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (builder == null)
			builder = new JPAEdmAssociationBuilder();
		return builder;
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
		if (view != null) {
			for (String key : associationMap.keySet()) {
				Association association = associationMap.get(key);
				if (association != null && view.compare(association.getEnd1(), association.getEnd2())) {
					currentAssociation = association;
					return association;
				}
			}
		}
		return null;
	}

	@Override
	public void addJPAEdmAssociationView(JPAEdmAssociationView associationView) {
		if (associationView != null) {
			currentAssociation = associationView.getEdmAssociation();
			associationMap
					.put(currentAssociation.getName(), currentAssociation);
			addJPAEdmRefConstraintView(associationView
					.getJPAEdmReferentialConstraintView());
		}
	}

	@Override
	public void addJPAEdmRefConstraintView(
			JPAEdmReferentialConstraintView refView) {
		if (refView != null && refView.isExists())
			inconsistentRefConstraintViewList.add(refView);
	}

	@Override
	public JPAEdmReferentialConstraintView getJPAEdmReferentialConstraintView() {
		if (inconsistentRefConstraintViewList.isEmpty())
			return null;
		return inconsistentRefConstraintViewList.get(0);
	}

	private class JPAEdmAssociationBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException, ODataJPARuntimeException {

			if (associationEndView != null
					&& searchAssociation(associationEndView) == null) {
				currentAssociation = new Association();
				currentAssociation.setEnd1(associationEndView
						.getEdmAssociationEnd1());
				currentAssociation.setEnd2(associationEndView
						.getEdmAssociationEnd2());

				JPAEdmNameBuilder.build(JPAEdmAssociation.this);

				associationMap.put(currentAssociation.getName(),
						currentAssociation);

			} else if (!inconsistentRefConstraintViewList.isEmpty()) {
				int inconsistentRefConstraintViewSize = inconsistentRefConstraintViewList
						.size();
				int index = 0;
				for (int i = 0; i < inconsistentRefConstraintViewSize; i++) {
					JPAEdmReferentialConstraintView view = inconsistentRefConstraintViewList
							.get(index);

					if (view.isExists() && !view.isConsistent()) {
						view.getBuilder().build();
					}
					if (view.isConsistent()) {
						currentAssociation = associationMap.get(view
								.getEdmRelationShipName());
						currentAssociation.setReferentialConstraint(view
								.getEdmReferentialConstraint());
						consistentAssociatonList.add(currentAssociation);
						inconsistentRefConstraintViewList.remove(index);
					} else {
						associationMap.remove(view.getEdmRelationShipName());
						index++;
					}
				}
			}

			if (associationMap.size() == consistentAssociatonList.size()) {
				isConsistent = true;
			} else {
				for (String key : associationMap.keySet()) {
					Association association = associationMap.get(key);
					if (!consistentAssociatonList.contains(association)) {
						consistentAssociatonList.add(association);
					}
				}
				isConsistent = true;
			}

		}
	}

}
