package com.sap.core.odata.core.edm.simpletype;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmInt32 implements EdmSimpleType {

  private EdmSimpleTypeKind edmSimpleType = EdmSimpleTypeKind.Int32;
  private static final EdmInt32 instance = new EdmInt32();

  private EdmInt32() {

  }

  public static EdmInt32 getInstance() {
    return instance;
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj || obj instanceof EdmInt32;
  }

  @Override
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeFacade.edmNamespace;
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
        || simpleType instanceof EdmInt32;
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
    return Integer.valueOf(value);
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
