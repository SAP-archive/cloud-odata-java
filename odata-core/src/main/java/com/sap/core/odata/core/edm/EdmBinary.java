package com.sap.core.odata.core.edm;

import java.util.Locale;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Implementation of the EDM simple type Binary
 * @author SAP AG
 */
public class EdmBinary implements EdmSimpleType {

  private static final EdmBinary instance = new EdmBinary();

  private EdmBinary() {

  }

  public static EdmBinary getInstance() {
    return instance;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof EdmBinary;
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
    return EdmSimpleTypeKind.Binary.toString();
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof EdmBinary;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      return facets == null || facets.isNullable() == null || facets.isNullable();

    if (literalKind == null)
      return false;

    switch (literalKind) {
    case DEFAULT:
    case JSON:
      try {
        return valueOfString(value, literalKind, facets) != null;
      } catch (EdmSimpleTypeException e) {
        return false;
      }

    case URI:
      return value.matches("(?:X|binary)'(?:\\p{XDigit}{2})*'")
          && (facets == null || facets.getMaxLength() == null
          || facets.getMaxLength() * 2 >= value.length() - (value.startsWith("X") ? 3 : 8));

    default:
      return false;
    }
  }

  @Override
  public byte[] valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
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
      final byte[] byteValue = Base64.decodeBase64(value);
      if (facets == null || facets.getMaxLength() == null || facets.getMaxLength() >= byteValue.length)
        return byteValue;
      else
        throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_FACETS_NOT_MATCHED.addContent(value, facets));

    case URI:
      if (validate(value, literalKind, facets))
        try {
          return Hex.decodeHex(value.substring(value.startsWith("X") ? 2 : 7, value.length() - 1).toCharArray());
        } catch (DecoderException e) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
        }
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

    byte[] byteArrayValue;
    if (value instanceof byte[]) {
      byteArrayValue = (byte[]) value;
    } else if (value instanceof Byte[]) {
      final int length = ((Byte[]) value).length;
      byteArrayValue = new byte[length];
      for (int i = 0; i < length; i++)
        byteArrayValue[i] = ((Byte[]) value)[i].byteValue();
    } else {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(value.getClass()));
    }

    if (facets != null && facets.getMaxLength() != null && byteArrayValue.length > facets.getMaxLength())
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_FACETS_NOT_MATCHED.addContent(value, facets));

    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    switch (literalKind) {
    case DEFAULT:
    case JSON:
      return Base64.encodeBase64String(byteArrayValue);
    case URI:
      return "binary'" + Hex.encodeHexString(byteArrayValue).toUpperCase(Locale.ROOT) + "'";
    default:
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_NOT_SUPPORTED.addContent(literalKind));
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return valueToString(valueOfString(literal, EdmLiteralKind.DEFAULT, null), EdmLiteralKind.URI, null);
  }
}
