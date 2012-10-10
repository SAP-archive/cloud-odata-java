package org.odata4j.test.unit.format.json;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.math.BigDecimal;

import org.joda.time.DateTimeZone;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.core.Guid;
import org.odata4j.format.FormatType;
import org.odata4j.format.json.JsonStreamReaderFactory.JsonParseException;
import org.odata4j.test.unit.format.AbstractEntryFormatParserTest;

public class JsonEntryFormatParserTest extends AbstractEntryFormatParserTest {

  @BeforeClass
  public static void setupClass() throws Exception {
    createFormatParser(FormatType.JSON);
  }

  @Test
  public void dateTime() throws Exception {
    verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"\\/Date(1112490120000)\\/\"")), DATETIME);
  }

  @Test
  public void dateTimeWithSeconds() throws Exception {
    verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"\\/Date(1146704523000)\\/\"")), DATETIME_WITH_SECONDS);
  }

  @Test
  public void dateTimeWithMillis() throws Exception {
    verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"\\/Date(1181005323004)\\/\"")), DATETIME_WITH_MILLIS);
    verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"\\/Date(1181005323010)\\/\"")), DATETIME_WITH_MILLIS.withMillisOfSecond(10));
  }

  @Test
  public void dateTimeWithOffset() throws Exception {
    verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"\\/Date(-12682440000+0000)\\/\"")), DATETIME_BEFORE_1970_NO_OFFSET.toLocalDateTime());
  }

  @Test
  public void dateTimeInXmlFormat() throws Exception {
    verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"2005-04-03T01:02\"")), DATETIME);
  }

  @Test
  public void dateTimeWithSecondsInXmlFormatZIgnored() throws Exception {
    verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"2006-05-04T01:02:03Z\"")), DATETIME_WITH_SECONDS);
  }

  @Test
  public void dateTimeNoOffset() throws Exception {
    verifyDateTimeOffsetPropertyValue(formatParser.parse(buildJson("\"DateTimeOffset\" : \"\\/Date(-12682440000+0000)\\/\"")), DATETIME_BEFORE_1970_NO_OFFSET);
  }

  @Test
  public void dateTimeWithSecondsPositiveOffset() throws Exception {
    verifyDateTimeOffsetPropertyValue(formatParser.parse(buildJson("\"DateTimeOffset\" : \"\\/Date(1146654123000+0420)\\/\"")), DATETIME_WITH_SECONDS_POSITIVE_OFFSET);
  }

  @Test
  public void dateTimeWithMillisNegativeOffset() throws Exception {
    verifyDateTimeOffsetPropertyValue(formatParser.parse(buildJson("\"DateTimeOffset\" : \"\\/Date(1181062923004-0480)\\/\"")), DATETIME_WITH_MILLIS_NEGATIVE_OFFSET);
  }

  @Test
  public void dateTimeWithoutOffset() throws Exception {
    verifyDateTimeOffsetPropertyValue(formatParser.parse(buildJson("\"DateTimeOffset\" : \"\\/Date(1112490120000)\\/\"")), DATETIME.toDateTime(DateTimeZone.UTC));
  }

  @Test
  public void dateTimeNoOffsetInXmlFormat() throws Exception {
    verifyDateTimeOffsetPropertyValue(formatParser.parse(buildJson("\"DateTimeOffset\" : \"1969-08-07T05:06:00Z\"")), DATETIME_BEFORE_1970_NO_OFFSET);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalDateTimeOffset() throws Exception {
    formatParser.parse(buildJson("\"DateTimeOffset\" : \"2005-04-03T01:02\""));
  }

  @Test
  public void time() throws Exception {
    verifyTimePropertyValue(formatParser.parse(buildJson("\"Time\" : \"PT1H2M3S\"")), TIME);
  }

  @Test
  public void timeWithMillis() throws Exception {
    verifyTimePropertyValue(formatParser.parse(buildJson("\"Time\" : \"PT1H2M3.004S\"")), TIME_WITH_MILLIS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalTime() throws Exception {
    formatParser.parse(buildJson("\"Time\" : \"01:02:03\""));
  }

  @Test
  public void bool() throws Exception {
    assertThat((Boolean) formatParser.parse(buildJson("\"Boolean\": true")).getEntity().getProperty(BOOLEAN_NAME).getValue(), is(BOOLEAN));
  }

  @Test(expected = JsonParseException.class)
  public void illegalBoolean() throws Exception {
    formatParser.parse(buildJson("\"Boolean\": undefined"));
  }

  @Test(expected = JsonParseException.class)
  public void illegalBooleanFormat() throws Exception {
    formatParser.parse(buildJson("\"Boolean\": \"false\""));
  }

  @Test
  public void string() throws Exception {
    assertThat((String) formatParser.parse(buildJson("\"String\": \"<\\\"\\t€\\\">\"")).getEntity().getProperty(STRING_NAME).getValue(), is(STRING));
  }

  @Test(expected = JsonParseException.class)
  public void illegalStringType() throws Exception {
    formatParser.parse(buildJson("\"String\": 123"));
  }

  @Test
  public void guid() throws Exception {
    assertThat((Guid) formatParser.parse(buildJson("\"Guid\": \"4786c33c-1e3d-4b57-b5cf-a4b759acac44\"")).getEntity().getProperty(GUID_NAME).getValue(), is(GUID));
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalGuid() throws Exception {
    formatParser.parse(buildJson("\"Guid\": \"a-b-c-d\""));
  }

  @Test
  public void decimal() throws Exception {
    assertThat((BigDecimal) formatParser.parse(buildJson("\"Decimal\": \"-12345.67890\"")).getEntity().getProperty(DECIMAL_NAME).getValue(), is(DECIMAL));
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalDecimal() throws Exception {
    formatParser.parse(buildJson("\"Decimal\": \"1eE+01\""));
  }

  @Test
  public void doubleWithExponent() throws Exception {
    assertThat((Double) formatParser.parse(buildJson("\"Double\": \"-1.23456789E-10\"")).getEntity().getProperty(DOUBLE_NAME).getValue(), is(DOUBLE));
  }

  @Test
  public void int16() throws Exception {
    assertThat((Short) formatParser.parse(buildJson("\"Int16\": -32768")).getEntity().getProperty(INT16_NAME).getValue(), is(Short.MIN_VALUE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void int16TooLarge() throws Exception {
    formatParser.parse(buildJson("\"Int16\": 32768"));
  }

  private StringReader buildJson(String property) {
    return new StringReader("" +
        "{" +
        "\"d\" : {" + property + "}" +
        "}");
  }
}
