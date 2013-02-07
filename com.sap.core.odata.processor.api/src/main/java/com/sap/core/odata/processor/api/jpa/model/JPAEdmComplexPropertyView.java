package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.ComplexProperty;

/**
 * <p>
 * A view on properties of Java Persistence embeddable type and EDM complex type. Properties of JPA embeddable types
 * are converted into EDM properties of EDM complex type.
 * </p>
 * <p>
 * The implementation of the view provides access to properties EDM complex type
 * created from Java Persistence embeddable type. The implementation acts as
 * a container for properties of EDM complex type.
 * </p>
 * 
 * @author SAP AG
 * @see JPAEdmComplexTypeView
 */
public interface JPAEdmComplexPropertyView extends JPAEdmBaseView {
	/**
	 * The method returns a complex property for a complex type. 
	 * 
	 * @return an instance of {@link ComplexProperty}
	 */
	ComplexProperty getEdmComplexProperty();
}
