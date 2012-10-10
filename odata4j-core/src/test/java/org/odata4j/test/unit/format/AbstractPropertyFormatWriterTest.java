package org.odata4j.test.unit.format;

import java.io.StringWriter;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.odata4j.core.Guid;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.FormatType;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.producer.PropertyResponse;

public abstract class AbstractPropertyFormatWriterTest {

  protected static final OProperty<LocalDateTime> DATETIME = OProperties.simple("DateTime", EdmSimpleType.DATETIME, new LocalDateTime(2005, 4, 3, 1, 2));
  protected static final OProperty<LocalDateTime> DATETIME_WITH_SECONDS = OProperties.simple("DateTime", EdmSimpleType.DATETIME, new LocalDateTime(2006, 5, 4, 1, 2, 3));
  protected static final OProperty<LocalDateTime> DATETIME_WITH_MILLIS = OProperties.simple("DateTime", EdmSimpleType.DATETIME, new LocalDateTime(2007, 6, 5, 1, 2, 3, 4));

  protected static final OProperty<DateTime> DATETIME_BEFORE_1970_NO_OFFSET = OProperties.simple("DateTimeOffset", EdmSimpleType.DATETIMEOFFSET, new DateTime(1969, 8, 7, 5, 6, 0, 0, DateTimeZone.UTC));
  protected static final OProperty<DateTime> DATETIME_WITH_SECONDS_POSITIVE_OFFSET = OProperties.simple("DateTimeOffset", EdmSimpleType.DATETIMEOFFSET, new DateTime(2006, 5, 4, 1, 2, 3, 0, DateTimeZone.forOffsetHours(7)));
  protected static final OProperty<DateTime> DATETIME_WITH_MILLIS_NEGATIVE_OFFSET = OProperties.simple("DateTimeOffset", EdmSimpleType.DATETIMEOFFSET, new DateTime(2007, 6, 5, 1, 2, 3, 4, DateTimeZone.forOffsetHours(-8)));

  protected static final OProperty<LocalTime> TIME = OProperties.simple("Time", EdmSimpleType.TIME, new LocalTime(1, 2, 3));
  protected static final OProperty<LocalTime> TIME_WITH_MILLIS = OProperties.simple("Time", EdmSimpleType.TIME, new LocalTime(1, 2, 3, 4));

  protected static final OProperty<Boolean> BOOLEAN_PROPERTY = OProperties.simple("Boolean", EdmSimpleType.BOOLEAN, Boolean.FALSE);

  protected static final OProperty<String> STRING_PROPERTY = OProperties.simple("String", EdmSimpleType.STRING, "<\"\t€\">");

  protected static final OProperty<Guid> GUID_PROPERTY = OProperties.simple("Guid", EdmSimpleType.GUID, Guid.fromString("4786c33c-1e3d-4b57-b5cf-a4b759acac44"));

  protected static final OProperty<BigDecimal> DECIMAL_PROPERTY = OProperties.simple("Decimal", EdmSimpleType.DECIMAL, BigDecimal.valueOf(-1234567890, 5));
  protected static final OProperty<BigDecimal> DECIMAL_PROPERTY_LARGE = OProperties.simple("DecimalLarge", EdmSimpleType.DECIMAL, BigDecimal.valueOf(1234567890123456789L, -1));

  protected static final OProperty<Double> DOUBLE_PROPERTY = OProperties.simple("Double", EdmSimpleType.DOUBLE, Double.valueOf("-1.23456789E-10"));

  protected static final OProperty<Short> INT16_PROPERTY = OProperties.simple("Int16", EdmSimpleType.INT16, Short.MIN_VALUE);

  protected static FormatWriter<PropertyResponse> formatWriter;

  protected StringWriter stringWriter;

  protected static void createFormatWriter(FormatType format) {
    formatWriter = FormatWriterFactory.getFormatWriter(PropertyResponse.class, null, format.toString(), null);
  }

  @Before
  public void setup() throws Exception {
    stringWriter = new StringWriter();
  }
}
