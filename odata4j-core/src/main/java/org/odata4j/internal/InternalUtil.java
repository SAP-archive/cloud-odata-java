package org.odata4j.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.core4j.Enumerable;
import org.core4j.Func1;
import org.core4j.Funcs;
import org.core4j.ThrowingFunc1;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISOPeriodFormat;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.ODataVersion;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OLink;
import org.odata4j.core.OProperty;
import org.odata4j.core.ORelatedEntitiesLinkInline;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.core.Throwables;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.producer.inmemory.BeanModel;

public class InternalUtil {

  private static final Pattern DATETIME_XML_PATTERN = Pattern.compile("" +
      "^" +
      "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})" + // group 1 (datetime)
      "(:\\d{2})?" + // group 2 (seconds)
      "(\\.\\d{1,7})?" + // group 3 (nanoseconds)
      "(Z)?" + // group 4 (tz, ignored - handles bad services)
      "$");

  private static final Pattern DATETIMEOFFSET_XML_PATTERN = Pattern.compile("" +
      "^" +
      "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})" + // group 1 (datetime)
      "(\\.\\d{1,7})?" + // group 2 (nanoSeconds)
      "(((\\+|-)\\d{2}:\\d{2})|(Z))" + // group 3 (offset) / group 6 (utc)
      "$");

  private static final Pattern DATETIME_JSON_PATTERN = Pattern.compile("" +
      "^/Date\\(" +
      "((\\+|-)?\\d+)" + // group 1 (ticks)
      "((\\+|-)\\d{4})?" + // group 3 (offset)
      "\\)/$");

  private static final DateTimeFormatter DATETIME_XML = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
  private static final DateTimeFormatter DATETIME_WITH_SECONDS_XML = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
  private static final DateTimeFormatter DATETIME_WITH_MILLIS_XML = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

  private static final DateTimeFormatter DATETIMEOFFSET_XML = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
  private static final DateTimeFormatter DATETIMEOFFSET_WITH_MILLIS_XML = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

  private static final String DATETIME_JSON_SUFFIX = ")\\/\"";
  private static final String DATETIME_JSON_PREFIX = "\"\\/Date(";

  public static LocalDateTime parseDateTimeFromXml(String value) {
    Matcher matcher = DATETIME_XML_PATTERN.matcher(value);

    if (matcher.matches()) {
      String dateTime = matcher.group(1);
      String seconds = matcher.group(2);
      String nanoSeconds = matcher.group(3);

      if (seconds == null && nanoSeconds != null)
        throw new IllegalArgumentException("Illegal datetime format " + value);

      if (seconds == null)
        return DATETIME_XML.parseDateTime(dateTime).toLocalDateTime();

      if (nanoSeconds == null)
        return DATETIME_WITH_SECONDS_XML.parseDateTime(dateTime + seconds).toLocalDateTime();

      if (nanoSeconds.length() <= 4)
        return DATETIME_WITH_MILLIS_XML.parseDateTime(dateTime + seconds + nanoSeconds).toLocalDateTime();

      return adjustMillis(DATETIME_WITH_MILLIS_XML.parseDateTime(dateTime + seconds + nanoSeconds.substring(0, 4)), nanoSeconds).toLocalDateTime();
    }
    throw new IllegalArgumentException("Illegal datetime format " + value);
  }

  public static DateTime parseDateTimeOffsetFromXml(String value) {
    Matcher matcher = DATETIMEOFFSET_XML_PATTERN.matcher(value);

    if (matcher.matches()) {
      String dateTime = matcher.group(1);
      String nanoSeconds = matcher.group(2);
      String offset = matcher.group(3);
      String utc = matcher.group(6);

      if (utc != null)
        if (utc.equals("Z"))
          offset = "+00:00";
        else
          throw new IllegalArgumentException("Illegal datetimeoffset format " + value);

      if (nanoSeconds == null)
        return DATETIMEOFFSET_XML.withOffsetParsed().parseDateTime(dateTime + offset);

      if (nanoSeconds.length() <= 4)
        return DATETIMEOFFSET_WITH_MILLIS_XML.withOffsetParsed().parseDateTime(dateTime + nanoSeconds + offset);

      return adjustMillis(DATETIMEOFFSET_WITH_MILLIS_XML.withOffsetParsed().parseDateTime(dateTime + nanoSeconds.substring(0, 4) + offset), nanoSeconds);
    }
    throw new IllegalArgumentException("Illegal datetimeoffset format " + value);
  }

