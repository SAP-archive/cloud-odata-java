package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.ComplexType;

public class EdmComplexTypeImplProv extends EdmStructuralTypeImplProv implements EdmComplexType {

  public EdmComplexTypeImplProv(EdmImplProv edm, ComplexType complexType, String namespace) throws EdmException {
    super(edm, complexType, EdmTypeKind.COMPLEX, namespace);
  }

  @Override
  public EdmComplexType getBaseType() throws EdmException {
    return (EdmComplexType) edmBaseType;
  }
}