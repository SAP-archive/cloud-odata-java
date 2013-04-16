package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Boolean
 * @author SAP AG
 */
public class EdmBoolean extends AbstractSimpleType {

  private static final EdmBoolean instance = new EdmBoolean();

  public static EdmBoolean getInstance() {
    return instance;
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit || simpleType instanceof EdmBoolean;
  }

  @Override
  public Class<?> getDefaultType() {
    return Boolean.class;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null) {
      return facets == null || facets.isNullable() == null || facets.isNullable();
    } else {
      return validateLiteral(value);
    }
  }

  private static boolean validateLiteral(final String value) {
    return "true".equals(value) || "1".equals(value)
        || "false".equals(value) || "0".equals(value);
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

    if (validateLiteral(value)) {
      if (returnType.isAssignableFrom(Boolean.class)) {
        return returnType.cast(Boolean.valueOf("true".equals(value) || "1".equals(value)));
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
      }
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
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

    if (value instanceof Boolean) {
      return Boolean.toString((Boolean) value);
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
    }
  }
}
