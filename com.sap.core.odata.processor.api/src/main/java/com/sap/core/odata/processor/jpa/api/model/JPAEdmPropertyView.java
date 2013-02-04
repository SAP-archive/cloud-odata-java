package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import javax.persistence.metamodel.Attribute;

import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;

public interface JPAEdmPropertyView extends JPAEdmBaseView {

	SimpleProperty getEdmSimpleProperty();

	JPAEdmKeyView getJPAEdmKeyView( );

	List<Property> getEdmPropertyList();
	
	Attribute<?, ?> getJPAAttribute( );

	JPAEdmNavigationPropertyView getJPAEdmNavigationPropertyView();

}
