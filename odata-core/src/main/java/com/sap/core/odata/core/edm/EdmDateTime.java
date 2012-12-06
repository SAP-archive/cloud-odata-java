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
  public int hashCode() {
    return EdmSimpleTypeKind.DateTime.hashCode();
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

    Calendar dateTimeValue = Calendar.getInstance();
    dateTimeValue.clear();

    switch (literalKind) {
    case DEFAULT:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));

    case JSON:
      if (value.matches("\\\\/Date\\((-?\\p{Digit}+)\\)\\\\/"))
        dateTimeValue.setTimeInMillis(Long.decode(value.substring(7, value.length() - 3)));
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
      return dateTimeValue;

    case URI:
      if (value.length() > 10 && value.startsWith("datetime'") && value.endsWith("'"))
        return valueOfString(value.substring(9, value.length() - 1), EdmLiteralKind.DEFAULT, facets);
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));

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

    switch (literalKind) {
    case DEFAULT:
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

      return result;

    case JSON:
      return "\\/Date(" + dateTimeValue.getTimeInMillis() + ")\\/";

    case URI:
      return toUriLiteral(valueToString(value, EdmLiteralKind.DEFAULT, facets));

    default:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "datetime'" + literal + "'";
  }

}