  private static DateTime adjustMillis(final DateTime dateTime, final String nanoSeconds) {
    return Math.round(Double.parseDouble("0." + nanoSeconds.substring(4))) == 0 ? dateTime : dateTime.plusMillis(1);
  }

  public static LocalDateTime parseDateTimeFromJson(String value) {
    DateTime dateTime = parseDateString(value);
    if (dateTime != null)
      return dateTime.toLocalDateTime();
    else
      // required to support datajs clients (although not spec compliant)
      return InternalUtil.parseDateTimeFromXml(value);
  }

  public static DateTime parseDateTimeOffsetFromJson(String value) {
    DateTime dateTime = parseDateString(value);
    if (dateTime != null)
      return dateTime;
    else
      // required to support datajs clients (although not spec compliant)
      return InternalUtil.parseDateTimeOffsetFromXml(value);
  }

  private static DateTime parseDateString(String value) {
    Matcher matcher = DATETIME_JSON_PATTERN.matcher(value);
    if (matcher.matches()) {
      String ticksString = matcher.group(1);
      long ticks = Long.valueOf(ticksString.replace("+", ""));
      String offsetString = matcher.group(3);
      if (offsetString != null) {
        int offset = Integer.valueOf(offsetString.replace("+", ""));
        return new DateTime(ticks, DateTimeZone.forOffsetHoursMinutes(offset / 60, offset % 60)).plusMinutes(offset);
      } else {
        return new DateTime(ticks, DateTimeZone.UTC);
      }
    }
    return null;
  }

  public static LocalTime parseTime(String value) {
    Period period = ISOPeriodFormat.standard().parsePeriod(value);
    return new LocalTime(period.toStandardDuration().getMillis(), DateTimeZone.UTC);
  }

  public static String formatDateTimeForXml(LocalDateTime localDateTime) {
    if (localDateTime == null)
      return null;

    if (localDateTime.getMillisOfSecond() != 0)
      return localDateTime.toString(DATETIME_WITH_MILLIS_XML);
    else if (localDateTime.getSecondOfMinute() != 0)
      return localDateTime.toString(DATETIME_WITH_SECONDS_XML);
    else
      return localDateTime.toString(DATETIME_XML);
  }

  public static String formatDateTimeOffsetForXml(DateTime dateTime) {
    if (dateTime == null)
      return null;

    String result;
    if (dateTime.getMillisOfSecond() != 0)
      result = dateTime.toString(DATETIMEOFFSET_WITH_MILLIS_XML);
    else
      result = dateTime.toString(DATETIMEOFFSET_XML);

    return result.replaceFirst("(\\+|-)00:00$", "Z");
  }

  public static String formatDateTimeForJson(LocalDateTime localDateTime) {
    return DATETIME_JSON_PREFIX + localDateTime.toDateTime(DateTimeZone.UTC).getMillis() + DATETIME_JSON_SUFFIX;
  }

  public static String formatDateTimeOffsetForJson(DateTime dateTime) {
    long millis = dateTime.getMillis();
    int offsetInMillis = dateTime.getZone().getOffset(millis);
    return DATETIME_JSON_PREFIX + (millis - offsetInMillis) + String.format(Locale.US, "%+05d", offsetInMillis / 1000 / 60) + DATETIME_JSON_SUFFIX;
  }

  public static String formatTimeForXml(LocalTime localTime) {
    return ISOPeriodFormat.standard().print(new Period(localTime.getMillisOfDay()));
  }

  public static String formatTimeForJson(LocalTime localTime) {
    return "\"" + formatTimeForXml(localTime) + "\"";
  }

  public static String toString(DateTime utc) {
    return utc.toString("yyyy-MM-dd'T'HH:mm:ss'Z'");
  }

  public static String reflectionToString(final Object obj) {
    StringBuilder rt = new StringBuilder();
    Class<?> objClass = obj.getClass();
    rt.append(objClass.getSimpleName());
    rt.append('[');

    String content = Enumerable.create(objClass.getFields())
        .select(Funcs.wrap(new ThrowingFunc1<Field, String>() {
          public String apply(Field f) throws Exception {
            Object fValue = f.get(obj);
            return f.getName() + ":" + fValue;
          }
        })).join(",");

    rt.append(content);

    rt.append(']');
    return rt.toString();
  }

