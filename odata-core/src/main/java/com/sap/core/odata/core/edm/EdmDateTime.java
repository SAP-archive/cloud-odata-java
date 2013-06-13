package com.sap.core.odata.core.edm;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type DateTime.
 * @author SAP AG
 */
public class EdmDateTime extends AbstractSimpleType {

  private static final Pattern PATTERN = Pattern.compile(
      "(\\p{Digit}{1,4})-(\\p{Digit}{1,2})-(\\p{Digit}{1,2})"
          + "T(\\p{Digit}{1,2}):(\\p{Digit}{1,2})(?::(\\p{Digit}{1,2})(\\.(\\p{Digit}{0,3}?)0*)?)?");
  private static final Pattern JSON_PATTERN = Pattern.compile("/Date\\((-?\\p{Digit}+)\\)/");
  private static final EdmDateTime instance = new EdmDateTime();

  public static EdmDateTime getInstance() {
    return instance;
  }

  @Override
  public Class<?> getDefaultType() {
    return Calendar.class;
  }

  @Override
  protected <T> T internalValueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    // In JSON, we allow also the XML literal form, so there is on purpose
    // no exception if the JSON pattern does not match.
    if (literalKind == EdmLiteralKind.JSON) {
      final Matcher matcher = JSON_PATTERN.matcher(value);
      if (matcher.matches()) {
        long millis;
        try {
          millis = Long.parseLong(matcher.group(1));
        } catch (final NumberFormatException e) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
        }
        if (returnType.isAssignableFrom(Long.class)) {
          return returnType.cast(millis);
        } else if (returnType.isAssignableFrom(Date.class)) {
          return returnType.cast(new Date(millis));
        } else if (returnType.isAssignableFrom(Calendar.class)) {
          Calendar dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
          dateTimeValue.clear();
          dateTimeValue.setTimeInMillis(millis);
          return returnType.cast(dateTimeValue);
        } else {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
        }
      }
    }

    Calendar dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    dateTimeValue.clear();

    if (literalKind == EdmLiteralKind.URI) {
      if (value.length() > 10 && value.startsWith("datetime'") && value.endsWith("'")) {
        parseLiteral(value.substring(9, value.length() - 1), facets, dateTimeValue);
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      }
    } else {
      parseLiteral(value, facets, dateTimeValue);
    }

    if (returnType.isAssignableFrom(Calendar.class)) {
      return returnType.cast(dateTimeValue);
    } else if (returnType.isAssignableFrom(Long.class)) {
      return returnType.cast(dateTimeValue.getTimeInMillis());
    } else if (returnType.isAssignableFrom(Date.class)) {
      return returnType.cast(dateTimeValue.getTime());
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    }
  }

  /**
   * Parses a formatted date/time value and sets the values of a
   * {@link Calendar} object accordingly. 
   * @param value         the formatted date/time value as String
   * @param facets        additional constraints for parsing (optional)
   * @param dateTimeValue the Calendar object to be set to the parsed value
   * @throws EdmSimpleTypeException
   */
  protected static void parseLiteral(final String value, final EdmFacets facets, final Calendar dateTimeValue) throws EdmSimpleTypeException {
    final Matcher matcher = PATTERN.matcher(value);
    if (!matcher.matches()) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    }

    dateTimeValue.set(
        Short.parseShort(matcher.group(1)),
        Byte.parseByte(matcher.group(2)) - 1, // month is zero-based
        Byte.parseByte(matcher.group(3)),
        Byte.parseByte(matcher.group(4)),
        Byte.parseByte(matcher.group(5)),
        matcher.group(6) == null ? 0 : Byte.parseByte(matcher.group(6)));

    if (matcher.group(7) != null) {
      if (matcher.group(7).length() == 1 || matcher.group(7).length() > 8) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      }
      final String decimals = matcher.group(8);
      if (facets != null && facets.getPrecision() != null && facets.getPrecision() < decimals.length()) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));
      }
      final String milliSeconds = decimals + "000".substring(decimals.length());
      dateTimeValue.set(Calendar.MILLISECOND, Short.parseShort(milliSeconds));
    }

    // The Calendar class does not check any values until a get method is called,
    // so we do just that to validate the fields set above, not because we want
    // to return something else.  For strict checks, the lenient mode is switched
    // off temporarily.
    dateTimeValue.setLenient(false);
    try {
      dateTimeValue.getTimeInMillis();
    } catch (final IllegalArgumentException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
    }
    dateTimeValue.setLenient(true);
  }

  @Override
  protected <T> String internalValueToString(final T value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    long timeInMillis;
    if (value instanceof Date) {
      timeInMillis = ((Date) value).getTime();
    } else if (value instanceof Calendar) {
      timeInMillis = ((Calendar) value).getTimeInMillis();
    } else if (value instanceof Long) {
      timeInMillis = ((Long) value).longValue();
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
    }

    if (literalKind == EdmLiteralKind.JSON) {
      return "/Date(" + timeInMillis + ")/";
    }

    Calendar dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    dateTimeValue.setTimeInMillis(timeInMillis);

    StringBuilder result = new StringBuilder(23); // 23 characters are enough for millisecond precision.
    final int year = dateTimeValue.get(Calendar.YEAR);
    appendTwoDigits(result, year / 100);
    appendTwoDigits(result, year % 100);
    result.append('-');
    appendTwoDigits(result, dateTimeValue.get(Calendar.MONTH) + 1); // month is zero-based
    result.append('-');
    appendTwoDigits(result, dateTimeValue.get(Calendar.DAY_OF_MONTH));
    result.append('T');
    appendTwoDigits(result, dateTimeValue.get(Calendar.HOUR_OF_DAY));
    result.append(':');
    appendTwoDigits(result, dateTimeValue.get(Calendar.MINUTE));
    result.append(':');
    appendTwoDigits(result, dateTimeValue.get(Calendar.SECOND));

    try {
      appendMilliseconds(result, timeInMillis, facets);
    } catch (final IllegalArgumentException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets), e);
    }

    return result.toString();
  }

  /**
   * Appends the given number to the given string builder,
   * assuming that the number has at most two digits, performance-optimized.
   * @param result a {@link StringBuilder}
   * @param number an integer that must satisfy <code>0 <= number <= 99</code>
   */
  private static void appendTwoDigits(final StringBuilder result, final int number) {
    result.append((char) ('0' + number / 10));
    result.append((char) ('0' + number % 10));
  }

  protected static void appendMilliseconds(final StringBuilder result, final long milliseconds, final EdmFacets facets) throws IllegalArgumentException {
    final int digits = milliseconds % 1000 == 0 ? 0 : milliseconds % 100 == 0 ? 1 : milliseconds % 10 == 0 ? 2 : 3;
    if (digits > 0) {
      result.append('.');
      for (int d = 100; d > 0; d /= 10) {
        final byte digit = (byte) (milliseconds % (d * 10) / d);
        if (digit > 0 || milliseconds % d > 0) {
          result.append((char) ('0' + digit));
        }
      }
    }

    if (facets != null && facets.getPrecision() != null) {
      final int precision = facets.getPrecision();
      if (digits > precision) {
        throw new IllegalArgumentException();
      }
      if (digits == 0 && precision > 0) {
        result.append('.');
      }
      for (int i = digits; i < precision; i++) {
        result.append('0');
      }
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "datetime'" + literal + "'";
  }
}
