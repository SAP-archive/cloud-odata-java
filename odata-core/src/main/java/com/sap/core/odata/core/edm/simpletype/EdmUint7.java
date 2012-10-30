package com.sap.core.odata.core.edm.simpletype;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmUint7 implements EdmSimpleType {

  @Override
  public boolean equals(Object obj) {
    return this == obj || obj instanceof EdmUint7;
  }

  @Override
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeFacade.systemNamespace;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.SIMPLE;
  }

  @Override
  public String getName() throws EdmException {
    return "UInt7";
  }

  @Override
  public boolean isCompatible(EdmSimpleType simpleType) {
    return simpleType instanceof EdmBit || simpleType instanceof EdmUint7;
  }

  @Override
  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object valueOfString(String value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String valueToString(Object value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toUriLiteral(String literal) {
    // TODO Auto-generated method stub
    return null;
  }
}
