package com.sap.core.odata.core.edm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class EdmSimpleTypeFacadeImpl implements EdmSimpleTypeFacade {

  private static final Pattern WHOLE_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+)([lL])?");
  private static final Pattern DECIMAL_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+(?:\\.\\p{Digit}*(?:[eE][-+]?\\p{Digit}+)?)?)([mMdDfF])");
  private static final Pattern STRING_VALUE_PATTERN = Pattern.compile("(X|binary|datetime|datetimeoffset|guid|time)?'(.*)'");
  
  @Override
  public UriLiteral parseUriLiteral(final String uriLiteral) throws UriParserException {
    final String literal = uriLiteral;

    if ("true".equals(literal) || "false".equals(literal))
      return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Boolean), literal);
    
    if ("null".equals(literal))
      return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Null), literal);

    if (literal.length() >= 2)
      if (literal.startsWith("'") && literal.endsWith("'"))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.String), literal.substring(1, literal.length() - 1).replace("''", "'"));

    final Matcher wholeNumberMatcher = WHOLE_NUMBER_PATTERN.matcher(literal);
    if (wholeNumberMatcher.matches()) {
      final String value = wholeNumberMatcher.group(1);
      final String suffix = wholeNumberMatcher.group(2);

      if ("L".equalsIgnoreCase(suffix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int64), value);
      else
        try {
          final int i = Integer.parseInt(value);
          if (i == 0 || i == 1)
            return new UriLiteral(getInternalEdmSimpleTypeByString("Bit"), value);
          else if (i > 1 && i <= Byte.MAX_VALUE)
            return new UriLiteral(getInternalEdmSimpleTypeByString("Uint7"), value);
          else if (i >= Byte.MIN_VALUE && i < 0)
            return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.SByte), value);
          else if (i > Byte.MAX_VALUE && i <= 255)
            return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Byte), value);
          else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE)
            return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int16), value);
          else
            return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int32), value);
        } catch (NumberFormatException e) {
          throw new UriParserException(UriParserException.LITERALFORMAT);
        }
    }

    final Matcher decimalNumberMatcher = DECIMAL_NUMBER_PATTERN.matcher(literal);
    if (decimalNumberMatcher.matches()) {
      final String value = decimalNumberMatcher.group(1);
      final String suffix = decimalNumberMatcher.group(2);

      if ("M".equalsIgnoreCase(suffix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Decimal), value);
      else if ("D".equalsIgnoreCase(suffix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Double), value);
      else if ("F".equalsIgnoreCase(suffix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Single), value);
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
          throw new UriParserException(UriParserException.NOTEXT, e);
        }
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Binary), Base64.encodeBase64String(b));
      }
      if ("datetime".equals(prefix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.DateTime), value);
      else if ("datetimeoffset".equals(prefix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.DateTimeOffset), value);
      else if ("guid".equals(prefix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Guid), value);
      else if ("time".equals(prefix))
        return new UriLiteral(getEdmSimpleType(EdmSimpleTypeKind.Time), value);
    }
    throw new UriParserException(UriParserException.UNKNOWNLITERAL);
  }
  
  public static EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleType){
    EdmSimpleType edmType = null;

    switch (edmSimpleType) {
    case Binary:
      edmType = EdmBinary.getInstance();
      break;
    case Boolean:
      edmType = EdmBoolean.getInstance();
      break;
    case Byte:
      edmType = EdmByte.getInstance();
      break;
    case DateTime:
      edmType = EdmDateTime.getInstance();
      break;
    case DateTimeOffset:
      edmType = EdmDateTimeOffset.getInstance();
      break;
    case Decimal:
      edmType = EdmDecimal.getInstance();
      break;
    case Double:
      edmType = EdmDouble.getInstance();
      break;
    case Guid:
      edmType = EdmGuid.getInstance();
      break;
    case Int16:
      edmType = EdmInt16.getInstance();
      break;
    case Int32:
      edmType = EdmInt32.getInstance();
      break;
    case Int64:
      edmType = EdmInt64.getInstance();
      break;
    case SByte:
      edmType = EdmSByte.getInstance();
      break;
    case Single:
      edmType = EdmSingle.getInstance();
      break;
    case String:
      edmType = EdmString.getInstance();
      break;
    case Time:
      edmType = EdmTime.getInstance();
      break;
    case Null:
      edmType = EdmNull.getInstance();
      break;
    default:
      throw new ODataRuntimeException("Invalid Type " + edmSimpleType);
    }

    return edmType;
  }
  
  public static EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType) {
    EdmSimpleType edmType;

    if ("Bit".equals(edmSimpleType)) {
      edmType = Bit.getInstance();
    } else if ("Uint7".equals(edmSimpleType)) {
      edmType = Uint7.getInstance();
    } else {
      throw new ODataRuntimeException("Invalid internal Type " + edmSimpleType);
    }
    return edmType;
  }
}
