package com.sap.core.odata.core.edm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Implementation of the EDM simple type DateTime
 * @author SAP AG
 */
public class EdmDateTime implements EdmSimpleType {

  private static final EdmDateTime instance = new EdmDateTime();

  private EdmDateTime() {

  }

  public static EdmDateTime getInstance() {
    return instance;
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj || obj instanceof EdmDateTime;
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
    return EdmSimpleTypeKind.DateTime.toString();
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof EdmDateTime;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      valueOfString(value, literalKind, facets);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }

  @Override
  public Object valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      if (facets == null || facets.isNullable() == null || facets.isNullable())
        return null;
      else
        throw new IllegalArgumentException();

    if (literalKind == null)
      throw new IllegalArgumentException();

    switch (literalKind) {
    case DEFAULT:
    case JSON:
      throw new IllegalArgumentException();

    case URI:
      throw new IllegalArgumentException();

    default:
      throw new IllegalArgumentException();
    }
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      if (facets == null)
        return null;
      else if (facets.getDefaultValue() == null)
        if (facets.isNullable() == null || facets.isNullable())
          return null;
        else
          throw new IllegalArgumentException();
      else
        return facets.getDefaultValue();

    if (literalKind == null)
      throw new IllegalArgumentException();

    Calendar dateTimeValue = Calendar.getInstance();
    dateTimeValue.clear();
    if (value instanceof Date) {
      dateTimeValue.setTime((Date) value);
    } else if (value instanceof Calendar) {
      dateTimeValue.setTime(((Calendar) value).getTime());
    } else if (value instanceof Long) {
      dateTimeValue.setTimeInMillis((Long) value);
    } else {
      throw new IllegalArgumentException();
    }

    switch (literalKind) {
    case DEFAULT:
      int digits = 3;  // precision at most to milliseconds
      if (facets != null && facets.getPrecision() != null && facets.getPrecision() < 3) {
        digits = facets.getPrecision();
        adjustMilliseconds(dateTimeValue, digits);
      }

      final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
      SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      if (dateTimeValue.get(Calendar.MILLISECOND) == 0)
        dateFormat.applyPattern(pattern);
      else
        dateFormat.applyPattern(pattern + ".SSS");

      final String result = dateFormat.format(dateTimeValue.getTime());
      if (result.contains("."))
        if (digits == 0)
          return result.substring(0, pattern.length() - 2);  // beware of the "'"s
        else if (digits == 3)
          return result;
        else
          return result.substring(0, pattern.length() - 2 + 1 + digits);  // beware of the "'"s
      else
        return result;

    case JSON:
      return "\\/Date(" + dateTimeValue.getTimeInMillis() + ")\\/";

    case URI:
      return toUriLiteral(valueToString(value, EdmLiteralKind.DEFAULT, facets));

    default:
      throw new IllegalArgumentException();
    }
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
    return "datetime'" + literal + "'";
  }

}
