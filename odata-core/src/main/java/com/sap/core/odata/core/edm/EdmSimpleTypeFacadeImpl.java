/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * @author SAP AG
 */
public class EdmSimpleTypeFacadeImpl implements EdmSimpleTypeFacade {

  @Override
  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException {
    if (uriLiteral == null || "null".equals(uriLiteral)) {
      return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Null), uriLiteral);
    }

    if ("true".equals(uriLiteral) || "false".equals(uriLiteral)) {
      return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Boolean), uriLiteral);
    }

    if (uriLiteral.length() >= 2
        && uriLiteral.startsWith("'") && uriLiteral.endsWith("'")) {
      try {
        final EdmSimpleType type = getEdmSimpleType(EdmSimpleTypeKind.String);
        return new EdmLiteral(type, type.valueOfString(uriLiteral, EdmLiteralKind.URI, null, String.class));
      } catch (EdmSimpleTypeException e) {
        throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(uriLiteral), e);
      }
    }

    if (uriLiteral.matches("-?\\p{Digit}+")) {
      try {
        final int i = getEdmSimpleType(EdmSimpleTypeKind.Int32).valueOfString(uriLiteral, EdmLiteralKind.URI, null, Integer.class);
        if (i == 0 || i == 1) {
          return new EdmLiteral(getInternalEdmSimpleTypeByString("Bit"), uriLiteral);
        }
        if (i >= 0 && i <= Byte.MAX_VALUE) {
          return new EdmLiteral(getInternalEdmSimpleTypeByString("Uint7"), uriLiteral);
        }
        if (i >= Byte.MIN_VALUE && i < 0) {
          return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.SByte), uriLiteral);
        } else if (i > Byte.MAX_VALUE && i <= 255) {
          return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Byte), uriLiteral);
        } else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
          return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int16), uriLiteral);
        } else {
          return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Int32), uriLiteral);
        }
      } catch (EdmSimpleTypeException e) {
        throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(uriLiteral), e);
      }
    }

    if (uriLiteral.endsWith("L") || uriLiteral.endsWith("l")) {
      return createEdmLiteral(EdmSimpleTypeKind.Int64, uriLiteral, 0, 1);
    }
    if (uriLiteral.endsWith("M") || uriLiteral.endsWith("m")) {
      return createEdmLiteral(EdmSimpleTypeKind.Decimal, uriLiteral, 0, 1);
    }
    if (uriLiteral.endsWith("D") || uriLiteral.endsWith("d")) {
      return createEdmLiteral(EdmSimpleTypeKind.Double, uriLiteral, 0, 1);
    }
    if (uriLiteral.equals("-INF") || uriLiteral.equals("INF") || uriLiteral.equals("NaN")) {
      return new EdmLiteral(getEdmSimpleType(EdmSimpleTypeKind.Single), uriLiteral);
    }
    if (uriLiteral.endsWith("F") || uriLiteral.endsWith("f")) {
      return createEdmLiteral(EdmSimpleTypeKind.Single, uriLiteral, 0, 1);
    }

    if (uriLiteral.startsWith("datetime'")) {
      return createEdmLiteral(EdmSimpleTypeKind.DateTime, uriLiteral, 9, 1);
    }
    if (uriLiteral.startsWith("datetimeoffset'")) {
      return createEdmLiteral(EdmSimpleTypeKind.DateTimeOffset, uriLiteral, 15, 1);
    }
    if (uriLiteral.startsWith("guid'")) {
      return createEdmLiteral(EdmSimpleTypeKind.Guid, uriLiteral, 5, 1);
    }
    if (uriLiteral.startsWith("time'")) {
      return createEdmLiteral(EdmSimpleTypeKind.Time, uriLiteral, 5, 1);
    }

    if (uriLiteral.startsWith("X'") || uriLiteral.startsWith("binary'")) {
      try {
        final EdmSimpleType type = getEdmSimpleType(EdmSimpleTypeKind.Binary);
        final byte[] value = type.valueOfString(uriLiteral, EdmLiteralKind.URI, null, byte[].class);
        return new EdmLiteral(type, type.valueToString(value, EdmLiteralKind.DEFAULT, null));
      } catch (EdmSimpleTypeException e) {
        throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(uriLiteral), e);
      }
    }

    throw new EdmLiteralException(EdmLiteralException.UNKNOWNLITERAL.addContent(uriLiteral));
  }

  private static EdmLiteral createEdmLiteral(final EdmSimpleTypeKind typeKind, final String literal, final int prefixLength, final int suffixLength) throws EdmLiteralException {
    final EdmSimpleType type = getEdmSimpleType(typeKind);
    if (type.validate(literal, EdmLiteralKind.URI, null)) {
      return new EdmLiteral(type, literal.substring(prefixLength, literal.length() - suffixLength));
    } else {
      throw new EdmLiteralException(EdmLiteralException.LITERALFORMAT.addContent(literal));
    }
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
    if ("Bit".equals(edmSimpleType)) {
      return Bit.getInstance();
    } else if ("Uint7".equals(edmSimpleType)) {
      return Uint7.getInstance();
    } else {
      throw new ODataRuntimeException("Invalid internal Type " + edmSimpleType);
    }
  }
}
