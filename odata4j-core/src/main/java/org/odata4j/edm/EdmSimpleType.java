package org.odata4j.edm;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.core4j.Enumerable;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.core.Guid;
import org.odata4j.core.UnsignedByte;

/**
 * Primitive types in the EDM type system.
 * Simple types are exposed as constants and associated with one or more java-types.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399213.aspx">[msdn] Simple Types (EDM)</a>
 */
public class EdmSimpleType<V> extends EdmType {

  private static Set<EdmSimpleType<?>> all = new HashSet<EdmSimpleType<?>>();

  // http://msdn.microsoft.com/en-us/library/bb399213.aspx
  public static final EdmSimpleType<byte[]> BINARY = newSimple("Edm.Binary", byte[].class, Byte[].class);
  public static final EdmSimpleType<Boolean> BOOLEAN = newSimple("Edm.Boolean", Boolean.class, boolean.class);
  public static final EdmSimpleType<UnsignedByte> BYTE = newSimple("Edm.Byte", UnsignedByte.class);
  public static final EdmSimpleType<LocalDateTime> DATETIME = newSimple("Edm.DateTime", LocalDateTime.class, Instant.class, Date.class, Calendar.class, Timestamp.class, java.sql.Date.class); // possible, but not default: Time.class
  public static final EdmSimpleType<DateTime> DATETIMEOFFSET = newSimple("Edm.DateTimeOffset", DateTime.class);
  public static final EdmSimpleType<BigDecimal> DECIMAL = newSimple("Edm.Decimal", BigDecimal.class);
  public static final EdmSimpleType<Double> DOUBLE = newSimple("Edm.Double", Double.class, double.class);
  public static final EdmSimpleType<Guid> GUID = newSimple("Edm.Guid", Guid.class, UUID.class);
  public static final EdmSimpleType<Short> INT16 = newSimple("Edm.Int16", Short.class, short.class);
  public static final EdmSimpleType<Integer> INT32 = newSimple("Edm.Int32", Integer.class, int.class);
  public static final EdmSimpleType<Long> INT64 = newSimple("Edm.Int64", Long.class, long.class);
  public static final EdmSimpleType<Byte> SBYTE = newSimple("Edm.SByte", Byte.class, byte.class);
  public static final EdmSimpleType<Float> SINGLE = newSimple("Edm.Single", Float.class, float.class);
  public static final EdmSimpleType<String> STRING = newSimple("Edm.String", String.class, char.class, Character.class);
  public static final EdmSimpleType<LocalTime> TIME = newSimple("Edm.Time", LocalTime.class, Time.class); // possible, but not default: Date.class, Calendar.class, Timestamp.class

  private static <V> EdmSimpleType<V> newSimple(String typeString, Class<V> canonicalJavaType, Class<?>... alternateJavaTypes) {
    EdmSimpleType<V> rt = new EdmSimpleType<V>(typeString, canonicalJavaType, alternateJavaTypes);
    all.add(rt);
    return rt;
  }

  /**
   * Immutable set of all edm simple types.
   */
  public static final Set<EdmSimpleType<?>> ALL = Collections.unmodifiableSet(all);

  private final Class<V> canonicalJavaType;
  private final Set<Class<?>> javaTypes;

  private EdmSimpleType(String fullyQualifiedTypeName, Class<V> canonicalJavaType, Class<?>... alternateJavaTypes) {
    super(fullyQualifiedTypeName);
    this.canonicalJavaType = canonicalJavaType;
    this.javaTypes = Enumerable.<Class<?>> create(canonicalJavaType).concat(alternateJavaTypes).toSet();
  }

  @Override
  public boolean isSimple() {
    return true;
  }

  public Class<V> getCanonicalJavaType() {
    return canonicalJavaType;
  }

  /**
   * Gets all java-types associated with this edm-type.
   *
   * @return the associated java-types.
   */
  public Set<Class<?>> getJavaTypes() {
    return javaTypes;
  }

  /**
   * Finds the edm simple type for a given java-type.
   *
   * @param javaType  the java-type
   * @return the associated edm simple type, else null
   */
  @SuppressWarnings("unchecked")
  public static <V> EdmSimpleType<V> forJavaType(Class<?> javaType) {
    for (EdmSimpleType<?> simple : ALL)
      if (simple.getJavaTypes().contains(javaType))
        return (EdmSimpleType<V>) simple;
    return null;
  }

  public static EdmType.Builder<?, ?> newBuilder(EdmType type) {
    return new Builder(type);
  }

  private static class Builder extends EdmType.Builder<EdmType, Builder> {

    private Builder(EdmType type) {
      super(type);
    }

    @Override
    Builder newBuilder(EdmType type, BuilderContext context) {
      return new Builder(type);
    }

    @Override
    public EdmType build() {
      return (EdmType) _build();
    }

    @Override
    protected EdmType buildImpl() {
      // should never get here
      return null;
    }

  }

}
