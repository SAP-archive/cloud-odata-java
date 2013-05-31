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

import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmNavigationProperty extends JPAEdmBaseViewImpl implements
		JPAEdmNavigationPropertyView {

	private JPAEdmAssociationView associationView = null;
	private NavigationProperty currentNavigationProperty = null;
	private JPAEdmPropertyView propertyView = null;
	private List<NavigationProperty> consistentNavigationProperties = null;
	private int count;

	public JPAEdmNavigationProperty(JPAEdmAssociationView associationView,
			JPAEdmPropertyView propertyView,int countNumber) {
		super(associationView);
		this.associationView = associationView;
		this.propertyView = propertyView;
		count = countNumber;
		if(consistentNavigationProperties == null)
		{
			consistentNavigationProperties = new ArrayList<NavigationProperty>();
		}
	}

	public JPAEdmNavigationProperty(JPAEdmSchemaView schemaView) {
		super(schemaView);
		consistentNavigationProperties = new ArrayList<NavigationProperty>();

	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (this.builder == null)
			this.builder = new JPAEdmNavigationPropertyBuilder();
		
		return builder;
	}

	private class JPAEdmNavigationPropertyBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {

			currentNavigationProperty = new NavigationProperty();
			JPAEdmNameBuilder.build(associationView, propertyView,
					JPAEdmNavigationProperty.this,count);
			consistentNavigationProperties.add(currentNavigationProperty);
		}

	}

	@Override
	public NavigationProperty getEdmNavigationProperty() {
		return currentNavigationProperty;
	}

	@Override
	public List<NavigationProperty> getConsistentEdmNavigationProperties() {
		return consistentNavigationProperties;
	}

	@Override
	public void addJPAEdmNavigationPropertyView(
			JPAEdmNavigationPropertyView view) {
		if (view != null && view.isConsistent()) {
			currentNavigationProperty = view.getEdmNavigationProperty();
			consistentNavigationProperties.add(currentNavigationProperty);

		}
	}

}
