package org.odata4j.test.unit.expressions;

import junit.framework.Assert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import org.odata4j.internal.InternalUtil;

public class DateTimeFormatTest {

  @Test
  public void testyyyyMMddHHmm() {
    LocalDateTime ldt = InternalUtil.parseDateTimeFromXml("2010-12-20T17:34");

    Assert.assertEquals(2010, ldt.getYear());
    Assert.assertEquals(12, ldt.getMonthOfYear());
    Assert.assertEquals(20, ldt.getDayOfMonth());
    Assert.assertEquals(17, ldt.getHourOfDay());
    Assert.assertEquals(34, ldt.getMinuteOfHour());
    Assert.assertEquals(0, ldt.getSecondOfMinute());
    Assert.assertEquals(0, ldt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmss() {
    LocalDateTime ldt = InternalUtil.parseDateTimeFromXml("2010-12-20T17:34:05");

    Assert.assertEquals(2010, ldt.getYear());
    Assert.assertEquals(12, ldt.getMonthOfYear());
    Assert.assertEquals(20, ldt.getDayOfMonth());
    Assert.assertEquals(17, ldt.getHourOfDay());
    Assert.assertEquals(34, ldt.getMinuteOfHour());
    Assert.assertEquals(5, ldt.getSecondOfMinute());
    Assert.assertEquals(0, ldt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssfffffff() {
    LocalDateTime ldt = InternalUtil.parseDateTimeFromXml("2010-12-20T17:34:05.1234567");

    Assert.assertEquals(2010, ldt.getYear());
    Assert.assertEquals(12, ldt.getMonthOfYear());
    Assert.assertEquals(20, ldt.getDayOfMonth());
    Assert.assertEquals(17, ldt.getHourOfDay());
    Assert.assertEquals(34, ldt.getMinuteOfHour());
    Assert.assertEquals(5, ldt.getSecondOfMinute());
    Assert.assertEquals(123, ldt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssffffffIgnoreZ() {
    LocalDateTime ldt = InternalUtil.parseDateTimeFromXml("2012-02-09T20:21:10.283459Z");

    Assert.assertEquals(2012, ldt.getYear());
    Assert.assertEquals(2, ldt.getMonthOfYear());
    Assert.assertEquals(9, ldt.getDayOfMonth());
    Assert.assertEquals(20, ldt.getHourOfDay());
    Assert.assertEquals(21, ldt.getMinuteOfHour());
    Assert.assertEquals(10, ldt.getSecondOfMinute());
    Assert.assertEquals(283, ldt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssfffffffZZ() {
    DateTime dt = InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34:05.1234567Z");
    dt = dt.toDateTime(DateTimeZone.UTC);

    Assert.assertEquals(2010, dt.getYear());
    Assert.assertEquals(12, dt.getMonthOfYear());
    Assert.assertEquals(20, dt.getDayOfMonth());
    Assert.assertEquals(17, dt.getHourOfDay());
    Assert.assertEquals(34, dt.getMinuteOfHour());
    Assert.assertEquals(5, dt.getSecondOfMinute());
    Assert.assertEquals(123, dt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssZZ() {
    DateTime dt = InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34:05Z");
    dt = dt.toDateTime(DateTimeZone.UTC);

    Assert.assertEquals(2010, dt.getYear());
    Assert.assertEquals(12, dt.getMonthOfYear());
    Assert.assertEquals(20, dt.getDayOfMonth());
    Assert.assertEquals(17, dt.getHourOfDay());
    Assert.assertEquals(34, dt.getMinuteOfHour());
    Assert.assertEquals(5, dt.getSecondOfMinute());
    Assert.assertEquals(0, dt.getMillisOfSecond());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testyyyyMMddHHmmZZ() {
    InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34Z");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testyyyyMMddHHmmp0200() {
    InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34+02:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testyyyyMMddHHmmm0600() {
    InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34-06:00");
  }

  @Test
  public void testyyyyMMddHHmmssm0600() {
    DateTime dt = InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34:05-06:00");
    dt = dt.toDateTime(DateTimeZone.UTC);

    Assert.assertEquals(2010, dt.getYear());
    Assert.assertEquals(12, dt.getMonthOfYear());
    Assert.assertEquals(20, dt.getDayOfMonth());
    Assert.assertEquals(23, dt.getHourOfDay());
    Assert.assertEquals(34, dt.getMinuteOfHour());
    Assert.assertEquals(5, dt.getSecondOfMinute());
    Assert.assertEquals(0, dt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssfffffffm0600() {
    DateTime dt = InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34:05.1234567-06:00");
    dt = dt.toDateTime(DateTimeZone.UTC);

    Assert.assertEquals(2010, dt.getYear());
    Assert.assertEquals(12, dt.getMonthOfYear());
    Assert.assertEquals(20, dt.getDayOfMonth());
    Assert.assertEquals(23, dt.getHourOfDay());
    Assert.assertEquals(34, dt.getMinuteOfHour());
    Assert.assertEquals(5, dt.getSecondOfMinute());
    Assert.assertEquals(123, dt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssfffm0600() {
    DateTime dt = InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34:05.123-06:00");
    dt = dt.toDateTime(DateTimeZone.UTC);

    Assert.assertEquals(2010, dt.getYear());
    Assert.assertEquals(12, dt.getMonthOfYear());
    Assert.assertEquals(20, dt.getDayOfMonth());
    Assert.assertEquals(23, dt.getHourOfDay());
    Assert.assertEquals(34, dt.getMinuteOfHour());
    Assert.assertEquals(5, dt.getSecondOfMinute());
    Assert.assertEquals(123, dt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssffm0600() {
    DateTime dt = InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34:05.12-06:00");
    dt = dt.toDateTime(DateTimeZone.UTC);

    Assert.assertEquals(2010, dt.getYear());
    Assert.assertEquals(12, dt.getMonthOfYear());
    Assert.assertEquals(20, dt.getDayOfMonth());
    Assert.assertEquals(23, dt.getHourOfDay());
    Assert.assertEquals(34, dt.getMinuteOfHour());
    Assert.assertEquals(5, dt.getSecondOfMinute());
    Assert.assertEquals(120, dt.getMillisOfSecond());
  }

  @Test
  public void testyyyyMMddHHmmssfm0600() {
    DateTime dt = InternalUtil.parseDateTimeOffsetFromXml("2010-12-20T17:34:05.1-06:00");
    dt = dt.toDateTime(DateTimeZone.UTC);

    Assert.assertEquals(2010, dt.getYear());
    Assert.assertEquals(12, dt.getMonthOfYear());
    Assert.assertEquals(20, dt.getDayOfMonth());
    Assert.assertEquals(23, dt.getHourOfDay());
    Assert.assertEquals(34, dt.getMinuteOfHour());
    Assert.assertEquals(5, dt.getSecondOfMinute());
    Assert.assertEquals(100, dt.getMillisOfSecond());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testyyyyMMddHHmmsspm0600() {
    InternalUtil.parseDateTimeFromXml("2010-12-20T17:34:05.-06:00");
  }

  @Test
  public void testFormatDateTimeyyyyMMddHHmm() {
    LocalDateTime dt = new LocalDateTime(2010, 12, 20, 17, 34);
    Assert.assertEquals("2010-12-20T17:34", InternalUtil.formatDateTimeForXml(dt));
  }

  @Test
  public void testFormatDateTimeyyyyMMddHHmmss() {
    LocalDateTime dt = new LocalDateTime(2010, 12, 20, 17, 34, 5);
    Assert.assertEquals("2010-12-20T17:34:05", InternalUtil.formatDateTimeForXml(dt));
  }

  @Test
  public void testFormatDateTimeyyyyMMddHHmmssfffffff() {
    LocalDateTime dt = new LocalDateTime(2010, 12, 20, 17, 34, 5, 123);
    Assert.assertEquals("2010-12-20T17:34:05.123", InternalUtil.formatDateTimeForXml(dt));
  }

  @Test
  public void testFormatDateTimeOffsetyyyyMMddHHmm() {
    Chronology c = ISOChronology.getInstance(DateTimeZone.forOffsetHours(1));
    DateTime dt = new DateTime(2010, 12, 20, 17, 34, 0, 0, c);
    Assert.assertEquals("2010-12-20T17:34:00+01:00", InternalUtil.formatDateTimeOffsetForXml(dt));
  }

  @Test
  public void testFormatDateTimeOffsetyyyyMMddHHmmss() {
    Chronology c = ISOChronology.getInstance(DateTimeZone.forOffsetHours(1));
    DateTime dt = new DateTime(2010, 12, 20, 17, 34, 5, 0, c);
    Assert.assertEquals("2010-12-20T17:34:05+01:00", InternalUtil.formatDateTimeOffsetForXml(dt));
  }

  @Test
  public void testFormatDateTimeOffsetyyyyMMddHHmmssfffffff() {
    Chronology c = ISOChronology.getInstance(DateTimeZone.forOffsetHours(1));
    DateTime dt = new DateTime(2010, 12, 20, 17, 34, 5, 123, c);
    Assert.assertEquals("2010-12-20T17:34:05.123+01:00", InternalUtil.formatDateTimeOffsetForXml(dt));
  }

  @Test
  public void testDateTimeOffsetParseFormat() {
    dtoCheck(new DateTime(1967, 01, 02, 03, 04, 05, 123, DateTimeZone.UTC), "Z", 0);
    dtoCheck(new DateTime(1967, 01, 02, 03, 04, 05, 123, DateTimeZone.forOffsetHours(-7)), "-07:00", -7 * 60 * 60 * 1000);
    dtoCheck(new DateTime(1967, 01, 02, 03, 04, 05, 123, DateTimeZone.forOffsetHours(+5)), "+05:00", 5 * 60 * 60 * 1000);
    dtoCheck(new DateTime(1967, 01, 02, 03, 04, 05, 123, DateTimeZone.forOffsetHoursMinutes(3, 30)), "+03:30", ((3 * 60) + 30) * 60 * 1000);
  }

  private void dtoCheck(DateTime lhs, String tzS, int tzOffsetMillis) {
    // DateTime---->String
    Assert.assertTrue(lhs.getZone().getOffset(0) == tzOffsetMillis);
    String f = InternalUtil.formatDateTimeOffsetForXml(lhs);
    //System.out.println("lhs : " + f);
    Assert.assertTrue(f.endsWith(tzS));

    // back to DateTime
    DateTime utcp = InternalUtil.parseDateTimeOffsetFromXml(f);
    f = InternalUtil.formatDateTimeOffsetForXml(lhs);
    //System.out.println("rhs: " + f);
    Assert.assertTrue(f.endsWith(tzS));

    // make sure the timezone was preserved.
    //System.out.println(" lhs zone: " + utc.getZone().getID() + " rhs zone: " + utcp.getZone().getID());
    Assert.assertTrue(utcp.getZone().getOffset(0) == tzOffsetMillis);
    Assert.assertEquals(lhs.getMillis(), utcp.getMillis());

    // zomg, DateTime.equals is all messed up...
  }
}
