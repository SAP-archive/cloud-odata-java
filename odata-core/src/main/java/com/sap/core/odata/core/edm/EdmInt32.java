package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Int32
 * @author SAP AG
 */
public class EdmInt32 extends AbstractSimpleType {

  private static final EdmInt32 instance = new EdmInt32();

  public static EdmInt32 getInstance() {
    return instance;
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit
        || simpleType instanceof Uint7
        || simpleType instanceof EdmByte
        || simpleType instanceof EdmSByte
        || simpleType instanceof EdmInt16
        || simpleType instanceof EdmInt32;
  }

  @Override
  public Number valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<?> returnType) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    Integer valueInteger;
    try {
      valueInteger = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
    }

    if (returnType == null || returnType == int.class || returnType == Integer.class)
      return valueInteger;
    else if (returnType == byte.class || returnType == Byte.class)
      if (valueInteger >= Byte.MIN_VALUE && valueInteger <= Byte.MAX_VALUE)
        return valueInteger.byteValue();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    else if (returnType == short.class || returnType == Short.class)
      if (valueInteger >= Short.MIN_VALUE && valueInteger <= Short.MAX_VALUE)
        return valueInteger.byteValue();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    else if (returnType == long.class || returnType == Long.class)
      return valueInteger.longValue();
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultLiteral(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (value instanceof Byte || value instanceof Short || value instanceof Integer)
      return value.toString();
    else if (value instanceof Long)
      if ((Long) value >= Integer.MIN_VALUE && (Long) value <= Integer.MAX_VALUE)
        return value.toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
  }

}
