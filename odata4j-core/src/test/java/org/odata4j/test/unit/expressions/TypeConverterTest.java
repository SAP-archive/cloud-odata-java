package org.odata4j.test.unit.expressions;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.Assert;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.odata4j.internal.TypeConverter;

public class TypeConverterTest {

  @Test
  public void testTypeConverter() {
    Assert.assertNull(TypeConverter.convert(null, Object.class));
    Assert.assertEquals((byte) 16, (Object) TypeConverter.convert(16, Byte.class));
    Assert.assertEquals(16, (Object) TypeConverter.convert(16, Integer.class));
  }

  @Test
  public void testTemporalTypes() throws ParseException {

    DateFormat dateTimeParser = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US);
    DateFormat timeParser = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.US);
    Calendar cal = Calendar.getInstance();

    Assert.assertEquals(dateTimeParser.parse("03/28/2011 7:20:21 pm"),
        TypeConverter.convert(new LocalDateTime(2011, 03, 28, 19, 20, 21), Date.class));

    Assert.assertEquals(timeParser.parse("7:20:21 pm"),
        TypeConverter.convert(new LocalTime(19, 20, 21), Date.class));

    cal.setTime(dateTimeParser.parse("03/28/2011 7:20:21 pm"));
    Assert.assertEquals(cal,
        TypeConverter.convert(new LocalDateTime(2011, 03, 28, 19, 20, 21), Calendar.class));

    cal.setTime(timeParser.parse("7:20:21 pm"));
    Assert.assertEquals(cal,
        TypeConverter.convert(new LocalTime(19, 20, 21), Calendar.class));

    Assert.assertEquals(new java.sql.Time(timeParser.parse("7:20:21 pm").getTime()),
        TypeConverter.convert(new LocalDateTime(1970, 1, 1, 19, 20, 21), java.sql.Time.class));

    Assert.assertEquals(new java.sql.Time(timeParser.parse("7:20:21 pm").getTime()),
        TypeConverter.convert(new LocalTime(19, 20, 21), java.sql.Time.class));

    Assert.assertEquals(new java.sql.Date(dateTimeParser.parse("03/28/2011 0:00:00 am").getTime()),
        TypeConverter.convert(new LocalDateTime(2011, 03, 28, 0, 0), java.sql.Date.class));

    Assert.assertEquals(new java.sql.Timestamp(dateTimeParser.parse("03/28/2011 7:20:21 pm").getTime()),
        TypeConverter.convert(new LocalDateTime(2011, 03, 28, 19, 20, 21), java.sql.Timestamp.class));

    Assert.assertEquals(new java.sql.Timestamp(timeParser.parse("7:20:21 pm").getTime()),
        TypeConverter.convert(new LocalTime(19, 20, 21), java.sql.Timestamp.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void convertLocalDateTimeWithDateComponentsToSqlTimeFails() throws Exception {
    TypeConverter.convert(new LocalDateTime(2011, 03, 28, 19, 20, 21), java.sql.Time.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void convertLocalDateTimeWithTimeComponentsToSqlDateFails() throws Exception {
    TypeConverter.convert(new LocalDateTime(2011, 03, 28, 19, 20, 21), java.sql.Date.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void convertLocalTimeToSqlDateFails() throws Exception {
    TypeConverter.convert(new LocalTime(19, 20, 21), java.sql.Date.class);
  }
}
