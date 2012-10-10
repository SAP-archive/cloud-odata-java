package org.odata4j.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;

/**
 * A static factory to create immutable {@link OProperty} instances.
 */
public class OProperties {

  private OProperties() {}

  /**
   * Creates a new OData property, inferring the edm-type from the value provided, which cannot be null.
   *
   * @param <T>  the property value's java-type
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static <T> OProperty<T> simple(String name, T value) {
    if (value == null)
      throw new IllegalArgumentException("Cannot infer EdmType if value is null");
    EdmSimpleType<T> type = EdmSimpleType.forJavaType(value.getClass());
    if (type == null)
      throw new IllegalArgumentException("Cannot infer EdmType for java type: " + value.getClass().getName());
    return simple(name, type, value);
  }

  /**
   * Creates a new OData property of the given edm-type.
   *
   * @param <T>  the property value's java-type
   * @param name  the property name
   * @param type  the property edm-type
   * @param value  the property value
   * @return a new OData property instance
   */
  public static <T> OProperty<T> simple(String name, EdmSimpleType<T> type, Object value) {
    OSimpleObject<T> simple = OSimpleObjects.create(type, value);
    return new Impl<T>(name, type, simple.getValue());
  }

  /**
   * Creates a new OData property of the given edm simple type with a null value.
   *
   * @param name  the property name
   * @param type  the property edm simple type
   * @return a new OData property instance
   */
  public static OProperty<?> null_(String name, EdmSimpleType<?> type) {
    return new Impl<Object>(name, type, null);
  }

  /**
  * Creates a new OData property of the given edm simple type with a null value.
  *
  * @param name  the property name
  * @param fqSimpleTypeName  the property edm simple type
  * @return a new OData property instance
  */
  public static OProperty<?> null_(String name, String fqSimpleTypeName) {
    return new Impl<Object>(name, EdmType.getSimple(fqSimpleTypeName), null);
  }

  /**
   * Creates a new complex-valued OData property of the given edm-type.
   *
   * @param name  the property name
   * @param type  the property edm-type
   * @param value  the property values
   * @return a new OData property instance
   */
  public static OProperty<List<OProperty<?>>> complex(String name, EdmComplexType type, List<OProperty<?>> value) {
    return new Impl<List<OProperty<?>>>(name, type, value);
  }

  /**
  * Creates a new collecion-valued OData property of the given edm-type.
  *
  * @param name  the property name
  * @param type  the property edm-type of objects in the collection
  * @param value  the OCollection
  * @return a new OData property instance
  */
  public static OProperty<OCollection<? extends OObject>> collection(String name, EdmCollectionType type, OCollection<? extends OObject> value) {
    return new Impl<OCollection<? extends OObject>>(name, type, value);
  }

