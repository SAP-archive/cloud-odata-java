package com.sap.core.odata.processor.api.jpa.model;

/**
 * This class provides view to the implementation class which gets the EDM Schema View.
 * @author AG
 *
 */
public interface JPAEdmModelView extends JPAEdmBaseView {
	public JPAEdmSchemaView getEdmSchemaView();
}
