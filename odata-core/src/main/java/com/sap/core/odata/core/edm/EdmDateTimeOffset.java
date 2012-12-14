package com.sap.core.odata.core.edm;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Implementation of the EDM simple type DateTimeOffset
 * @author SAP AG
 */
public class EdmDateTimeOffset implements EdmSimpleType {

  private static final Pattern PATTERN = Pattern.compile(
      "(\\p{Digit}{1,4})-(\\p{Digit}{1,2})-(\\p{Digit}{1,2})"
          + "T(\\p{Digit}{1,2}):(\\p{Digit}{1,2})(?::(\\p{Digit}{1,2})(\\.\\p{Digit}{1,7})?)?"
          + "(Z|([-+]\\p{Digit}{1,2}:\\p{Digit}{2}))?");
  private static final Pattern JSON_PATTERN = Pattern.compile(
      "\\\\/Date\\((-?\\p{Digit}+)"
          + "(?:(\\+|-)(\\p{Digit}{1,4}))?"
          + "\\)\\\\/");
  private static final EdmDateTimeOffset instance = new EdmDateTimeOffset();

  private EdmDateTimeOffset() {

  }

  public static EdmDateTimeOffset getInstance() {
    return instance;
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj || obj instanceof EdmDateTimeOffset;
  }

  @Override
  public int hashCode() {
    return EdmSimpleTypeKind.DateTimeOffset.hashCode();
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
    return EdmSimpleTypeKind.DateTimeOffset.toString();
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof EdmDateTimeOffset;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      valueOfString(value, literalKind, facets);
      return true;
    } catch (EdmSimpleTypeException e) {
      return false;
    }
  }

  @Override
  public Calendar valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      if (facets == null || facets.isNullable() == null || facets.isNullable())
        return null;
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_NULL_NOT_ALLOWED);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (literalKind == EdmLiteralKind.JSON) {
      final Matcher matcher = JSON_PATTERN.matcher(value);
      if (matcher.matches()) {
        Calendar dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        dateTimeValue.clear();
        try {
          dateTimeValue.setTimeInMillis(Long.parseLong(matcher.group(1)));
        } catch (NumberFormatException e) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
        }
        if (matcher.group(2) != null) {
          final int offsetInMinutes = Integer.parseInt(matcher.group(3));
          if (offsetInMinutes == 0)
            return dateTimeValue;
          if (offsetInMinutes >= 24 * 60)
            throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
          dateTimeValue.setTimeZone(TimeZone.getTimeZone(
              "GMT" + matcher.group(2) + String.valueOf(offsetInMinutes / 60)
              + ":" + String.format("%02d", offsetInMinutes % 60)));
          // Subtract the time-zone offset to counter the automatic adjustment above
          // caused by the fact that the Calendar instance created above is in the
          // "GMT" timezone.
          dateTimeValue.add(Calendar.MILLISECOND, 0 - dateTimeValue.get(Calendar.ZONE_OFFSET));
        }
        return dateTimeValue;
      }

    } else if (literalKind == EdmLiteralKind.URI) {
      if (value.length() > 16 && value.startsWith("datetimeoffset'") && value.endsWith("'"))
        return valueOfString(value.substring(15, value.length() - 1), EdmLiteralKind.DEFAULT, facets);
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    }

    final Matcher matcher = PATTERN.matcher(value);
    if (!matcher.matches())
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

    if (matcher.group(8) == null) {
      return EdmDateTime.getInstance().valueOfString(value, EdmLiteralKind.DEFAULT, facets);
    } else {
      Calendar dateTimeValue = EdmDateTime.getInstance().valueOfString(value.substring(0, matcher.start(8)), EdmLiteralKind.DEFAULT, facets);
      if (matcher.group(9) == null || matcher.group(9).matches("[-+]0+:0+")) {
        return dateTimeValue;
      } else {
        dateTimeValue.setTimeZone(TimeZone.getTimeZone("GMT" + matcher.group(9)));
        if (dateTimeValue.get(Calendar.ZONE_OFFSET) == 0) // invalid offset
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
        // Subtract the time-zone offset to counter the automatic adjustment above
        // caused by the fact that the Calendar instance returned from EdmDateTime
        // is in the "GMT" timezone.
        dateTimeValue.add(Calendar.MILLISECOND, 0 - dateTimeValue.get(Calendar.ZONE_OFFSET));
        return dateTimeValue;
      }
    }
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
      return "\\/Date("
          + Long.toString(dateTimeValue.getTimeInMillis())
          + (offset == 0 ? "" : String.format("%+05d", offsetInMinutes))
          + ")\\/";

    } else {
      final String localTimeString = EdmDateTime.getInstance().valueToString(dateTimeValue, EdmLiteralKind.DEFAULT, facets);
      final int offsetHours = offsetInMinutes / 60;
      final int offsetMinutes = Math.abs(offsetInMinutes % 60);
      final String offsetString = offset == 0 ? "Z" : String.format("%+03d:%02d", offsetHours, offsetMinutes);

      if (literalKind == EdmLiteralKind.URI)
        return "datetimeoffset'" + localTimeString + offsetString + "'";
      else
        return localTimeString + offsetString;
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "datetimeoffset'" + literal + "'";
  }

}
