package com.sap.core.odata.core.edm;

import java.util.Locale;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the EDM simple type Binary
 * @author SAP AG
 */
public class EdmBinary extends AbstractSimpleType {

  private static final EdmBinary instance = new EdmBinary();

  public static EdmBinary getInstance() {
    return instance;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    if (value == null)
      return facets == null || facets.isNullable() == null || facets.isNullable();

    if (literalKind == null)
      return false;

    if (literalKind == EdmLiteralKind.URI)
      return value.matches("(?:X|binary)'(?:\\p{XDigit}{2})*'")
          && (facets == null || facets.getMaxLength() == null
          || facets.getMaxLength() * 2 >= value.length() - (value.startsWith("X") ? 3 : 8));
    else
      return Base64.isBase64(value)
          && (facets == null || facets.getMaxLength() == null
          || facets.getMaxLength() * 4 >= value.length() * 3);
  }

  @Override
  public byte[] valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<?> returnType) throws EdmSimpleTypeException {
    if (literalKind == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_KIND_MISSING);

    if (returnType != null && returnType != byte[].class && returnType != Byte[].class)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_TYPE_NOT_SUPPORTED.addContent(returnType));

    if (validate(value, literalKind, facets))
      if (value == null)
        return null;
      else if (literalKind == EdmLiteralKind.URI)
        try {
          return Hex.decodeHex(value.substring(value.startsWith("X") ? 2 : 7, value.length() - 1).toCharArray());
        } catch (DecoderException e) {
          throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value), e);
        }
      else
        return Base64.decodeBase64(value);

    else if (value == null)
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_NULL_NOT_ALLOWED);
    else
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_ILLEGAL_CONTENT.addContent(value));
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (value == null)
      return getNullOrDefaultLiteral(facets);

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
    else if (literalKind == EdmLiteralKind.URI)
      return "binary'" + Hex.encodeHexString(byteArrayValue).toUpperCase(Locale.ROOT) + "'";
    else
      return Base64.encodeBase64String(byteArrayValue);
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return valueToString(valueOfString(literal, EdmLiteralKind.DEFAULT, null, null), EdmLiteralKind.URI, null);
  }
}
