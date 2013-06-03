package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.ComplexProperty;

/**
 * <p>
 * A view on properties of Java Persistence embeddable type and EDM complex
 * type. Properties of JPA embeddable types are converted into EDM properties of
 * EDM complex type.
 * </p>
 * <p>
 * The implementation of the view provides access to properties of EDM complex
 * type created for a given JPA EDM complex type. The implementation acts as a
 * container for the properties of EDM complex type.
 * </p>
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView
 */
public interface JPAEdmComplexPropertyView extends JPAEdmBaseView {
  /**
   * The method returns a complex property for a complex type.
   * 
   * @return an instance of
   *         {@link com.sap.core.odata.api.edm.provider.ComplexProperty}
   */
  ComplexProperty getEdmComplexProperty();
}
