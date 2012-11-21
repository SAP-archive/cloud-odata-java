package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmByte implements EdmSimpleType {

  private static final EdmByte instance = new EdmByte();

  private EdmByte() {

  }

  public static EdmByte getInstance() {
    return instance;
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj || obj instanceof EdmByte;
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
    return EdmSimpleTypeKind.Byte.toString();
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit
        || simpleType instanceof Uint7
        || simpleType instanceof EdmByte;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      valueOfString(value, literalKind, facets);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }

  @Override
  public Object valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    Short valueShort;
    try {
      valueShort = Short.parseShort(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(e);
    }
    if (valueShort >= 0 && valueShort <= 255)
      return valueShort;
    else
      throw new IllegalArgumentException();
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      return "0";
    else if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)
      if (((Number) value).longValue() >= 0 && ((Number) value).longValue() <= 255)
        return value.toString();
      else
        throw new IllegalArgumentException();
    else
      throw new IllegalArgumentException();
  }

  @Override
  public String toUriLiteral(final String literal) {
    return literal;
  }

}
