package com.sap.core.odata.api.edm;

public interface EdmComplexType extends EdmNamed, EdmStructuralType, EdmType {

  EdmComplexType getBaseType() throws EdmException;
}
