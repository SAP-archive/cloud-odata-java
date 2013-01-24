package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.NavigationProperty;

public interface JPAEdmNavigationPropertyView extends JPAEdmBaseView {

	void addJPAEdmNavigationPropertyView(JPAEdmNavigationPropertyView view);

	List<NavigationProperty> getConsistentEdmNavigationProperties();

	NavigationProperty getEdmNavigationProperty();
	

}
