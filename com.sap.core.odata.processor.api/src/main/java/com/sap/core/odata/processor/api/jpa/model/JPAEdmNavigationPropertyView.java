package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.NavigationProperty;

/**
 * This class provides view to the implementation class which has functionality to add EDM Navigation Property View and to get EDM Navigation Properties.
 * @author AG
 *
 */
public interface JPAEdmNavigationPropertyView extends JPAEdmBaseView {

	void addJPAEdmNavigationPropertyView(JPAEdmNavigationPropertyView view);

	List<NavigationProperty> getConsistentEdmNavigationProperties();

	NavigationProperty getEdmNavigationProperty();
	

}
