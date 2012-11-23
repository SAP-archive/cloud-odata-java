package com.sap.core.odata.core.edm;

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
 * Implementation of the EDM simple type DateTimeOffset
 * @author SAP AG
 */
public class EdmDateTimeOffset implements EdmSimpleType {

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
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeKind.edmNamespace;
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
      throw new IllegalArgumentException();
    }

    final int offset = dateTimeValue.get(Calendar.ZONE_OFFSET) + dateTimeValue.get(Calendar.DST_OFFSET);
    dateTimeValue.add(Calendar.MILLISECOND, offset); // to counter UTC output below
    final int offsetInMinutes = offset / 60 / 1000;
    final int offsetHours = offsetInMinutes / 60;
    final int offsetMinutes = Math.abs(offsetInMinutes % 60);

    String localTimeString = null;
    String offsetString = null;
    if (literalKind != EdmLiteralKind.JSON) {
      localTimeString = EdmSimpleTypeKind.getSimpleTypeInstance(EdmSimpleTypeKind.DateTime)
          .valueToString(dateTimeValue, EdmLiteralKind.DEFAULT, facets);
      if (offset == 0)
        offsetString = "Z";
      else
        offsetString = String.format("%+03d:%02d", offsetHours, offsetMinutes);
    }

    switch (literalKind) {
    case DEFAULT:
      return localTimeString + offsetString;

    case JSON:
      if (offset == 0)
        offsetString = "";
      else
        offsetString = String.format("%+03d%02d", offsetHours, offsetMinutes);
      return "\\/Date(" + dateTimeValue.getTimeInMillis() + offsetString + ")\\/";

    case URI:
      return "datetimeoffset'" + localTimeString + offsetString + "'";

    default:
      throw new IllegalArgumentException();
    }
  }

  @Override
  public String toUriLiteral(final String literal) {
    return "datetimeoffset'" + literal + "'";
  }

}
