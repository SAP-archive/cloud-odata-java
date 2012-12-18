package com.sap.core.odata.core.edm;

import java.util.UUID;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Guid
 * @author SAP AG
 */
public class EdmGuid extends AbstractSimpleType {

  private static final String PATTERN = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";
  private static final EdmGuid instance = new EdmGuid();

  public static EdmGuid getInstance() {
    return instance;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      return facets == null || facets.isNullable() == null || facets.isNullable();
    else if (literalKind == EdmLiteralKind.URI)
      return value.matches(toUriLiteral(PATTERN));
    else
      return value.matches(PATTERN);
  }

  @Override
  public UUID valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (validate(value, literalKind, facets))
      if (value == null)
        return null;
      else if (literalKind == EdmLiteralKind.URI)
        return UUID.fromString(value.substring(5, value.length() - 1));
      else
        return UUID.fromString(value);

    else if (value == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_NULL_NOT_ALLOWED);
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultValue(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (value instanceof UUID)
      if (literalKind == EdmLiteralKind.URI)
        return toUriLiteral(((UUID) value).toString());
      else
        return ((UUID) value).toString();
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
  }

  @Override
  public String toUriLiteral(final String literal) {
    return "guid'" + literal + "'";
  }

}
