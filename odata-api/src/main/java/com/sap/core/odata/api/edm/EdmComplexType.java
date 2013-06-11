package com.sap.core.odata.api.edm;

/**
 * <p>A CSDL ComplexType element.</p>
 * <p>EdmComplexType holds a set of related information like {@link EdmSimpleType}
 * properties and EdmComplexType properties.
 * @author SAP AG
 * @com.sap.core.odata.DoNotImplement
 */
public interface EdmComplexType extends EdmStructuralType {

  @Override
  EdmComplexType getBaseType() throws EdmException;
}
