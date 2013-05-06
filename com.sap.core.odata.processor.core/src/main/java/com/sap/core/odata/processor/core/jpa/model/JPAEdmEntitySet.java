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
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmEntitySet extends JPAEdmBaseViewImpl implements
		JPAEdmEntitySetView {

	private EntitySet currentEntitySet = null;
	private List<EntitySet> consistentEntitySetList = null;
	private JPAEdmEntityTypeView entityTypeView = null;
	private JPAEdmSchemaView schemaView;

	public JPAEdmEntitySet(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (this.builder == null)
			this.builder = new JPAEdmEntitySetBuilder();
		
		return builder;
	}

	@Override
	public EntitySet getEdmEntitySet() {
		return currentEntitySet;
	}

	@Override
	public List<EntitySet> getConsistentEdmEntitySetList() {
		return consistentEntitySetList;
	}

	@Override
	public JPAEdmEntityTypeView getJPAEdmEntityTypeView() {
		return entityTypeView;
	}

	@Override
	public void clean() {
		this.currentEntitySet = null;
		this.consistentEntitySetList = null;
		this.entityTypeView = null;
		this.isConsistent = false;
	}

	private class JPAEdmEntitySetBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException, ODataJPARuntimeException {

			if (consistentEntitySetList == null)
				consistentEntitySetList = new ArrayList<EntitySet>();

			entityTypeView = new JPAEdmEntityType(schemaView);
			entityTypeView.getBuilder().build();

			if (entityTypeView.isConsistent() && entityTypeView.getConsistentEdmEntityTypes() != null ) {

				String nameSpace = schemaView.getEdmSchema().getNamespace();
				for (EntityType entityType : entityTypeView
						.getConsistentEdmEntityTypes()) {

					currentEntitySet = new EntitySet();
					currentEntitySet.setEntityType(new FullQualifiedName(
							nameSpace, entityType.getName()));
					JPAEdmNameBuilder.build(JPAEdmEntitySet.this,entityTypeView);
					consistentEntitySetList.add(currentEntitySet);

				}
				isConsistent = true;
			} else {
				isConsistent = false;
				return;
			}

		}

	}

}
