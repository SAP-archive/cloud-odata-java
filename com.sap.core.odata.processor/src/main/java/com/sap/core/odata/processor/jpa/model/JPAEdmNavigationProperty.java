package com.sap.core.odata.processor.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmNavigationProperty extends JPAEdmBaseViewImpl implements
		JPAEdmNavigationPropertyView {

	private JPAEdmAssociationView associationView = null;
	private NavigationProperty currentNavigationProperty = null;
	private JPAEdmPropertyView propertyView = null;
	private List<NavigationProperty> consistentNavigationProperties = null;

	public JPAEdmNavigationProperty(JPAEdmAssociationView associationView,
			JPAEdmPropertyView propertyView) {
		super(associationView);
		this.associationView = associationView;
		this.propertyView = propertyView;
	}

	public JPAEdmNavigationProperty(JPAEdmSchemaView schemaView) {
		super(schemaView);
		consistentNavigationProperties = new ArrayList<NavigationProperty>();

	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmNavigationPropertyBuilder();
	}

	private class JPAEdmNavigationPropertyBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {

			currentNavigationProperty = new NavigationProperty();
			JPAEdmNameBuilder.build(associationView, propertyView,
					JPAEdmNavigationProperty.this);
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
