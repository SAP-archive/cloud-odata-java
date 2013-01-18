package com.sap.core.odata.core.edm;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Single
 * @author SAP AG
 */
public class EdmSingle extends AbstractSimpleType {

  // value-range limitations according to the CSDL document
  private static final int MAX_PRECISION = 7;
  private static final int MAX_SCALE = 38;

  private static final Pattern PATTERN = Pattern.compile(
      "(?:\\+|-)?\\p{Digit}{1,7}(?:\\.\\p{Digit}{1,7})?(?:(?:E|e)(?:\\+|-)?\\p{Digit}{1,2})?(F|f)?");
  private static final EdmSingle instance = new EdmSingle();

  public static EdmSingle getInstance() {
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
        || simpleType instanceof EdmInt64
        || simpleType instanceof EdmSingle;
  }

  @Override
  public Float valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    final Matcher matcher = PATTERN.matcher(value);
    if (!matcher.matches())
      if (value.equals("-INF"))
        return Float.NEGATIVE_INFINITY;
      else if (value.equals("INF"))
        return Float.POSITIVE_INFINITY;
      else if (value.equals("NaN"))
        return Float.NaN;
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    if ((literalKind == EdmLiteralKind.URI) == (matcher.group(1) == null))
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

    // The number format is checked above, so we don't have to catch NumberFormatException.
    Float result;
    if (literalKind == EdmLiteralKind.URI)
      result = Float.valueOf(value.substring(0, value.length() - 1));
    else
      result = Float.valueOf(value);

    // Values outside the value range have been set to Infinity by Float.valueOf();
    // "real" infinite values have been treated already above, so we can throw an exception
    // if we see them here.
    if (result.isInfinite())
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    else
      return result;
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultLiteral(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    String result;
    if (value instanceof Long || value instanceof Integer)
      if (Math.abs(((Number) value).longValue()) < Math.pow(10, MAX_PRECISION))
        result = value.toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else if (value instanceof Short || value instanceof Byte)
      result = value.toString();
    else if (value instanceof Double)
      if (((Double) value).isInfinite())
        return value.toString().toUpperCase(Locale.ROOT).substring(0, value.toString().length() - 5);
      else if (Float.isInfinite(((Double) value).floatValue()))
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
      else
        result = Float.toString(((Double) value).floatValue());
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

    if (literalKind == EdmLiteralKind.URI)
      return toUriLiteral(result);
    else
      return result;
  }

  @Override
  public String toUriLiteral(final String literal) {
    return literal + "F";
  }

}
