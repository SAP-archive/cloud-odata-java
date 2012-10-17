package com.sap.core.odata.core.edm;

import com.sap.core.odata.core.edm.EdmType;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.core.Guid;
import org.odata4j.core.UnsignedByte;

public enum EdmSimpleType implements EdmType {

  //TODO: SimpleTypeInterface
  
  BINARY("Edm.Binary", byte[].class, Byte[].class), BOOLEAN("Edm.Boolean", Boolean.class, boolean.class), BYTE("Edm.Byte", UnsignedByte.class), DATETIME("Edm.DateTime", LocalDateTime.class, Instant.class, Date.class, Calendar.class, Timestamp.class,
      java.sql.Date.class), DATETIMEOFFSET("Edm.DateTimeOffset", DateTime.class), DECIMAL("Edm.Decimal", BigDecimal.class), DOUBLE("Edm.Double", Double.class, double.class), GUID("Edm.Guid", Guid.class, UUID.class), INT16("Edm.Int16", Short.class,
      short.class), INT32("Edm.Int32", Integer.class, int.class), INT64("Edm.Int64", Long.class, long.class), SBYTE("Edm.SByte", Byte.class, byte.class), SINGLE("Edm.Single", Float.class, float.class), STRING("Edm.String", String.class, char.class,
      Character.class), TIME("Edm.Time", LocalTime.class, Time.class);

  
  private String name;
  private Class<?> canonicalJavaType;
  private List<Class<?>> alternativeJavaTypes = new ArrayList<Class<?>>();

  private <V> EdmSimpleType(String name, Class<?> canonicalJavaType, Class<?>... alternateJavaTypes) {
    this.name = name;
    this.canonicalJavaType = canonicalJavaType;

    for (Class<?> item : alternateJavaTypes) {
      this.alternativeJavaTypes.add(item);
    }

  }

  public String getName() {

    return this.name;
  }

  public Class<?>[] getAlternativeJavaTypes() {
    return alternativeJavaTypes.toArray(new Class<?>[0]);
  }

  public Class<?> getCanonicalJavaType() {
    return canonicalJavaType;
  }

  @Override
  public String getNamespace() {
    return "Edm";
  }

  @Override
  public EdmTypeKind getKind() {

    return EdmTypeKind.SIMPLE;
  }

}
