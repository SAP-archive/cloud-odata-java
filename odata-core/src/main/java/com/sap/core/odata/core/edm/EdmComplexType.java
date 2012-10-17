package com.sap.core.odata.core.edm;

public interface EdmComplexType extends EdmNamed, EdmStructuralType, EdmType {

  EdmComplexType getBaseType() throws EdmException;
}
