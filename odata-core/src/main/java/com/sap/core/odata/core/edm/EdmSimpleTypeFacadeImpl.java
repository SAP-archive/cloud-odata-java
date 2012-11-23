package com.sap.core.odata.core.edm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;

public class EdmSimpleTypeFacadeImpl extends EdmSimpleTypeFacade {

  private static final Pattern WHOLE_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+)([lL])?");
  private static final Pattern DECIMAL_NUMBER_PATTERN = Pattern.compile("(-?\\p{Digit}+(?:\\.\\p{Digit}*(?:[eE][-+]?\\p{Digit}+)?)?)([mMdDfF])");
  private static final Pattern STRING_VALUE_PATTERN = Pattern.compile("(X|binary|datetime|datetimeoffset|guid|time)?'(.*)'");
  
  private EdmSimpleType getInternalTypeKindInstance(String edmSimpleType){
    return RuntimeDelegate.getInternalEdmSimpleTypeByString(edmSimpleType);
  }
  
  @Override
  public UriLiteral parse(final String uriLiteral) throws UriParserException {
    final String literal = uriLiteral;

    if ("true".equals(literal) || "false".equals(literal))
      return new UriLiteral(booleanInstance(), literal);
    
    if ("null".equals(literal))
      return new UriLiteral(nullInstance(), literal);

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
          if (i == 0 || i == 1)
            return new UriLiteral(getInternalTypeKindInstance("Bit"), value);
          else if (i > 1 && i <= Byte.MAX_VALUE)
            return new UriLiteral(getInternalTypeKindInstance("Uint7"), value);
          else if (i >= Byte.MIN_VALUE && i < 0)
            return new UriLiteral(sByteInstance(), value);
          else if (i > Byte.MAX_VALUE && i <= 255)
            return new UriLiteral(byteInstance(), value);
          else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE)
            return new UriLiteral(int16Instance(), value);
          else
            return new UriLiteral(int32Instance(), value);
        } catch (NumberFormatException e) {
          throw new UriParserException(UriParserException.LITERALFORMAT);
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
          throw new UriParserException(UriParserException.NOTEXT, e);
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
    throw new UriParserException(UriParserException.UNKNOWNLITERAL);
  }
}
