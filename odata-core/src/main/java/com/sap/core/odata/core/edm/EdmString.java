package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type String
 * @author SAP AG
 */
public class EdmString extends AbstractSimpleType {

  private static final EdmString instance = new EdmString();

  public static EdmString getInstance() {
    return instance;
  }

  @Override
  public Class<?> getDefaultType() {
    return String.class;
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

    String result;
    if (literalKind == EdmLiteralKind.URI) {
      if (value.length() >= 2 && value.startsWith("'") && value.endsWith("'")) {
        result = (value.substring(1, value.length() - 1)).replace("''", "'");
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      }
    } else {
      result = value;
    }

    if (facets != null) {
      if (facets.isUnicode() != null && !facets.isUnicode()) {
        if (!result.matches("\\p{ASCII}*")) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));
        }
      }
      if (facets.getMaxLength() != null && facets.getMaxLength() < result.length()) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));
      }
    }

    if (returnType.isAssignableFrom(String.class)) {
      return returnType.cast(result);
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

    String result;
    if (value instanceof String) {
      result = (String) value;
    } else {
      result = String.valueOf(value);
    }

    if (facets != null && facets.isUnicode() != null && !facets.isUnicode()) {
      if (!result.matches("\\p{ASCII}*")) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets));
      }
    }

    if (facets != null && facets.getMaxLength() != null && facets.getMaxLength() < result.length()) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets));
    }

    if (literalKind == EdmLiteralKind.URI) {
      result = toUriLiteral(result);
    }

    return result;
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "'" + literal.replace("'", "''") + "'";
  }
}
