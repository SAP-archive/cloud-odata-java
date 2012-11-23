package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmDecimal implements EdmSimpleType {

  private EdmSimpleTypeKind edmSimpleType = EdmSimpleTypeKind.Decimal;
  private static final EdmDecimal instance = new EdmDecimal();

  private EdmDecimal() {

  }

  public static EdmDecimal getInstance() {
    return instance;
  }
  
  @Override
  public boolean equals(Object obj) {
    return this == obj || obj instanceof EdmDecimal;
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
        || simpleType instanceof EdmInt16
        || simpleType instanceof EdmInt32
        || simpleType instanceof EdmInt64
        || simpleType instanceof EdmSingle
        || simpleType instanceof EdmDouble
        || simpleType instanceof EdmDecimal;
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
    return literal + "m";
  }

}
