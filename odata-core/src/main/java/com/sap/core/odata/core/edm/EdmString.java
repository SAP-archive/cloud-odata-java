package com.sap.core.odata.core.edm;

import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type String.
 * @author SAP AG
 */
public class EdmString extends AbstractSimpleType {

  private static final Pattern PATTERN_ASCII = Pattern.compile("\\p{ASCII}*");
  private static final EdmString instance = new EdmString();

  public static EdmString getInstance() {
    return instance;
  }

  @Override
  public Class<?> getDefaultType() {
    return String.class;
  }

  @Override
  protected <T> T internalValueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
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

    if (facets != null
        && (facets.isUnicode() != null && !facets.isUnicode() && !PATTERN_ASCII.matcher(result).matches()
        || facets.getMaxLength() != null && facets.getMaxLength() < result.length())) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));
    }

    if (returnType.isAssignableFrom(String.class)) {
      return returnType.cast(result);
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    }
  }

  @Override
  protected <T> String internalValueToString(final T value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    final String result = value instanceof String ? (String) value : String.valueOf(value);

    if (facets != null
        && (facets.isUnicode() != null && !facets.isUnicode() && !PATTERN_ASCII.matcher(result).matches()
        || facets.getMaxLength() != null && facets.getMaxLength() < result.length())) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets));
    }

    return result;
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    final int length = literal.length();

    StringBuilder uriLiteral = new StringBuilder(length + 2);
    uriLiteral.append('\'');
    for (int i = 0; i < length; i++) {
      final char c = literal.charAt(i);
      if (c == '\'') {
        uriLiteral.append(c);
      }
      uriLiteral.append(c);
    }
    uriLiteral.append('\'');
    return uriLiteral.toString();
  }
}