  /**
   * Creates a new OData property of the given edm-type with a value parsed from a string.
   *
   * @param name  the property name
   * @param type  the property edm-type
   * @param value  the property value
   * @return a new OData property instance
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static OProperty<?> parseSimple(String name, EdmSimpleType type, String value) {
    if (type == null)
      type = EdmSimpleType.STRING;
    OSimpleObject<?> simple = OSimpleObjects.parse(type, value);
    return new Impl(name, type, simple.getValue());
  }

  /**
   * Creates a new short-valued OData property with {@link EdmSimpleType#INT16}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Short> int16(String name, Short value) {
    return new Impl<Short>(name, EdmSimpleType.INT16, value);
  }

  /**
   * Creates a new integer-valued OData property with {@link EdmSimpleType#INT32}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Integer> int32(String name, Integer value) {
    return new Impl<Integer>(name, EdmSimpleType.INT32, value);
  }

  /**
   * Creates a new long-valued OData property with {@link EdmSimpleType#INT64}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Long> int64(String name, Long value) {
    return new Impl<Long>(name, EdmSimpleType.INT64, value);
  }

  /**
   * Creates a new String-valued OData property with {@link EdmSimpleType#STRING}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<String> string(String name, String value) {
    return new Impl<String>(name, EdmSimpleType.STRING, value);
  }

  /**
   * Creates a new String-valued OData property with {@link EdmSimpleType#STRING}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<String> string(String name, char value) {
    return new Impl<String>(name, EdmSimpleType.STRING, Character.toString(value));
  }

  /**
   * Creates a new String-valued OData property with {@link EdmSimpleType#STRING}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Character> character(String name, Character value) {
    return new Impl<Character>(name, EdmSimpleType.STRING, value);
  }

  /**
   * Creates a new Guid-valued OData property with {@link EdmSimpleType#GUID}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Guid> guid(String name, String value) {
    return guid(name, Guid.fromString(value));
  }

  /**
   * Creates a new Guid-valued OData property with {@link EdmSimpleType#GUID}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Guid> guid(String name, Guid value) {
    return new Impl<Guid>(name, EdmSimpleType.GUID, value);
  }

  /**
   * Creates a new boolean-valued OData property with {@link EdmSimpleType#BOOLEAN}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Boolean> boolean_(String name, Boolean value) {
    return new Impl<Boolean>(name, EdmSimpleType.BOOLEAN, value);
  }

  /**
   * Creates a new single-precision-valued OData property with {@link EdmSimpleType#SINGLE}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Float> single(String name, Float value) {
    return new Impl<Float>(name, EdmSimpleType.SINGLE, value);
  }

  /**
   * Creates a new double-precision-valued OData property with {@link EdmSimpleType#DOUBLE}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Double> double_(String name, Double value) {
    return new Impl<Double>(name, EdmSimpleType.DOUBLE, value);
  }

  /**
   * Creates a new LocalDateTime-valued OData property with {@link EdmSimpleType#DATETIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalDateTime> datetime(String name, LocalDateTime value) {
    return new Impl<LocalDateTime>(name, EdmSimpleType.DATETIME, value);
  }

  /**
   * Creates a new LocalDateTime-valued OData property with {@link EdmSimpleType#DATETIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalDateTime> datetime(String name, Instant value) {
    return new Impl<LocalDateTime>(name, EdmSimpleType.DATETIME, value != null ? new LocalDateTime(value) : null);
  }

  /**
   * Creates a new LocalDateTime-valued OData property with {@link EdmSimpleType#DATETIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalDateTime> datetime(String name, Date value) {
    return new Impl<LocalDateTime>(name, EdmSimpleType.DATETIME, value != null ? LocalDateTime.fromDateFields(value) : null);
  }

  /**
   * Creates a new LocalDateTime-valued OData property with {@link EdmSimpleType#DATETIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalDateTime> datetime(String name, Calendar value) {
    return new Impl<LocalDateTime>(name, EdmSimpleType.DATETIME, value != null ? LocalDateTime.fromCalendarFields(value) : null);
  }

  /**
   * Creates a new LocalDateTime-valued OData property with {@link EdmSimpleType#DATETIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalDateTime> datetime(String name, Timestamp value) {
    return new Impl<LocalDateTime>(name, EdmSimpleType.DATETIME, value != null ? new LocalDateTime(value) : null);
  }

  /**
   * Creates a new LocalDateTime-valued OData property with {@link EdmSimpleType#DATETIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalDateTime> datetime(String name, java.sql.Date value) {
    return new Impl<LocalDateTime>(name, EdmSimpleType.DATETIME, value != null ? new LocalDateTime(value) : null);
  }

  /**
   * Creates a new LocalDateTime-valued OData property with {@link EdmSimpleType#DATETIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalDateTime> datetime(String name, Time value) {
    return new Impl<LocalDateTime>(name, EdmSimpleType.DATETIME, value != null ? new LocalDateTime(value) : null);
  }

  /**
   * Creates a new DateTime-valued OData property with {@link EdmSimpleType#DATETIMEOFFSET}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<DateTime> datetimeOffset(String name, DateTime value) {
    return new Impl<DateTime>(name, EdmSimpleType.DATETIMEOFFSET, value);
  }

  /**
   * Creates a new LocalTime-valued OData property with {@link EdmSimpleType#TIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalTime> time(String name, LocalTime value) {
    return new Impl<LocalTime>(name, EdmSimpleType.TIME, value);
  }

  /**
   * Creates a new LocalTime-valued OData property with {@link EdmSimpleType#TIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalTime> time(String name, Date value) {
    return new Impl<LocalTime>(name, EdmSimpleType.TIME, value != null ? LocalTime.fromDateFields(value) : null);
  }

  /**
   * Creates a new LocalTime-valued OData property with {@link EdmSimpleType#TIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalTime> time(String name, Calendar value) {
    return new Impl<LocalTime>(name, EdmSimpleType.TIME, value != null ? LocalTime.fromCalendarFields(value) : null);
  }

  /**
   * Creates a new LocalTime-valued OData property with {@link EdmSimpleType#TIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalTime> time(String name, Timestamp value) {
    return new Impl<LocalTime>(name, EdmSimpleType.TIME, value != null ? new LocalTime(value) : null);
  }

  /**
   * Creates a new LocalTime-valued OData property with {@link EdmSimpleType#TIME}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<LocalTime> time(String name, Time value) {
    return new Impl<LocalTime>(name, EdmSimpleType.TIME, value != null ? new LocalTime(value) : null);
  }

  /**
   * Creates a new BigDecimal-valued OData property with {@link EdmSimpleType#DECIMAL}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<BigDecimal> decimal(String name, BigDecimal value) {
    return new Impl<BigDecimal>(name, EdmSimpleType.DECIMAL, value);
  }

  /**
   * Creates a new BigDecimal-valued OData property with {@link EdmSimpleType#DECIMAL}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<BigDecimal> decimal(String name, BigInteger value) {
    return new Impl<BigDecimal>(name, EdmSimpleType.DECIMAL, value != null ? BigDecimal.valueOf(value.longValue()) : null);
  }

  /**
   * Creates a new BigDecimal-valued OData property with {@link EdmSimpleType#DECIMAL}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<BigDecimal> decimal(String name, long value) {
    return new Impl<BigDecimal>(name, EdmSimpleType.DECIMAL, BigDecimal.valueOf(value));
  }

  /**
   * Creates a new BigDecimal-valued OData property with {@link EdmSimpleType#DECIMAL}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<BigDecimal> decimal(String name, double value) {
    return new Impl<BigDecimal>(name, EdmSimpleType.DECIMAL, BigDecimal.valueOf(value));
  }

  /**
   * Creates a new byte-array-valued OData property with {@link EdmSimpleType#BINARY}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<byte[]> binary(String name, byte[] value) {
    return new Impl<byte[]>(name, EdmSimpleType.BINARY, value);
  }

  /**
   * Creates a new byte-array-valued OData property with {@link EdmSimpleType#BINARY}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Byte[]> binary(String name, Byte[] value) {
    return new Impl<Byte[]>(name, EdmSimpleType.BINARY, value);
  }

  /**
   * Creates a new unsigned-byte-valued OData property with {@link EdmSimpleType#BYTE}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<UnsignedByte> byte_(String name, UnsignedByte value) {
    return new Impl<UnsignedByte>(name, EdmSimpleType.BYTE, value);
  }

  /**
   * Creates a new byte-valued OData property with {@link EdmSimpleType#SBYTE}
   *
   * @param name  the property name
   * @param value  the property value
   * @return a new OData property instance
   */
  public static OProperty<Byte> sbyte_(String name, byte value) {
    return new Impl<Byte>(name, EdmSimpleType.SBYTE, value);
  }

  private static class Impl<T> implements OProperty<T> {

    private final String name;
    private final EdmType type;
    private final T value;

    Impl(String name, EdmType type, T value) {
      this.name = name;
      this.type = type;
      this.value = value;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public EdmType getType() {
      return type;
    }

    @Override
    public T getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.format("OProperty[%s,%s,%s]", name, getType(), OSimpleObjects.getValueDisplayString(value));
    }
  }

}
