package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.provider.ComplexType;

public class EdmComplexTypeImplProv extends EdmStructuralTypeImplProv implements EdmComplexType {

  private ComplexType complexType;
  private EdmImplProv edm;

  public EdmComplexTypeImplProv(ComplexType complexType, EdmImplProv edm) {
    this.complexType = complexType;
    this.edm = edm;
  }
}
