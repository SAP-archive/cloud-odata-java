package com.sap.core.odata.core.edm;

import java.text.SimpleDateFormat;
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
 * <p>Implementation of the EDM simple type Time</p>
 * <p>Arguably, this type is intended to represent a time of day, not an instance in time.
 * The year, month, and day parts of time instances are simply ignored.
 * The time value is interpreted and formatted as local time.</p>
 * @author SAP AG
 */
public class EdmTime implements EdmSimpleType {

  private static final Pattern PATTERN = Pattern.compile(
      "PT(?:(\\p{Digit}+)H)?(?:(\\p{Digit}+)M)?(?:(\\p{Digit}+)(?:\\.(\\p{Digit}+?)0*)?S)?");
  private static final EdmTime instance = new EdmTime();

  private EdmTime() {

  }

  public static EdmTime getInstance() {
    return instance;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof EdmTime;
  }

  @Override
  public int hashCode() {
    return EdmSimpleTypeKind.Time.hashCode();
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
    return EdmSimpleTypeKind.Time.toString();
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof EdmTime;
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

    if (literalKind == EdmLiteralKind.URI)
      if (value.length() > 6 && value.startsWith("time'") && value.endsWith("'"))
        return valueOfString(value.substring(5, value.length() - 1), EdmLiteralKind.DEFAULT, facets);
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

    final Matcher matcher = PATTERN.matcher(value);
    if (!matcher.matches()
        || (matcher.group(1) == null && matcher.group(2) == null && matcher.group(3) == null))
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

    Calendar dateTimeValue = Calendar.getInstance();
    dateTimeValue.clear();

    if (matcher.group(1) != null)
      dateTimeValue.set(Calendar.HOUR, Integer.parseInt(matcher.group(1)));
    if (matcher.group(2) != null)
      dateTimeValue.set(Calendar.MINUTE, Integer.parseInt(matcher.group(2)));
    if (matcher.group(3) != null)
      dateTimeValue.set(Calendar.SECOND, Integer.parseInt(matcher.group(3)));

    if (matcher.group(4) != null)
      if (facets == null || facets.getPrecision() == null || facets.getPrecision() >= matcher.group(4).length())
        if (matcher.group(4).length() <= 3)
          dateTimeValue.set(Calendar.MILLISECOND,
              Short.parseShort(matcher.group(4) + "000".substring(0, 3 - matcher.group(4).length())));
        else
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));

    return dateTimeValue;
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

    final String pattern = "'PT'H'H'm'M's.SSS";
    SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
    dateFormat.setTimeZone(dateTimeValue.getTimeZone());
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

    result += "S";

    if (literalKind == EdmLiteralKind.URI)
      return toUriLiteral(result);
    else
      return result;
  }

  @Override
  public String toUriLiteral(final String literal) {
    return "time'" + literal + "'";
  }

}
