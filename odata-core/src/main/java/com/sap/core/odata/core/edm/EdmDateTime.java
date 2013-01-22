package com.sap.core.odata.core.edm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type DateTime
 * @author SAP AG
 */
public class EdmDateTime extends AbstractSimpleType {

  private static final Pattern PATTERN = Pattern.compile(
      "(\\p{Digit}{1,4})-(\\p{Digit}{1,2})-(\\p{Digit}{1,2})"
          + "T(\\p{Digit}{1,2}):(\\p{Digit}{1,2})(?::(\\p{Digit}{1,2})(\\.\\p{Digit}{1,7})?)?");
  private static final Pattern JSON_PATTERN = Pattern.compile("\\\\/Date\\((-?\\p{Digit}+)\\)\\\\/");
  private static final EdmDateTime instance = new EdmDateTime();

  public static EdmDateTime getInstance() {
    return instance;
  }

  @Override
  public Object valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<?> returnType) throws EdmSimpleTypeException {
    if (value == null) {
      checkNullLiteralAllowed(facets);
      return null;
    }

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    // In JSON, we allow also the XML literal form, so there is on purpose
    // no exception if the JSON pattern does not match.
    if (literalKind == EdmLiteralKind.JSON)
      if (JSON_PATTERN.matcher(value).matches()) {
        long millis;
        try {
          millis = Long.parseLong(value.substring(7, value.length() - 3));
        } catch (NumberFormatException e) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
        }
        if (returnType == long.class || returnType == Long.class)
          return millis;
        else if (returnType == Date.class)
          return new Date(millis);
        else if (returnType != null && returnType != Calendar.class)
          throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));

        Calendar dateTimeValue = Calendar.getInstance();
        dateTimeValue.clear();
        dateTimeValue.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateTimeValue.setTimeInMillis(millis);
        return dateTimeValue;
      }

    Calendar calendarValue;
    if (literalKind == EdmLiteralKind.URI)
      if (value.length() > 10 && value.startsWith("datetime'") && value.endsWith("'"))
        calendarValue = parseLiteral(value.substring(9, value.length() - 1).replace("%3A", ":"), facets);
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    else
      calendarValue = parseLiteral(value, facets);

    if (returnType == null || returnType == Calendar.class)
      return calendarValue;
    else if (returnType == long.class || returnType == Long.class)
      return calendarValue.getTimeInMillis();
    else if (returnType == Date.class)
      return calendarValue.getTime();
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));
  }

  private Calendar parseLiteral(final String value, final EdmFacets facets) throws EdmSimpleTypeException {
    Calendar dateTimeValue = Calendar.getInstance();
    dateTimeValue.clear();
    dateTimeValue.setTimeZone(TimeZone.getTimeZone("GMT"));

    final Matcher matcher = PATTERN.matcher(value);
    if (!matcher.matches())
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

    dateTimeValue.set(
        Short.parseShort(matcher.group(1)),
        Byte.parseByte(matcher.group(2)) - 1, // month is zero-based
        Byte.parseByte(matcher.group(3)),
        Byte.parseByte(matcher.group(4)),
        Byte.parseByte(matcher.group(5)));
    if (matcher.group(6) != null)
      dateTimeValue.set(Calendar.SECOND, Byte.parseByte(matcher.group(6)));

    if (matcher.group(7) != null) {
      String milliSeconds = matcher.group(7).substring(1);
      while (milliSeconds.endsWith("0"))
        milliSeconds = milliSeconds.substring(0, milliSeconds.length() - 1);
      if (milliSeconds.length() > 3)
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      if (facets != null && facets.getPrecision() != null && facets.getPrecision() < milliSeconds.length())
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));
      while (milliSeconds.length() < 3)
        milliSeconds += "0";
      dateTimeValue.set(Calendar.MILLISECOND, Short.parseShort(milliSeconds));
    }

    // The Calendar class does not check any values until a get method is called,
    // so we do just that to validate the fields set above, not because we want
    // to return something else.  For strict checks, the lenient mode is switched
    // off temporarily.
    dateTimeValue.setLenient(false);
    try {
      dateTimeValue.getTimeInMillis();
    } catch (IllegalArgumentException e) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
    }
    dateTimeValue.setLenient(true);
    return dateTimeValue;
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultLiteral(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    Calendar dateTimeValue = Calendar.getInstance();
    dateTimeValue.clear();
    if (value instanceof Date)
      dateTimeValue.setTime((Date) value);
    else if (value instanceof Calendar)
      dateTimeValue.setTime(((Calendar) value).getTime());
    else if (value instanceof Long)
      dateTimeValue.setTimeInMillis((Long) value);
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));

    if (literalKind == EdmLiteralKind.JSON)
      return "\\/Date(" + dateTimeValue.getTimeInMillis() + ")\\/";

    final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    dateFormat.applyPattern(pattern);
    String result = dateFormat.format(dateTimeValue.getTime());

    if (facets == null || facets.getPrecision() == null)
      while (result.endsWith("0"))
        result = result.substring(0, result.length() - 1);
    else if (facets.getPrecision() <= 3)
      if (result.endsWith("000".substring(0, 3 - facets.getPrecision())))
        result = result.substring(0, result.length() - (3 - facets.getPrecision()));
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets));
    else
      for (int i = 4; i <= facets.getPrecision(); i++)
        result += "0";

    if (result.endsWith("."))
      result = result.substring(0, result.length() - 1);

    if (literalKind == EdmLiteralKind.URI)
      return toUriLiteral(result);
    else
      return result;
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "datetime'" + literal.replace(":", "%3A") + "'";
  }

}
