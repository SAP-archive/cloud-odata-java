package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A CSDL ComplexType element
 * 
 * <p>EdmComplexType holds a set of related information like {@link EdmSimpleType} properties and EdmComplexType properties.
 * @author SAP AG
 */
public interface EdmComplexType extends EdmStructuralType {

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmStructuralType#getBaseType()
   */
  @Override
  EdmComplexType getBaseType() throws EdmException;
}
