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
 * <p>Implementation of the EDM simple type Time.</p>
 * <p>Arguably, this type is intended to represent a time of day, not an instance in time.
 * The time value is interpreted and formatted as local time.</p>
 * <p>Formatting simply ignores the year, month, and day parts of time instances.
 * Parsing returns a Calendar object where all unused fields have been cleared.</p>
 * @author SAP AG
 */
public class EdmTime extends AbstractSimpleType {

  private static final Pattern PATTERN = Pattern.compile(
      "PT(?:(\\p{Digit}{1,2})H)?(?:(\\p{Digit}{1,4})M)?(?:(\\p{Digit}{1,5})(?:\\.(\\p{Digit}+?)0*)?S)?");
  private static final EdmTime instance = new EdmTime();

  public static EdmTime getInstance() {
    return instance;
  }

  @Override
  public Class<?> getDefaultType() {
    return Calendar.class;
  }

  @Override
  protected <T> T internalValueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    Calendar valueCalendar;
    if (literalKind == EdmLiteralKind.URI) {
      if (value.length() > 6 && value.startsWith("time'") && value.endsWith("'")) {
        valueCalendar = parseLiteral(value.substring(5, value.length() - 1), facets);
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      }
    } else {
      valueCalendar = parseLiteral(value, facets);
    }

    if (returnType.isAssignableFrom(Calendar.class)) {
      return returnType.cast(valueCalendar);
    } else if (returnType.isAssignableFrom(Long.class)) {
      return returnType.cast(valueCalendar.getTimeInMillis());
    } else if (returnType.isAssignableFrom(Date.class)) {
      return returnType.cast(valueCalendar.getTime());
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
    }
  }

  private Calendar parseLiteral(final String literal, final EdmFacets facets) throws EdmSimpleTypeException {
    final Matcher matcher = PATTERN.matcher(literal);
    if (!matcher.matches()
        || (matcher.group(1) == null && matcher.group(2) == null && matcher.group(3) == null)) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(literal));
    }

    Calendar dateTimeValue = Calendar.getInstance();
    dateTimeValue.clear();

    dateTimeValue.set(Calendar.HOUR_OF_DAY,
        matcher.group(1) == null ? 0 : Integer.parseInt(matcher.group(1)));
    dateTimeValue.set(Calendar.MINUTE,
        matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2)));
    dateTimeValue.set(Calendar.SECOND,
        matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3)));

    if (matcher.group(4) != null) {
      if (facets == null || facets.getPrecision() == null || facets.getPrecision() >= matcher.group(4).length()) {
        if (matcher.group(4).length() <= 3) {
          dateTimeValue.set(Calendar.MILLISECOND,
              Short.parseShort(matcher.group(4) + "000".substring(0, 3 - matcher.group(4).length())));
        } else {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(literal));
        }
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(literal, facets));
      }
    }

    if (dateTimeValue.get(Calendar.DAY_OF_YEAR) == 1) {
      return dateTimeValue;
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(literal));
    }
  }

  @Override
  protected <T> String internalValueToString(final T value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    Calendar dateTimeValue;
    if (value instanceof Date) {
      dateTimeValue = Calendar.getInstance();
      dateTimeValue.clear();
      dateTimeValue.setTime((Date) value);
    } else if (value instanceof Calendar) {
      dateTimeValue = (Calendar) ((Calendar) value).clone();
    } else if (value instanceof Long) {
      dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
      dateTimeValue.clear();
      dateTimeValue.setTimeInMillis((Long) value);
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
    }

    StringBuilder result = new StringBuilder(15); // 15 characters are enough for millisecond precision.
    result.append('P');
    result.append('T');
    result.append(dateTimeValue.get(Calendar.HOUR_OF_DAY));
    result.append('H');
    result.append(dateTimeValue.get(Calendar.MINUTE));
    result.append('M');
    result.append(dateTimeValue.get(Calendar.SECOND));

    try {
      EdmDateTime.appendMilliseconds(result, dateTimeValue.get(Calendar.MILLISECOND), facets);
    } catch (final IllegalArgumentException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets), e);
    }

    result.append('S');

    return result.toString();
  }

  @Override
  public String toUriLiteral(final String literal) {
    return "time'" + literal + "'";
  }
}
