package com.sap.core.odata.core.edm;

import java.math.BigDecimal;
import java.util.Locale;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Implementation of the EDM simple type Double
 * @author SAP AG
 */
public class EdmDouble implements EdmSimpleType {

  private static final String PATTERN = "-?\\p{Digit}{1,15}(?:\\.\\p{Digit}{1,15})?(?:(?:E|e)-?\\p{Digit}{1,3})?";
  // value-range limitations according to the CSDL document
  private static final int MAX_PRECISION = 15;
  private static final int MAX_SCALE = 308;

  private static final EdmDouble instance = new EdmDouble();

  private EdmDouble() {

  }

  public static EdmDouble getInstance() {
    return instance;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof EdmDouble;
  }

  @Override
  public int hashCode() {
    return EdmSimpleTypeKind.Double.hashCode();
  }

  @Override
  public String getNamespace() throws EdmException {
    return EdmSimpleType.EDM_NAMESPACE;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.SIMPLE;
  }

  @Override
  public String getName() throws EdmException {
    return EdmSimpleTypeKind.Double.toString();
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit
        || simpleType instanceof Uint7
        || simpleType instanceof EdmByte
        || simpleType instanceof EdmSByte
        || simpleType instanceof EdmInt16
        || simpleType instanceof EdmInt32
        || simpleType instanceof EdmInt64
        || simpleType instanceof EdmSingle
        || simpleType instanceof EdmDouble;
  }

  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      return facets == null || facets.isNullable() == null || facets.isNullable();
    else if (value.matches("NaN|-?INF"))
      return true;
    else if (literalKind == null)
      return false;
    else if (literalKind == EdmLiteralKind.URI)
      return value.matches(PATTERN + "(?:D|d)");
    else
      return value.matches(PATTERN);
  }

  @Override
  public Double valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (!validate(value, literalKind, facets))
      if (value == null)
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_NULL_NOT_ALLOWED);
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

    if (value == null)
      return null;
    else if (value.equals("-INF"))
      return Double.NEGATIVE_INFINITY;
    else if (value.equals("INF"))
      return Double.POSITIVE_INFINITY;
    else if (value.equals("NaN"))
      return Double.NaN;

    Double result;
    if (literalKind == EdmLiteralKind.URI)
      result = Double.parseDouble(value.substring(0, value.length() - 1));
    else
      result = Double.parseDouble(value);

    if (result.isInfinite())
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    else
      return result;
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

    String result;
    if (value instanceof Long)
      if (Math.abs((Long) value) < Math.pow(10, MAX_PRECISION))
        result = value.toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else if (value instanceof Integer || value instanceof Short || value instanceof Byte)
      result = value.toString();
    else if (value instanceof Double)
      if (((Double) value).isInfinite())
        return value.toString().toUpperCase(Locale.ROOT).substring(0, value.toString().length() - 5);
      else
        result = value.toString();
    else if (value instanceof Float)
      if (((Float) value).isInfinite())
        return value.toString().toUpperCase(Locale.ROOT).substring(0, value.toString().length() - 5);
      else
        result = value.toString();
    else if (value instanceof BigDecimal)
      if (((BigDecimal) value).precision() <= MAX_PRECISION && Math.abs(((BigDecimal) value).scale()) <= MAX_SCALE)
        result = ((BigDecimal) value).toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));

    switch (literalKind) {
    case DEFAULT:
    case JSON:
      return result;

    case URI:
      return toUriLiteral(result);

    default:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));
    }
  }

  @Override
  public String toUriLiteral(final String literal) {
    return literal + "D";
  }

}
