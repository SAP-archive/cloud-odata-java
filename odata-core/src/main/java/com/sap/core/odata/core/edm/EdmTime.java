package com.sap.core.odata.core.edm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Implementation of the EDM simple type Time
 * @author SAP AG
 */
public class EdmTime implements EdmSimpleType {

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
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeKind.edmNamespace;
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
    return null;
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

    int digits = 3; // precision at most to milliseconds
    if (facets != null && facets.getPrecision() != null && facets.getPrecision() < 3) {
      digits = facets.getPrecision();
      adjustMilliseconds(dateTimeValue, digits);
    }

    // Arguably, Edm.Time is intended to represent a time of day, not an instance in time.
    // The year, month, and day parts of time instances are simply ignored.
    // The time value is interpreted and formatted as local time.
    final String pattern = "'PT'H'H'm'M's";
    SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
    dateFormat.setTimeZone(dateTimeValue.getTimeZone());
    if (dateTimeValue.get(Calendar.MILLISECOND) == 0)
      dateFormat.applyPattern(pattern);
    else
      dateFormat.applyPattern(pattern + ".SSS");

    String result = dateFormat.format(dateTimeValue.getTime());
    if (result.contains("."))
      if (digits == 0)
        result = result.substring(0, result.length() - 4) + "S";
      else
        result = result.substring(0, result.length() - 3 + digits) + "S";
    else
      result = result + "S";

    if (literalKind == EdmLiteralKind.URI)
      return toUriLiteral(result);
    else
      return result;
  }

  private static void adjustMilliseconds(Calendar dateTimeValue, final int digits) {
    int roundToValue = 1;
    for (int i = 0; i < 3 - digits; i++)
      roundToValue *= 10;
    final int millis = dateTimeValue.get(Calendar.MILLISECOND);

    if (millis % roundToValue >= 5)
      dateTimeValue.add(Calendar.MILLISECOND, roundToValue - millis % roundToValue);
  }

  @Override
  public String toUriLiteral(final String literal) {
    return "time'" + literal + "'";
  }

}
