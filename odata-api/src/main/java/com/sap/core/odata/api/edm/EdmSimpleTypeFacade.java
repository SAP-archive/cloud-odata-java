package com.sap.core.odata.api.edm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.sap.core.odata.api.rest.RuntimeDelegate;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;

public class EdmSimpleTypeFacade {

  public static final String edmNamespace = "Edm";
  public static final String systemNamespace = "System";
  private static final Pattern WHOLE_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+)([lL])?");
  private static final Pattern DECIMAL_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+(?:\\.\\p{Digit}*(?:[eE][-+]?\\p{Digit}+)?)?)([mMdDfF])");
  private static final Pattern STRING_VALUE_PATTERN = Pattern.compile("(X|binary|datetime|datetimeoffset|guid|time)?'(.*)'");

  public enum EdmSimpleTypes
  {
    BINARY("Binary"), BOOLEAN("Boolean"), BYTE("Byte"), DATETIME("DateTime"), DATETIMEOFFSET("DateTimeOffset"), DECIMAL("Decimal"), DOUBLE("Double"), GUID("Guid"), INT16("Int16"), INT32("Int32"), INT64("Int64"), NULL("Null"), SBYTE("SByte"), SINGLE("Single"), STRING("String"), TIME("Time"), BIT("Bit"), UINT7("UInt7");

    private String name;

    private EdmSimpleTypes(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  public EdmSimpleType getInstance(EdmSimpleTypes edmSimpleType) {
    return RuntimeDelegate.getInstance().getEdmSimpleType(edmSimpleType);
  }

  public EdmSimpleType binaryInstance() {
    return getInstance(EdmSimpleTypes.BINARY);
  }

  public EdmSimpleType booleanInstance() {
    return getInstance(EdmSimpleTypes.BOOLEAN);
  }

  public EdmSimpleType byteInstance() {
    return getInstance(EdmSimpleTypes.BYTE);
  }

  public EdmSimpleType dateTimeInstance() {
    return getInstance(EdmSimpleTypes.DATETIME);
  }

  public EdmSimpleType dateTimeOffsetInstance() {
    return getInstance(EdmSimpleTypes.DATETIMEOFFSET);
  }

  public EdmSimpleType decimalInstance() {
    return getInstance(EdmSimpleTypes.DECIMAL);
  }

  public EdmSimpleType doubleInstance() {
    return getInstance(EdmSimpleTypes.DOUBLE);
  }

  public EdmSimpleType guidInstance() {
    return getInstance(EdmSimpleTypes.GUID);
  }

  public EdmSimpleType int16Instance() {
    return getInstance(EdmSimpleTypes.INT16);
  }

  public EdmSimpleType int32Instance() {
    return getInstance(EdmSimpleTypes.INT32);
  }

  public EdmSimpleType int64Instance() {
    return getInstance(EdmSimpleTypes.INT64);
  }

  public EdmSimpleType nullInstance() {
    return getInstance(EdmSimpleTypes.NULL);
  }

  public EdmSimpleType sByteInstance() {
    return getInstance(EdmSimpleTypes.SBYTE);
  }

  public EdmSimpleType singleInstance() {
    return getInstance(EdmSimpleTypes.SINGLE);
  }

  public EdmSimpleType stringInstance() {
    return getInstance(EdmSimpleTypes.STRING);
  }

  public EdmSimpleType timeInstance() {
    return getInstance(EdmSimpleTypes.TIME);
  }

  public UriLiteral parseUriLiteral(String uriLiteral) throws UriParserException {
    String literal = uriLiteral;

    if ("true".equals(literal) || "false".equals(literal))
      return new UriLiteral(booleanInstance(), literal);

    if (literal.length() >= 2)
      if (literal.startsWith("'") && literal.endsWith("'"))
        return new UriLiteral(stringInstance(), literal.substring(1, literal.length() - 1).replace("''", "'"));

    final Matcher wholeNumberMatcher = WHOLE_NUMBER_PATTERN.matcher(literal);
    if (wholeNumberMatcher.matches()) {
      final String value = wholeNumberMatcher.group(1);
      final String suffix = wholeNumberMatcher.group(2);

      if ("L".equalsIgnoreCase(suffix))
        return new UriLiteral(int64Instance(), value);
      else
        try {
          final int i = Integer.parseInt(value);
          if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
            if (i == 0 || i == 1) {
              return new UriLiteral(getInstance(EdmSimpleTypes.BIT), value);
            } else if (i >= 0 && i <= 127) {
              return new UriLiteral(getInstance(EdmSimpleTypes.UINT7), value);
            } else {
              return new UriLiteral(sByteInstance(), value);
            }
          } else if (i > Byte.MAX_VALUE && i <= 255)
            return new UriLiteral(byteInstance(), value);
          else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE)
            return new UriLiteral(int16Instance(), value);
          else
            return new UriLiteral(int32Instance(), value);
        } catch (NumberFormatException e) {
          throw new UriParserException("Wrong format for literal value: " + uriLiteral, e);
        }
    }

    final Matcher decimalNumberMatcher = DECIMAL_NUMBER_PATTERN.matcher(literal);
    if (decimalNumberMatcher.matches()) {
      final String value = decimalNumberMatcher.group(1);
      final String suffix = decimalNumberMatcher.group(2);

      if ("M".equalsIgnoreCase(suffix))
        return new UriLiteral(decimalInstance(), value);
      else if ("D".equalsIgnoreCase(suffix))
        return new UriLiteral(doubleInstance(), value);
      else if ("F".equalsIgnoreCase(suffix))
        return new UriLiteral(singleInstance(), value);
    }

    final Matcher stringValueMatcher = STRING_VALUE_PATTERN.matcher(literal);
    if (stringValueMatcher.matches()) {
      final String prefix = stringValueMatcher.group(1);
      final String value = stringValueMatcher.group(2);

      if ("X".equals(prefix) || "binary".equals(prefix)) {
        byte[] b;
        try {
          b = Hex.decodeHex(value.toCharArray());
        } catch (DecoderException e) {
          throw new UriParserException(e);
        }
        return new UriLiteral(binaryInstance(), Base64.encodeBase64String(b));
      }
      if ("datetime".equals(prefix))
        return new UriLiteral(dateTimeInstance(), value);
      else if ("datetimeoffset".equals(prefix))
        return new UriLiteral(dateTimeOffsetInstance(), value);
      else if ("guid".equals(prefix))
        return new UriLiteral(guidInstance(), value);
      else if ("time".equals(prefix))
        return new UriLiteral(timeInstance(), value);
    }
    throw new UriParserException("Unknown uriLiteral: " + uriLiteral);
  }

}