  @SuppressWarnings("unchecked")
  public static <T> T toEntity(Class<T> entityType, OEntity oe) {
    if (entityType.equals(OEntity.class))
      return (T) oe;
    else
      return (T) InternalUtil.toPojo(entityType, oe);
  }

  public static <T> T toPojo(Class<T> pojoClass, OEntity oe) {
    try {
      Constructor<T> defaultCtor = findDefaultDeclaredConstructor(pojoClass);
      if (defaultCtor == null)
        throw new RuntimeException(
            "Unable to find a default constructor for "
                + pojoClass.getName());

      if (!defaultCtor.isAccessible())
        defaultCtor.setAccessible(true);

      T rt = defaultCtor.newInstance();

      final BeanModel beanModel = new BeanModel(pojoClass);

      for (OProperty<?> op : oe.getProperties()) {
        if (beanModel.canWrite(op.getName()))
          beanModel.setPropertyValue(rt, op.getName(), op.getValue());
      }

      for (OLink l : oe.getLinks()) {
        if (l instanceof ORelatedEntitiesLinkInline) {
          ORelatedEntitiesLinkInline ol = (ORelatedEntitiesLinkInline) l;
          final String collectionName = ol.getTitle();
          if (beanModel.canWrite(ol.getTitle())) {
            Collection<Object> relatedEntities = ol
                .getRelatedEntities() == null
                ? null
                : Enumerable.create(ol.getRelatedEntities())
                    .select(new Func1<OEntity, Object>() {
                      @Override
                      public Object apply(OEntity input) {
                        return toPojo(
                            beanModel.getCollectionElementType(collectionName),
                            input);
                      }
                    }).toList();
            beanModel.setCollectionValue(rt, collectionName,
                relatedEntities);
          }
        } else if (l instanceof ORelatedEntityLink) {
          // TODO set entity
        }
      }

      return rt;
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }

  }

  @SuppressWarnings("unchecked")
  private static <T> Constructor<T> findDefaultDeclaredConstructor(
      Class<T> pojoClass) {
    for (Constructor<?> ctor : pojoClass.getDeclaredConstructors()) {
      if (ctor.getParameterTypes().length == 0)
        return (Constructor<T>) ctor;
    }
    return null;
  }

  public static String getEntityRelId(List<String> keyPropertyNames,
      final List<OProperty<?>> entityProperties, String entitySetName) {
    String key = null;
    if (keyPropertyNames != null) {
      Object[] keyProperties = Enumerable.create(keyPropertyNames)
          .select(new Func1<String, OProperty<?>>() {
            public OProperty<?> apply(String input) {
              for (OProperty<?> entityProperty : entityProperties)
                if (entityProperty.getName().equals(input))
                  return entityProperty;
              throw new IllegalArgumentException("Key property '"
                  + input + "' is invalid");
            }
          }).cast(Object.class).toArray(Object.class);
      key = OEntityKey.create(keyProperties).toKeyString();
    }

    return entitySetName + key;

  }

  public static String getEntityRelId(OEntity oe) {
    return getEntityRelId(oe.getEntitySet(), oe.getEntityKey());
  }

  public static String getEntityRelId(EdmEntitySet entitySet, OEntityKey entityKey) {
    String key = entityKey.toKeyString();
    return entitySet.getName() + key;
  }

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw Throwables.propagate(e);
    }
  }

  public static ODataVersion getDataServiceVersion(String headerValue) {
    ODataVersion version = ODataConstants.DATA_SERVICE_VERSION;
    if (headerValue != null) {
      String[] str = headerValue.split(";");
      version = ODataVersion.parse(str[0]);
    }
    return version;
  }

  public static final int COPY_BUFFER_SIZE = 8 * 1024;

  public static void copyInputToOutput(InputStream inStream, OutputStream outStream) throws IOException {
    byte[] buf = new byte[COPY_BUFFER_SIZE];
    int n;
    while ((n = inStream.read(buf)) != -1) {
      outStream.write(buf, 0, n);
    }
    outStream.flush();
  }
}
