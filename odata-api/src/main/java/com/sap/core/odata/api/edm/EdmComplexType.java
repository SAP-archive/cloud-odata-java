package com.sap.core.odata.api.edm;

/**
 * A CSDL ComplexType element
 * 
 * EdmComplexType holds a set of related information like {@link EdmSimpleType} properties and EdmComplexType properties.
 * <p>IMPORTANT
 * Do not implement this interface. This interface is intended for usage only.
 * 
 * @author SAP AG
 */
public interface EdmComplexType extends EdmStructuralType {

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmStructuralType#getBaseType()
   */
  EdmComplexType getBaseType() throws EdmException;
}
