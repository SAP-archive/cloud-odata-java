/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type SByte
 * @author SAP AG
 */
public class EdmSByte extends AbstractSimpleType {

  private static final EdmSByte instance = new EdmSByte();

  public static EdmSByte getInstance() {
    return instance;
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit
        || simpleType instanceof Uint7
        || simpleType instanceof EdmSByte;
  }

  @Override
  public Class<?> getDefaultType() {
    return Byte.class;
  }

  @Override
  public <T> T valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);
    }

    Byte valueByte;
    try {
      valueByte = Byte.parseByte(value);
    } catch (NumberFormatException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
    }

    if (returnType.isAssignableFrom(Byte.class)) {
      return returnType.cast(valueByte);
    } else if (returnType.isAssignableFrom(Short.class)) {
      return returnType.cast(valueByte.shortValue());
    } else if (returnType.isAssignableFrom(Integer.class)) {
      return returnType.cast(valueByte.intValue());
    } else if (returnType.isAssignableFrom(Long.class)) {
      return returnType.cast(valueByte.longValue());
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    }
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null) {
      return getNullOrDefaultLiteral(facets);
    }

    if (literalKind == null) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);
    }

    if (value instanceof Byte) {
      return value.toString();
    } else if (value instanceof Short || value instanceof Integer || value instanceof Long) {
      if (((Number) value).longValue() >= Byte.MIN_VALUE && ((Number) value).longValue() <= Byte.MAX_VALUE) {
        return value.toString();
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
      }
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
    }
  }
}
