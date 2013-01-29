package com.sap.core.odata.core.edm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Decimal
 * @author SAP AG
 */
public class EdmDecimal extends AbstractSimpleType {

  private static final Pattern PATTERN = Pattern.compile("(?:\\+|-)?(?:0*(\\p{Digit}{1,29}?))(?:\\.(\\p{Digit}{1,29}?)0*)?(M|m)?");
  private static final EdmDecimal instance = new EdmDecimal();

  public static EdmDecimal getInstance() {
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
        || simpleType instanceof EdmSingle
        || simpleType instanceof EdmDouble
        || simpleType instanceof EdmDecimal;
  }

  @Override
  public Class<?> getDefaultType() {
    return BigDecimal.class;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      return facets == null || facets.isNullable() == null || facets.isNullable();

    if (literalKind == null)
      return false;

    return validateLiteral(value, literalKind) && validatePrecisionAndScale(value, facets);
  }

  private static boolean validateLiteral(final String value, final EdmLiteralKind literalKind) {
    final Matcher matcher = PATTERN.matcher(value);
    return matcher.matches()
        && (literalKind == EdmLiteralKind.URI) != (matcher.group(3) == null);
  }

  private static final boolean validatePrecisionAndScale(final String value, final EdmFacets facets) {
    if (facets == null || facets.getPrecision() == null && facets.getScale() == null)
      return true;

    final Matcher matcher = PATTERN.matcher(value);
    matcher.matches();
    final int significantIntegerDigits = matcher.group(1).equals("0") ? 0 : matcher.group(1).length();
    final int decimals = matcher.group(2) == null ? 0 : matcher.group(2).length();
    return (facets.getPrecision() == null || facets.getPrecision() >= significantIntegerDigits + decimals)
        && (facets.getScale() == null || facets.getScale() >= decimals);
  }

  @Override
  public <T> T valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (!validateLiteral(value, literalKind))
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    if (!validatePrecisionAndScale(value, facets))
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));

    final BigDecimal valueBigDecimal = new BigDecimal(
        literalKind == EdmLiteralKind.URI ? value.substring(0, value.length() - 1) : value);

    if (returnType.isAssignableFrom(BigDecimal.class))
      return returnType.cast(valueBigDecimal);
    else if (returnType.isAssignableFrom(Double.class))
      if (BigDecimal.valueOf(valueBigDecimal.doubleValue()).compareTo(valueBigDecimal) == 0)
        return returnType.cast(valueBigDecimal.doubleValue());
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_UNCONVERTIBLE_TO_VALUE_TYPE.addContent(value, returnType));
    else if (returnType.isAssignableFrom(Float.class))
      if (BigDecimal.valueOf(valueBigDecimal.floatValue()).compareTo(valueBigDecimal) == 0)
        return returnType.cast(valueBigDecimal.floatValue());
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_UNCONVERTIBLE_TO_VALUE_TYPE.addContent(value, returnType));
    else
      try {
        if (returnType.isAssignableFrom(BigInteger.class))
          return returnType.cast(valueBigDecimal.toBigIntegerExact());
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

      } catch (ArithmeticException e) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_UNCONVERTIBLE_TO_VALUE_TYPE.addContent(value, returnType), e);
      }
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultLiteral(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    String result;
    if (value instanceof Long || value instanceof Integer || value instanceof Short || value instanceof Byte || value instanceof BigInteger) {
      result = value.toString();
      final int digits = result.startsWith("-") ? result.length() - 1 : result.length();
      if (facets != null && facets.getPrecision() != null && facets.getPrecision() < digits)
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets));

    } else if (value instanceof Double || value instanceof Float || value instanceof BigDecimal) {
      BigDecimal bigDecimalValue;
      try {
        if (value instanceof Double)
          bigDecimalValue = BigDecimal.valueOf((Double) value);
        else if (value instanceof Float)
          bigDecimalValue = BigDecimal.valueOf((Float) value);
        else
          bigDecimalValue = (BigDecimal) value;
      } catch (NumberFormatException e) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_ILLEGAL_CONTENT.addContent(value), e);
      }

      int digits = bigDecimalValue.precision();
      if (bigDecimalValue.scale() < 0)
        digits -= bigDecimalValue.scale();
      if (bigDecimalValue.scale() > bigDecimalValue.precision())
        digits = bigDecimalValue.scale();
      if (facets == null
          || (facets.getPrecision() == null || facets.getPrecision() >= digits)
          && (facets.getScale() == null || facets.getScale() >= bigDecimalValue.scale()))
        result = bigDecimalValue.toPlainString();
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets));

    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
    }

    if (literalKind == EdmLiteralKind.URI)
      result = toUriLiteral(result);

    return result;
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return literal + "M";
  }
}
