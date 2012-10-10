package org.odata4j.internal;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.odata4j.core.Guid;

public class TypeConverter {

  @SuppressWarnings("unchecked")
  public static <T> T convert(Object obj, Class<T> desiredClass) {
    if (obj == null)
      return null;

    Class<?> objClass = obj.getClass();
    if (objClass.equals(desiredClass))
      return (T) obj;

    // number conversions
    if (Number.class.isAssignableFrom(objClass)) {
      if (desiredClass.equals(Byte.TYPE) || desiredClass.equals(Byte.class))
        return (T) (Object) ((Number) obj).byteValue();
      else if (desiredClass.equals(Short.TYPE) || desiredClass.equals(Short.class))
        return (T) (Object) ((Number) obj).shortValue();
      else if (desiredClass.equals(Integer.TYPE) || desiredClass.equals(Integer.class))
        return (T) (Object) ((Number) obj).intValue();
      else if (desiredClass.equals(Long.TYPE) || desiredClass.equals(Long.class))
        return (T) (Object) ((Number) obj).longValue();
      else if (desiredClass.equals(Float.TYPE) || desiredClass.equals(Float.class))
        return (T) (Object) ((Number) obj).floatValue();
      else if (desiredClass.equals(Double.TYPE) || desiredClass.equals(Double.class))
        return (T) (Object) ((Number) obj).doubleValue();
    }

    // date / time conversions
    if (desiredClass.equals(Date.class))
      if (objClass.equals(LocalDateTime.class))
        return (T) getDateFromLocalDateTime(obj);
      else if (objClass.equals(LocalTime.class))
        return (T) getDateFromLocalTime(obj);

    if (desiredClass.equals(Calendar.class))
      if (objClass.equals(LocalDateTime.class)) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getMillisFromLocalDateTime(obj));
        return (T) cal;
      } else if (objClass.equals(LocalTime.class)) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getMillisFromLocalTime(obj));
        return (T) cal;
      }

    if (desiredClass.equals(Timestamp.class))
      if (objClass.equals(LocalDateTime.class))
        return (T) new Timestamp(getMillisFromLocalDateTime(obj));
      else if (objClass.equals(LocalTime.class))
        return (T) new Timestamp(getMillisFromLocalTime(obj));

    if (desiredClass.equals(java.sql.Date.class))
      if (objClass.equals(LocalDateTime.class)) {
        if (localDateTimeHasTimeComponents(obj))
          throw new IllegalArgumentException("org.joda.time.LocalDateTime cannot be converted into java.sql.Date when hours, minutes, seconds, and milliseconds are not 'normalized', i.e. zero");
        return (T) new java.sql.Date(getMillisFromLocalDateTime(obj));
      }

    if (desiredClass.equals(Time.class))
      if (objClass.equals(LocalDateTime.class)) {
        if (localDateTimeHasDateComponents(obj))
          throw new IllegalArgumentException("org.joda.time.LocalDateTime cannot be converted into java.sql.Time when date components differ from 'zero epoch', i.e. 1970-01-01");
        return (T) new Time(getMillisFromLocalDateTime(obj));
      } else if (objClass.equals(LocalTime.class)) {
        return (T) new Time(getMillisFromLocalTime(obj));
      }

    if (desiredClass.equals(LocalDateTime.class))
      if (objClass.equals(Timestamp.class) || objClass.equals(java.sql.Date.class) || objClass.equals(Time.class))
        return (T) new LocalDateTime(obj);
      else if (Date.class.isAssignableFrom(objClass))
        return (T) LocalDateTime.fromDateFields((Date) obj);
      else if (Calendar.class.isAssignableFrom(objClass))
        return (T) LocalDateTime.fromCalendarFields((Calendar) obj);

    if (desiredClass.equals(LocalTime.class))
      if (objClass.equals(Timestamp.class) || objClass.equals(Time.class))
        return (T) new LocalTime(obj);
      else if (Date.class.isAssignableFrom(objClass) && !objClass.equals(java.sql.Date.class))
        return (T) LocalTime.fromDateFields((Date) obj);
      else if (Calendar.class.isAssignableFrom(objClass))
        return (T) LocalTime.fromCalendarFields((Calendar) obj);

    // guid conversions
    if (desiredClass.equals(Guid.class) && objClass.equals(UUID.class))
      return (T) Guid.fromUUID((UUID) obj);

    if (desiredClass.equals(UUID.class) && (objClass.equals(Guid.class) || objClass.equals(String.class)))
      return (T) UUID.fromString(obj.toString());

    throw new UnsupportedOperationException(String.format("Unable to convert %s into %s", objClass.getName(), desiredClass.getName()));
  }

  private static Date getDateFromLocalDateTime(Object obj) {
    return ((LocalDateTime) obj).toDateTime().toDate();
  }

  private static Date getDateFromLocalTime(Object obj) {
    return new LocalDateTime(((LocalTime) obj).getMillisOfDay(), DateTimeZone.UTC).toDateTime().toDate();
  }

  private static long getMillisFromLocalDateTime(Object obj) {
    return ((LocalDateTime) obj).toDateTime().getMillis();
  }

  private static long getMillisFromLocalTime(Object obj) {
    return new LocalDateTime(((LocalTime) obj).getMillisOfDay(), DateTimeZone.UTC).toDateTime().getMillis();
  }

  private static boolean localDateTimeHasTimeComponents(Object obj) {
    return ((LocalDateTime) obj).getMillisOfDay() != 0;
  }

  private static boolean localDateTimeHasDateComponents(Object obj) {
    return ((LocalDateTime) obj).toDateTime(DateTimeZone.UTC).getMillis() != ((LocalDateTime) obj).getMillisOfDay();
  }
}
