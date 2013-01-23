package com.sap.core.odata.core.edm;

import java.math.BigInteger;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Int64
 * @author SAP AG
 */
public class EdmInt64 extends AbstractSimpleType {

  private static final EdmInt64 instance = new EdmInt64();

  public static EdmInt64 getInstance() {
    return instance;
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit
        || simpleType instanceof Uint7
        || simpleType instanceof EdmByte
        || simpleType instanceof EdmSByte
        || simpleType instanceof EdmInt16
        || simpleType instanceof EdmInt32
        || simpleType instanceof EdmInt64;
  }

  @Override
  public Class<?> getDefaultType() {
    return Long.class;
  }

  @Override
  public <T> T valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    Long valueLong;
    try {
      if (literalKind == EdmLiteralKind.URI)
        if (value.endsWith("L") || value.endsWith("l"))
          valueLong = Long.parseLong(value.substring(0, value.length() - 1));
        else
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      else
        valueLong = Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
    }

    if (returnType == Long.class)
      return returnType.cast(valueLong);
    else if (returnType == Byte.class)
      if (valueLong >= Byte.MIN_VALUE && valueLong <= Byte.MAX_VALUE)
        return returnType.cast(valueLong.byteValue());
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    else if (returnType == Short.class)
      if (valueLong >= Short.MIN_VALUE && valueLong <= Short.MAX_VALUE)
        return returnType.cast(valueLong.byteValue());
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    else if (returnType == Integer.class)
      if (valueLong >= Integer.MIN_VALUE && valueLong <= Integer.MAX_VALUE)
        return returnType.cast(valueLong.byteValue());
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    else if (returnType == BigInteger.class)
      return returnType.cast(BigInteger.valueOf(valueLong));
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultLiteral(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    String result;
    if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)
      result = value.toString();
    else if (value instanceof BigInteger)
      if (((BigInteger) value).bitLength() < Long.SIZE) // "<" because of the sign bit
        result = value.toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));

    if (literalKind == EdmLiteralKind.URI)
      result = toUriLiteral(result);

    return result;
  }

  @Override
  public String toUriLiteral(final String literal) {
    return literal + "L";
  }
}
