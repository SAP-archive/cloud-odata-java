package org.odata4j.core;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.internal.InternalUtil;
import org.odata4j.internal.TypeConverter;
import org.odata4j.repack.org.apache.commons.codec.binary.Base64;
import org.odata4j.repack.org.apache.commons.codec.binary.Hex;

/**
 * A static factory to create immutable {@link OSimpleObject} instances.
 */
public class OSimpleObjects {

  private OSimpleObjects() {}

  /** Creates a new {@link OSimpleObject} instance given a type and value. */
  @SuppressWarnings("unchecked")
  public static <T> OSimpleObject<T> create(EdmSimpleType<T> type, Object value) {
    if (type == EdmSimpleType.STRING) {
      String sValue = null;
      if (value != null) {
        if (value instanceof Character) {
          sValue = value.toString();
        } else {
          sValue = (String) value;
        }
      }
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.STRING, sValue);
    } else if (type == EdmSimpleType.BOOLEAN) {
      Boolean bValue = (Boolean) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.BOOLEAN, bValue);
    } else if (type == EdmSimpleType.INT16) {
      Short sValue = (Short) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.INT16, sValue);
    } else if (type == EdmSimpleType.INT32) {
      Integer iValue = (Integer) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.INT32, iValue);
    } else if (type == EdmSimpleType.INT64) {
      Long iValue = (Long) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.INT64, iValue);
    } else if (type == EdmSimpleType.BYTE) {
      UnsignedByte bValue = (UnsignedByte) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.BYTE, bValue);
    } else if (type == EdmSimpleType.SBYTE) {
      Byte bValue = (Byte) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.SBYTE, bValue);
    } else if (type == EdmSimpleType.DECIMAL) {
      BigDecimal dValue = (BigDecimal) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.DECIMAL, dValue);
    } else if (type == EdmSimpleType.DATETIME) {
      LocalDateTime ldtValue = TypeConverter.convert(value, LocalDateTime.class);
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.DATETIME, ldtValue);
    } else if (type == EdmSimpleType.TIME) {
      LocalTime ltValue = TypeConverter.convert(value, LocalTime.class);
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.TIME, ltValue);
    } else if (type == EdmSimpleType.BINARY) {
      byte[] bValue = (byte[]) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.BINARY, bValue);
    } else if (type == EdmSimpleType.DOUBLE) {
      Double dValue = (Double) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.DOUBLE, dValue);
    } else if (type == EdmSimpleType.SINGLE) {
      Float fValue = (Float) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.SINGLE, fValue);
    } else if (type == EdmSimpleType.GUID) {
      Guid gValue = TypeConverter.convert(value, Guid.class);
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.GUID, gValue);
    } else if (type == EdmSimpleType.DATETIMEOFFSET) {
      DateTime dtValue = (DateTime) value;
      return (OSimpleObject<T>) Impl.create(EdmSimpleType.DATETIMEOFFSET, dtValue);
    } else {
      throw new UnsupportedOperationException("Implement " + type);
    }
  }

  /** Parses a string value into a new {@link OSimpleObject} given an edm type. */
  @SuppressWarnings("unchecked")
  public static <V> OSimpleObject<V> parse(EdmSimpleType<V> type, String value) {
    if (value == null)
      return (OSimpleObject<V>) Impl.create(type, null);

    if (EdmSimpleType.GUID.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.GUID, Guid.fromString(value));
    if (EdmSimpleType.BOOLEAN.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.BOOLEAN, Boole.fromString(value).toBoolean());
    if (EdmSimpleType.BYTE.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.BYTE, UnsignedByte.parseUnsignedByte(value));
    if (EdmSimpleType.SBYTE.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.SBYTE, Byte.parseByte(value));
    if (EdmSimpleType.INT16.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.INT16, Short.parseShort(value));
    if (EdmSimpleType.INT32.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.INT32, Integer.parseInt(value));
    if (EdmSimpleType.INT64.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.INT64, Long.parseLong(value));
    if (EdmSimpleType.SINGLE.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.SINGLE, Float.parseFloat(value));
    if (EdmSimpleType.DOUBLE.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.DOUBLE, Double.parseDouble(value));
    if (EdmSimpleType.DECIMAL.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.DECIMAL, new BigDecimal(value));
    if (EdmSimpleType.BINARY.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.BINARY, new Base64().decode(value));
    if (EdmSimpleType.DATETIME.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.DATETIME, InternalUtil.parseDateTimeFromXml(value));
    if (EdmSimpleType.DATETIMEOFFSET.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.DATETIMEOFFSET, InternalUtil.parseDateTimeOffsetFromXml(value));
    if (EdmSimpleType.TIME.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.TIME, InternalUtil.parseTime(value));
    if (EdmSimpleType.STRING.equals(type))
      return (OSimpleObject<V>) Impl.create(EdmSimpleType.STRING, value);

    throw new UnsupportedOperationException("type:" + type);
  }

  /** Returns a human-readable string value for a given object. */
  public static String getValueDisplayString(Object value) {
    if (value instanceof byte[])
      value = "0x" + Hex.encodeHexString((byte[]) value);
    return value == null ? "null" : value.toString();
  }

  private static class Impl<V> implements OSimpleObject<V> {

    private final EdmSimpleType<V> type;
    private final V value;

    private Impl(EdmSimpleType<V> type, V value) {
      this.type = type;
      this.value = value;
    }

    private static <V> Impl<V> create(EdmSimpleType<V> type, V value) {
      return new Impl<V>(type, value);
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public EdmSimpleType<V> getType() {
      return type;
    }

    @Override
    public String toString() {
      return getValueDisplayString(value);
    }

  }

}
