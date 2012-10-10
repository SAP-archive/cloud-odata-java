package org.odata4j.format.json;

import java.math.BigDecimal;

import org.odata4j.core.Guid;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.core.UnsignedByte;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonParseException;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonStreamTokenizer.JsonTokenType;
import org.odata4j.internal.InternalUtil;
import org.odata4j.repack.org.apache.commons.codec.binary.Base64;

public class JsonTypeConverter {

  public static OProperty<?> parse(String name, EdmSimpleType<?> type, String value, JsonTokenType tokenType) {

    if (type == null)
      return OProperties.string(name, value);
    else if (value == null)
      return OProperties.null_(name, type);
    else if (EdmSimpleType.STRING.equals(type))
      if (tokenType == JsonTokenType.STRING)
        return OProperties.string(name, value);
      else
        throw new JsonParseException("Illegal JSON string-value format");
    else if (EdmSimpleType.GUID.equals(type))
      return OProperties.guid(name, Guid.fromString(value));
    else if (EdmSimpleType.BOOLEAN.equals(type))
      if (tokenType == JsonTokenType.FALSE || tokenType == JsonTokenType.TRUE)
        return OProperties.boolean_(name, Boolean.valueOf(value));
      else
        throw new JsonParseException("Illegal JSON boolean-value format");
    else if (EdmSimpleType.BYTE.equals(type))
      return OProperties.byte_(name, UnsignedByte.parseUnsignedByte(value));
    else if (EdmSimpleType.SBYTE.equals(type))
      return OProperties.sbyte_(name, Byte.parseByte(value));
    else if (EdmSimpleType.INT16.equals(type))
      return OProperties.int16(name, Short.parseShort(value));
    else if (EdmSimpleType.INT32.equals(type))
      return OProperties.int32(name, Integer.parseInt(value));
    else if (EdmSimpleType.INT64.equals(type))
      return OProperties.int64(name, Long.parseLong(value));
    else if (EdmSimpleType.SINGLE.equals(type))
      return OProperties.single(name, Float.parseFloat(value));
    else if (EdmSimpleType.DOUBLE.equals(type))
      return OProperties.double_(name, Double.parseDouble(value));
    else if (EdmSimpleType.DECIMAL.equals(type))
      return OProperties.decimal(name, new BigDecimal(value));
    else if (EdmSimpleType.BINARY.equals(type))
      return OProperties.binary(name, new Base64().decode(value));
    else if (EdmSimpleType.DATETIME.equals(type))
      return OProperties.datetime(name, InternalUtil.parseDateTimeFromJson(value));
    else if (EdmSimpleType.DATETIMEOFFSET.equals(type))
      return OProperties.datetimeOffset(name, InternalUtil.parseDateTimeOffsetFromJson(value));
    else if (EdmSimpleType.TIME.equals(type))
      return OProperties.time(name, InternalUtil.parseTime(value));

    throw new UnsupportedOperationException("type:" + type);
  }

}
