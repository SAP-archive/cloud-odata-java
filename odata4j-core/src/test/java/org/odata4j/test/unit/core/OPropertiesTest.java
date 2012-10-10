package org.odata4j.test.unit.core;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.joda.time.Instant;
import org.junit.Test;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;

public class OPropertiesTest {

  private static final String NAME = "name";
  private static final String VALUE = "value";
  private static final String HEX_VALUE = "0x76616c7565";

  @Test
  public void stringPropertyToStringTest() {
    OProperty<String> property = OProperties.simple(NAME, VALUE);
    String toString = property.toString();
    Assert.assertTrue(toString.contains(NAME));
    Assert.assertTrue(toString.contains(VALUE));
  }

  @Test
  public void binaryPropertyToStringTest() {
    OProperty<byte[]> property = OProperties.simple(NAME, VALUE.getBytes());
    String toString = property.toString();
    Assert.assertTrue(toString.contains(NAME));
    Assert.assertTrue(toString.contains(HEX_VALUE));
  }

  @Test
  public void datetimeFromInstantWithNullValue() throws Exception {
    Instant instant = null;
    assertThatValueIsNull(OProperties.datetime(NAME, instant));
  }

  @Test
  public void datetimeFromDateWithNullValue() throws Exception {
    Date date = null;
    assertThatValueIsNull(OProperties.datetime(NAME, date));
  }

  @Test
  public void datetimeFromCalendarWithNullValue() throws Exception {
    Calendar calendar = null;
    assertThatValueIsNull(OProperties.datetime(NAME, calendar));
  }

  @Test
  public void datetimeFromTimestampWithNullValue() throws Exception {
    Timestamp timestamp = null;
    assertThatValueIsNull(OProperties.datetime(NAME, timestamp));
  }

  @Test
  public void datetimeFromSqlDateWithNullValue() throws Exception {
    java.sql.Date date = null;
    assertThatValueIsNull(OProperties.datetime(NAME, date));
  }

  @Test
  public void datetimeFromTimeWithNullValue() throws Exception {
    Time time = null;
    assertThatValueIsNull(OProperties.datetime(NAME, time));
  }

  @Test
  public void timeFromDateWithNullValue() throws Exception {
    Date date = null;
    assertThatValueIsNull(OProperties.time(NAME, date));
  }

  @Test
  public void timeFromCalendarWithNullValue() throws Exception {
    Calendar calendar = null;
    assertThatValueIsNull(OProperties.time(NAME, calendar));
  }

  @Test
  public void timeFromTimestampWithNullValue() throws Exception {
    Timestamp timestamp = null;
    assertThatValueIsNull(OProperties.time(NAME, timestamp));
  }

  @Test
  public void timeFromTimeWithNullValue() throws Exception {
    Time time = null;
    assertThatValueIsNull(OProperties.time(NAME, time));
  }

  @Test
  public void decimalFromBigIntegerWithNullValue() throws Exception {
    BigInteger bigInteger = null;
    assertThatValueIsNull(OProperties.decimal(NAME, bigInteger));
  }

  private void assertThatValueIsNull(OProperty<?> property) {
    assertThat(property.getValue(), nullValue());
  }
}
