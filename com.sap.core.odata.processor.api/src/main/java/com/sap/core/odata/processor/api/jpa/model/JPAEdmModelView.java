package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.factory.JPAAccessFactory;

/**
 * A view on JPA meta model and EDM meta model. The view acts as the base for
 * the construction of EDM meta model from a JPA meta model.
 * <p>
 * The implementation of the view acts as the container for JPA meta model and
 * EDM meta model. The instance of JPA EDM meta model can be created using
 * {@link JPAAccessFactory}. The instance thus obtained can be used for
 * constructing other elements of the meta model using {@link JPAEdmBuilder}.
 * 
 * @author SAP AG
 * @DoNotImplement
 * @see JPAAccessFactory
 */
public interface JPAEdmModelView extends JPAEdmBaseView {
	/**
	 * The method returns a consistent JPA EDM schema view created from the JPA
	 * meta model.
	 * 
	 * @return an instance of type {@link JPAEdmSchemaView}
	 */
	public JPAEdmSchemaView getEdmSchemaView();
}
