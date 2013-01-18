package com.sap.core.odata.core.edm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * @author SAP AG
 */
public class EdmSimpleTypeFacadeImpl implements EdmSimpleTypeFacade {

  private static final Pattern UINT7_PATTERN = Pattern.compile("\\p{Digit}{1,2}|(?:1(?:0|1)\\p{Digit})|(?:12[0-7])");
  private static final Pattern WHOLE_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+)([lL])?");
  private static final Pattern DECIMAL_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+(?:\\.\\p{Digit}*(?:[eE][-+]?\\p{Digit}+)?)?)([mMdDfF])");
  private static final Pattern STRING_VALUE_PATTERN = Pattern.compile("(X|binary|datetime|datetimeoffset|guid|time)?'(.*)'");

  @Override
  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException {
    if ("true".equals(uriLiteral) || "false".equals(uriLiteral))
      return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Boolean), uriLiteral);

    if ("null".equals(uriLiteral))
      return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Null), uriLiteral);

    if (uriLiteral.length() >= 2)
      if (uriLiteral.startsWith("'") && uriLiteral.endsWith("'"))
        return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.String), uriLiteral.substring(1, uriLiteral.length() - 1).replace("''", "'"));

    // Handle the internal number data types separately to allow the use of EdmSimpleTypeKind below.
    if ("0".equals(uriLiteral) || "1".equals(uriLiteral))
      return new EdmLiteral(getInternalEdmSimpleTypeByString("Bit"), uriLiteral);

    if (UINT7_PATTERN.matcher(uriLiteral).matches())
      return new EdmLiteral(getInternalEdmSimpleTypeByString("Uint7"), uriLiteral);

    String resultValue;
    EdmSimpleTypeKind resultTypeKind = null;

    final Matcher wholeNumberMatcher = WHOLE_NUMBER_PATTERN.matcher(uriLiteral);
    if (wholeNumberMatcher.matches()) {
      resultValue = wholeNumberMatcher.group(1);
      final String suffix = wholeNumberMatcher.group(2);

      try {
        final long l = Long.parseLong(resultValue);
        if ("L".equalsIgnoreCase(suffix))
          resultTypeKind = EdmSimpleTypeKind.Int64;
        else if (l >= Byte.MIN_VALUE && l <= Byte.MAX_VALUE)
          resultTypeKind = EdmSimpleTypeKind.SByte;
        else if (l > Byte.MAX_VALUE && l <= 255)
          resultTypeKind = EdmSimpleTypeKind.Byte;
        else if (l >= Short.MIN_VALUE && l <= Short.MAX_VALUE)
          resultTypeKind = EdmSimpleTypeKind.Int16;
        else if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE)
          resultTypeKind = EdmSimpleTypeKind.Int32;
        else
          throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(uriLiteral));
      } catch (NumberFormatException e) {
        throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(uriLiteral), e);
      }

    } else {
      final Matcher decimalNumberMatcher = DECIMAL_NUMBER_PATTERN.matcher(uriLiteral);
      if (decimalNumberMatcher.matches()) {
        resultValue = decimalNumberMatcher.group(1);
        final String suffix = decimalNumberMatcher.group(2);

        if ("M".equalsIgnoreCase(suffix))
          resultTypeKind = EdmSimpleTypeKind.Decimal;
        else if ("D".equalsIgnoreCase(suffix))
          resultTypeKind = EdmSimpleTypeKind.Double;
        else if ("F".equalsIgnoreCase(suffix))
          resultTypeKind = EdmSimpleTypeKind.Single;

      } else {
        final Matcher stringValueMatcher = STRING_VALUE_PATTERN.matcher(uriLiteral);
        if (stringValueMatcher.matches()) {
          final String prefix = stringValueMatcher.group(1);
          resultValue = stringValueMatcher.group(2);

          if ("X".equals(prefix) || "binary".equals(prefix)) {
            byte[] b;
            try {
              b = Hex.decodeHex(resultValue.toCharArray());
            } catch (DecoderException e) {
              throw new EdmLiteralException(EdmLiteralException.NOTEXT.addContent(e.getClass().getName()), e);
            }
            resultTypeKind = EdmSimpleTypeKind.Binary;
            resultValue = Base64.encodeBase64String(b);
          }
          if ("datetime".equals(prefix))
            resultTypeKind = EdmSimpleTypeKind.DateTime;
          else if ("datetimeoffset".equals(prefix))
            resultTypeKind = EdmSimpleTypeKind.DateTimeOffset;
          else if ("guid".equals(prefix))
            resultTypeKind = EdmSimpleTypeKind.Guid;
          else if ("time".equals(prefix))
            resultTypeKind = EdmSimpleTypeKind.Time;
        } else {
          throw new EdmLiteralException(EdmLiteralException.UNKNOWNLITERAL.addContent(uriLiteral));
        }
      }
    }
    final EdmSimpleType type = getEdmSimpleType(resultTypeKind);
    if (type.validate(resultValue, EdmLiteralKind.DEFAULT, null))
      return new EdmLiteral(type, resultValue);
    else
      throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(uriLiteral));
  }

  public static EdmSimpleType getEdmSimpleType(final EdmSimpleTypeKind typeKind) {

    switch (typeKind) {
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
      throw new ODataRuntimeException("Invalid Type " + typeKind);
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
