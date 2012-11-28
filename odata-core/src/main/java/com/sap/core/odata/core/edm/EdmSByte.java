package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Implementation of the EDM simple type SByte
 * @author SAP AG
 */
public class EdmSByte implements EdmSimpleType {

  private static final EdmSByte instance = new EdmSByte();

  private EdmSByte() {

  }

  public static EdmSByte getInstance() {
    return instance;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof EdmSByte;
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
    return EdmSimpleTypeKind.SByte.toString();
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit || simpleType instanceof Uint7 || simpleType instanceof EdmSByte;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      valueOfString(value, literalKind, facets);
      return true;
    } catch (EdmSimpleTypeException e) {
      return false;
    }
  }

  @Override
  public Byte valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      if (facets == null || facets.isNullable() == null || facets.isNullable())
        return null;
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_NULL_NOT_ALLOWED);

    try {
      return Byte.parseByte(value);
    } catch (NumberFormatException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
    }
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      if (facets == null)
        return null;
      else if (facets.getDefaultValue() == null)
        if (facets.isNullable() == null || facets.isNullable())
          return null;
        else
          throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_NULL_NOT_ALLOWED);
      else
        return facets.getDefaultValue();

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (value instanceof Byte)
      return value.toString();
    else if (value instanceof Short || value instanceof Integer || value instanceof Long)
      if (((Number) value).longValue() >= Byte.MIN_VALUE && ((Number) value).longValue() <= Byte.MAX_VALUE)
        return value.toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
  }

  @Override
  public String toUriLiteral(final String literal) {
    return literal;
  }

}
