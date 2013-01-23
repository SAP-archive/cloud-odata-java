package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Int16
 * @author SAP AG
 */
public class EdmInt16 extends AbstractSimpleType {

  private static final EdmInt16 instance = new EdmInt16();

  public static EdmInt16 getInstance() {
    return instance;
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit
        || simpleType instanceof Uint7
        || simpleType instanceof EdmByte
        || simpleType instanceof EdmSByte
        || simpleType instanceof EdmInt16;
  }

  @Override
  public Class<?> getDefaultType() {
    return Short.class;
  }

  @Override
  public <T> T valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    Short valueShort;
    try {
      valueShort = Short.parseShort(value);
    } catch (NumberFormatException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
    }

    if (returnType == Short.class)
      return returnType.cast(valueShort);
    else if (returnType == Byte.class)
      if (valueShort >= Byte.MIN_VALUE && valueShort <= Byte.MAX_VALUE)
        return returnType.cast(valueShort.byteValue());
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    else if (returnType == Integer.class)
      return returnType.cast(valueShort.intValue());
    else if (returnType == Long.class)
      return returnType.cast(valueShort.longValue());
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultLiteral(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (value instanceof Byte || value instanceof Short)
      return value.toString();
    else if (value instanceof Integer || value instanceof Long)
      if (((Number) value).longValue() >= Short.MIN_VALUE && ((Number) value).longValue() <= Short.MAX_VALUE)
        return value.toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
  }
}
