package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmInt16 implements EdmSimpleType {

  private EdmSimpleTypeKind edmSimpleType = EdmSimpleTypeKind.Int16;
  private static final EdmInt16 instance = new EdmInt16();

  private EdmInt16() {

  }

  public static EdmInt16 getInstance() {
    return instance;
  }
  @Override
  public boolean equals(Object obj) {
    return this == obj || obj instanceof EdmInt16;
  }

  @Override
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeKind.edmNamespace;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.SIMPLE;
  }

  @Override
  public String getName() throws EdmException {
    return this.edmSimpleType.toString();
  }

  @Override
  public boolean isCompatible(EdmSimpleType simpleType) {
    return simpleType instanceof Bit
        || simpleType instanceof Uint7
        || simpleType instanceof EdmByte
        || simpleType instanceof EdmSByte
        || simpleType instanceof EdmInt16;
  }

  @Override
  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets) {
    boolean valid = false;
    if (null != this.valueOfString(value, literalKind, facets)) {
      valid = true;
    }
    return valid;
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
   return literal;
  }

}
