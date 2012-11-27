package com.sap.core.odata.core.edm;

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

    switch (literalKind) {
    case DEFAULT:
    case JSON:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));

    case URI:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));

    default:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));
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
    final int offsetHours = offsetInMinutes / 60;
    final int offsetMinutes = Math.abs(offsetInMinutes % 60);

    String localTimeString;
    if (literalKind == EdmLiteralKind.JSON)
      localTimeString = Long.toString(dateTimeValue.getTimeInMillis());
    else
      localTimeString = EdmDateTime.getInstance().valueToString(dateTimeValue, EdmLiteralKind.DEFAULT, facets);

    String offsetString;
    if (literalKind == EdmLiteralKind.JSON)
      if (offset == 0)
        offsetString = "";
      else
        offsetString = String.format("%+03d%02d", offsetHours, offsetMinutes);
    else
      if (offset == 0)
        offsetString = "Z";
      else
        offsetString = String.format("%+03d:%02d", offsetHours, offsetMinutes);

    switch (literalKind) {
    case DEFAULT:
      return localTimeString + offsetString;

    case JSON:
      return "\\/Date(" + localTimeString + offsetString + ")\\/";

    case URI:
      return "datetimeoffset'" + localTimeString + offsetString + "'";

    default:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "datetimeoffset'" + literal + "'";
  }

}
