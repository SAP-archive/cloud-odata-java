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
 * Implementation of the EDM simple type DateTimeOffset.
 * @author SAP AG
 */
public class EdmDateTimeOffset extends AbstractSimpleType {

  private static final Pattern PATTERN = Pattern.compile(
      "(\\p{Digit}{1,4})-(\\p{Digit}{1,2})-(\\p{Digit}{1,2})"
          + "T(\\p{Digit}{1,2}):(\\p{Digit}{1,2})(?::(\\p{Digit}{1,2})(\\.\\p{Digit}{1,7})?)?"
          + "(Z|([-+]\\p{Digit}{1,2}:\\p{Digit}{2}))?");
  private static final Pattern JSON_PATTERN = Pattern.compile(
      "/Date\\((-?\\p{Digit}+)(?:(\\+|-)(\\p{Digit}{1,4}))?\\)/");
  private static final EdmDateTimeOffset instance = new EdmDateTimeOffset();

  public static EdmDateTimeOffset getInstance() {
    return instance;
  }

  @Override
  public Class<?> getDefaultType() {
    return Calendar.class;
  }

  @Override
  protected <T> T internalValueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    if (literalKind == EdmLiteralKind.URI) {
      if (value.length() > 16 && value.startsWith("datetimeoffset'") && value.endsWith("'")) {
        return internalValueOfString(value.substring(15, value.length() - 1), EdmLiteralKind.DEFAULT, facets, returnType);
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      }
    }

    Calendar dateTimeValue = null;

    if (literalKind == EdmLiteralKind.JSON) {
      final Matcher matcher = JSON_PATTERN.matcher(value);
      if (matcher.matches()) {
        long millis;
        try {
          millis = Long.parseLong(matcher.group(1));
        } catch (final NumberFormatException e) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
        }
        String timeZone = "GMT";
        if (matcher.group(2) != null) {
          final int offsetInMinutes = Integer.parseInt(matcher.group(3));
          if (offsetInMinutes >= 24 * 60)
            throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
          if (offsetInMinutes != 0) {
            timeZone += matcher.group(2) + String.valueOf(offsetInMinutes / 60)
                + ":" + String.format("%02d", offsetInMinutes % 60);
            // Convert the local-time milliseconds to UTC.
            millis -= (matcher.group(2).equals("+") ? 1 : -1) * offsetInMinutes * 60 * 1000;
          }
        }
        dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        dateTimeValue.clear();
        dateTimeValue.setTimeInMillis(millis);
      }
    }

    if (dateTimeValue == null) {
      final Matcher matcher = PATTERN.matcher(value);
      if (!matcher.matches())
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

      final String timeZoneOffset = matcher.group(8) != null && matcher.group(9) != null && !matcher.group(9).matches("[-+]0+:0+") ? matcher.group(9) : null;
      dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone("GMT" + timeZoneOffset));
      if (dateTimeValue.get(Calendar.ZONE_OFFSET) == 0 && timeZoneOffset != null)
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      dateTimeValue.clear();
      EdmDateTime.parseLiteral(value.substring(0, matcher.group(8) == null ? value.length() : matcher.start(8)), facets, dateTimeValue);
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
      dateTimeValue = Calendar.getInstance();
      dateTimeValue.clear();
      dateTimeValue.setTimeZone(TimeZone.getTimeZone("GMT"));
      dateTimeValue.setTimeInMillis((Long) value);
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
    }

    final int offset = dateTimeValue.get(Calendar.ZONE_OFFSET) + dateTimeValue.get(Calendar.DST_OFFSET);
    dateTimeValue.add(Calendar.MILLISECOND, offset); // to counter UTC output below
    final int offsetInMinutes = offset / 60 / 1000;

    if (literalKind == EdmLiteralKind.JSON) {
      return "/Date("
          + Long.toString(dateTimeValue.getTimeInMillis())
          + (offset == 0 ? "" : String.format("%+05d", offsetInMinutes))
          + ")/";

    } else {
      final String localTimeString = EdmDateTime.getInstance().valueToString(dateTimeValue, EdmLiteralKind.DEFAULT, facets);
      final int offsetHours = offsetInMinutes / 60;
      final int offsetMinutes = Math.abs(offsetInMinutes % 60);
      final String offsetString = offset == 0 ? "Z" : String.format("%+03d:%02d", offsetHours, offsetMinutes);

      return localTimeString + offsetString;
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "datetimeoffset'" + literal + "'";
  }
}
