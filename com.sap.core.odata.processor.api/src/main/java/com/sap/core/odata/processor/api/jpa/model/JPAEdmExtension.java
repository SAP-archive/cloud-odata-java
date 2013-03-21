package com.sap.core.odata.processor.api.jpa.model;

/**
 * The interface provides methods to extend JPA EDM containers.
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmExtension {
	/**
	 * The method is used to extend the JPA EDM schema view. Use this method to
	 * register custom operations.
	 * 
	 * @param view
	 *            is the schema view
	 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView#registerOperations(Class,
	 *      String[])
	 */
	public void extend(JPAEdmSchemaView view);
}
