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
 * Implementation of the EDM simple type Single.
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
  public Class<?> getDefaultType() {
    return Float.class;
  }

  @Override
  protected <T> T internalValueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    String valueString = value;
    Float result = null;
    // Handle special values first.
    if (value.equals("-INF")) {
      result = Float.NEGATIVE_INFINITY;
    } else if (value.equals("INF")) {
      result = Float.POSITIVE_INFINITY;
    } else if (value.equals("NaN")) {
      result = Float.NaN;
    } else {
      // Now only "normal" numbers remain.
      final Matcher matcher = PATTERN.matcher(value);
      if (!matcher.matches()
          || (literalKind == EdmLiteralKind.URI) == (matcher.group(1) == null))
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

      if (literalKind == EdmLiteralKind.URI)
        valueString = value.substring(0, value.length() - 1);

      // The number format is checked above, so we don't have to catch NumberFormatException.
      result = Float.valueOf(valueString);
      // "Real" infinite values have been treated already above, so we can throw an exception
      // if the conversion to a float results in an infinite value.
      if (result.isInfinite())
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    }

    if (returnType.isAssignableFrom(Float.class))
      return returnType.cast(result);
    else if (returnType.isAssignableFrom(Double.class))
      if (result.isInfinite() || result.isNaN())
        return returnType.cast(result.doubleValue());
      else
        return returnType.cast(Double.valueOf(valueString));
    else if (result.isInfinite() || result.isNaN())
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_UNCONVERTIBLE_TO_VALUE_TYPE.addContent(value, returnType));
    else
      try {
        final BigDecimal valueBigDecimal = new BigDecimal(valueString);
        if (returnType.isAssignableFrom(BigDecimal.class))
          return returnType.cast(valueBigDecimal);
        else if (returnType.isAssignableFrom(Long.class))
          return returnType.cast(valueBigDecimal.longValueExact());
        else if (returnType.isAssignableFrom(Integer.class))
          return returnType.cast(valueBigDecimal.intValueExact());
        else if (returnType.isAssignableFrom(Short.class))
          return returnType.cast(valueBigDecimal.shortValueExact());
        else if (returnType.isAssignableFrom(Byte.class))
          return returnType.cast(valueBigDecimal.byteValueExact());
        else
          throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));

      } catch (final ArithmeticException e) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_UNCONVERTIBLE_TO_VALUE_TYPE.addContent(value, returnType), e);
      }
  }

  @Override
  protected <T> String internalValueToString(final T value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value instanceof Long || value instanceof Integer)
      if (Math.abs(((Number) value).longValue()) < Math.pow(10, MAX_PRECISION))
        return value.toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else if (value instanceof Short || value instanceof Byte)
      return value.toString();
    else if (value instanceof Double)
      if (((Double) value).isInfinite())
        return value.toString().toUpperCase(Locale.ROOT).substring(0, value.toString().length() - 5);
      else if (Float.isInfinite(((Double) value).floatValue()))
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
      else
        return Float.toString(((Double) value).floatValue());
    else if (value instanceof Float)
      if (((Float) value).isInfinite())
        return value.toString().toUpperCase(Locale.ROOT).substring(0, value.toString().length() - 5);
      else
        return value.toString();
    else if (value instanceof BigDecimal)
      if (((BigDecimal) value).precision() <= MAX_PRECISION && Math.abs(((BigDecimal) value).scale()) <= MAX_SCALE)
        return ((BigDecimal) value).toString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value));
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
  }

  @Override
  public String toUriLiteral(final String literal) {
    return literal.equals("-INF") || literal.equals("INF") || literal.equals("NaN") ?
        literal : literal + "F";
  }
}
