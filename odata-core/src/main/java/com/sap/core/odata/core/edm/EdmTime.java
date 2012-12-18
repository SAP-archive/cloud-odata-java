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
 * <p>Implementation of the EDM simple type Time</p>
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
  public Calendar valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getCheckedNullValue(facets);

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (literalKind == EdmLiteralKind.URI)
      if (value.length() > 6 && value.startsWith("time'") && value.endsWith("'"))
        return parseLiteral(value.substring(5, value.length() - 1), facets);
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
    else
      return parseLiteral(value, facets);
  }

  private Calendar parseLiteral(final String literal, final EdmFacets facets) throws EdmSimpleTypeException {
    final Matcher matcher = PATTERN.matcher(literal);
    if (!matcher.matches()
        || (matcher.group(1) == null && matcher.group(2) == null && matcher.group(3) == null))
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(literal));

    Calendar dateTimeValue = Calendar.getInstance();
    dateTimeValue.clear();

    if (matcher.group(1) != null)
      dateTimeValue.set(Calendar.HOUR_OF_DAY, Integer.parseInt(matcher.group(1)));
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
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(literal));
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(literal, facets));

    if (dateTimeValue.get(Calendar.DAY_OF_YEAR) == 1) // not beyond the current (initial) day
      return dateTimeValue;
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(literal));
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultValue(facets);

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
