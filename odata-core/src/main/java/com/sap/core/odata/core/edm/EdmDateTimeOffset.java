/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
 * Implementation of the EDM simple type DateTimeOffset
 * @author SAP AG
 */
public class EdmDateTimeOffset extends AbstractSimpleType {

  private static final Pattern PATTERN = Pattern.compile(
      "(\\p{Digit}{1,4})-(\\p{Digit}{1,2})-(\\p{Digit}{1,2})"
          + "T(\\p{Digit}{1,2}):(\\p{Digit}{1,2})(?::(\\p{Digit}{1,2})(\\.\\p{Digit}{1,7})?)?"
          + "(Z|([-+]\\p{Digit}{1,2}:\\p{Digit}{2}))?");
  private static final Pattern JSON_PATTERN = Pattern.compile(
      "\\\\/Date\\((-?\\p{Digit}+)"
          + "(?:(\\+|-)(\\p{Digit}{1,4}))?"
          + "\\)\\\\/");
  private static final EdmDateTimeOffset instance = new EdmDateTimeOffset();

  public static EdmDateTimeOffset getInstance() {
    return instance;
  }

  @Override
  public Class<?> getDefaultType() {
    return Calendar.class;
  }

  @Override
  public <T> T valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);
    }

    if (literalKind == EdmLiteralKind.URI) {
      if (value.length() > 16 && value.startsWith("datetimeoffset'") && value.endsWith("'")) {
        return valueOfString(value.substring(15, value.length() - 1), EdmLiteralKind.DEFAULT, facets, returnType);
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      }
    }

    Calendar dateTimeValue = null;

    if (literalKind == EdmLiteralKind.JSON) {
      final Matcher matcher = JSON_PATTERN.matcher(value);
      if (matcher.matches()) {
        dateTimeValue = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        dateTimeValue.clear();
        long millis;
        try {
          millis = Long.parseLong(matcher.group(1));
        } catch (NumberFormatException e) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
        }
        dateTimeValue.setTimeInMillis(millis);
        if (matcher.group(2) != null) {
          final int offsetInMinutes = Integer.parseInt(matcher.group(3));
          if (offsetInMinutes >= 24 * 60) {
            throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
          }
          if (offsetInMinutes != 0) {
            dateTimeValue.setTimeZone(TimeZone.getTimeZone(
                "GMT" + matcher.group(2) + String.valueOf(offsetInMinutes / 60)
                    + ":" + String.format("%02d", offsetInMinutes % 60)));
            // Subtract the time-zone offset to counter the automatic adjustment above
            // caused by the fact that the Calendar instance created above is in the
            // "GMT" timezone.
            dateTimeValue.add(Calendar.MILLISECOND, 0 - dateTimeValue.get(Calendar.ZONE_OFFSET));
          }
        }
      }
    }

    if (dateTimeValue == null) {
      final Matcher matcher = PATTERN.matcher(value);
      if (!matcher.matches()) {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      }

      if (matcher.group(8) == null) {
        return EdmDateTime.getInstance().valueOfString(value, EdmLiteralKind.DEFAULT, facets, returnType);
      } else {
        dateTimeValue = EdmDateTime.getInstance().valueOfString(value.substring(0, matcher.start(8)), EdmLiteralKind.DEFAULT, facets, Calendar.class);
        if (matcher.group(9) != null && !matcher.group(9).matches("[-+]0+:0+")) {
          dateTimeValue.setTimeZone(TimeZone.getTimeZone("GMT" + matcher.group(9)));
          if (dateTimeValue.get(Calendar.ZONE_OFFSET) == 0) {
            throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
          }
          // Subtract the time-zone offset to counter the automatic adjustment above
          // caused by the fact that the Calendar instance returned from EdmDateTime
          // is in the "GMT" timezone.
          dateTimeValue.add(Calendar.MILLISECOND, 0 - dateTimeValue.get(Calendar.ZONE_OFFSET));
        }
      }
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
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null) {
      return getNullOrDefaultLiteral(facets);
    }

    if (literalKind == null) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);
    }

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

      if (literalKind == EdmLiteralKind.URI) {
        return toUriLiteral(localTimeString + offsetString);
      } else {
        return localTimeString + offsetString;
      }
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "datetimeoffset'" + literal + "'";
  }
}
