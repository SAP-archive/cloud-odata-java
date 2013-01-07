package com.sap.core.odata.core.edm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * @author SAP AG
 */
public class EdmSimpleTypeFacadeImpl implements EdmSimpleTypeFacade {

  private static final Pattern WHOLE_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+)([lL])?");
  private static final Pattern DECIMAL_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+(?:\\.\\p{Digit}*(?:[eE][-+]?\\p{Digit}+)?)?)([mMdDfF])");
  private static final Pattern STRING_VALUE_PATTERN = Pattern.compile("(X|binary|datetime|datetimeoffset|guid|time)?'(.*)'");

  @Override
  /*TODO check
   * It looks like "datetime'345345345'" <- obviously wrong#
   * is parsed to EdmLiteral( EdmDateTime instance, "datetime'345345345'")
   * But its not verified if datetime'345345345' is really a VALID datetime literal
   */
  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException {
    final String literal = uriLiteral;

    if ("true".equals(literal) || "false".equals(literal))
      return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Boolean), literal);

    if ("null".equals(literal))
      return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Null), literal);

    if (literal.length() >= 2)
      if (literal.startsWith("'") && literal.endsWith("'"))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.String), literal.substring(1, literal.length() - 1).replace("''", "'"));

    final Matcher wholeNumberMatcher = WHOLE_NUMBER_PATTERN.matcher(literal);
    if (wholeNumberMatcher.matches()) {
      final String value = wholeNumberMatcher.group(1);
      final String suffix = wholeNumberMatcher.group(2);

      if ("L".equalsIgnoreCase(suffix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int64), value);
      else
        try {
          final int i = Integer.parseInt(value);
          if (i == 0 || i == 1)
            return new EdmLiteral(getInternalEdmSimpleTypeByString("Bit"), value);
          else if (i > 1 && i <= Byte.MAX_VALUE)
            return new EdmLiteral(getInternalEdmSimpleTypeByString("Uint7"), value);
          else if (i >= Byte.MIN_VALUE && i < 0)
            return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.SByte), value);
          else if (i > Byte.MAX_VALUE && i <= 255)
            return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Byte), value);
          else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE)
            return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int16), value);
          else
            return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int32), value);
        } catch (NumberFormatException e) {
          throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(literal), e);
        }
    }

    final Matcher decimalNumberMatcher = DECIMAL_NUMBER_PATTERN.matcher(literal);
    if (decimalNumberMatcher.matches()) {
      final String value = decimalNumberMatcher.group(1);
      final String suffix = decimalNumberMatcher.group(2);

      if ("M".equalsIgnoreCase(suffix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Decimal), value);
      else if ("D".equalsIgnoreCase(suffix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Double), value);
      else if ("F".equalsIgnoreCase(suffix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Single), value);
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
          throw new EdmLiteralException(EdmLiteralException.NOTEXT.addContent(e.getClass().getName()), e);
        }
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Binary), Base64.encodeBase64String(b));
      }
      if ("datetime".equals(prefix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.DateTime), value);
      else if ("datetimeoffset".equals(prefix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.DateTimeOffset), value);
      else if ("guid".equals(prefix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Guid), value);
      else if ("time".equals(prefix))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Time), value);
    }
    throw new EdmLiteralException(EdmLiteralException.UNKNOWNLITERAL.addContent(literal));
  }

  public static EdmSimpleType getEdmSimpleType(final EdmSimpleTypeKind edmSimpleType) {

    switch (edmSimpleType) {
    case Binary:
      return EdmBinary.getInstance();
    case Boolean:
      return EdmBoolean.getInstance();
    case Byte:
      return EdmByte.getInstance();
    case DateTime:
      return EdmDateTime.getInstance();
    case DateTimeOffset:
      return EdmDateTimeOffset.getInstance();
    case Decimal:
      return EdmDecimal.getInstance();
    case Double:
      return EdmDouble.getInstance();
    case Guid:
      return EdmGuid.getInstance();
    case Int16:
      return EdmInt16.getInstance();
    case Int32:
      return EdmInt32.getInstance();
    case Int64:
      return EdmInt64.getInstance();
    case SByte:
      return EdmSByte.getInstance();
    case Single:
      return EdmSingle.getInstance();
    case String:
      return EdmString.getInstance();
    case Time:
      return EdmTime.getInstance();
    case Null:
      return EdmNull.getInstance();
    default:
      throw new ODataRuntimeException("Invalid Type " + edmSimpleType);
    }
  }

  public static EdmSimpleType getInternalEdmSimpleTypeByString(final String edmSimpleType) {
    if ("Bit".equals(edmSimpleType))
      return Bit.getInstance();
    else if ("Uint7".equals(edmSimpleType))
      return Uint7.getInstance();
    else
      throw new ODataRuntimeException("Invalid internal Type " + edmSimpleType);
  }
}
