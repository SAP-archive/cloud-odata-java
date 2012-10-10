package org.odata4j.test.unit.format.xml;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.format.FormatType;
import org.odata4j.producer.Responses;
import org.odata4j.test.unit.format.AbstractPropertyFormatWriterTest;

@SuppressWarnings("unchecked")
public class AtomPropertyFormatWriterTest extends AbstractPropertyFormatWriterTest {

  @BeforeClass
  public static void setupClass() throws Exception {
    createFormatWriter(FormatType.ATOM);
  }

  @Test
  public void dateTime() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.DateTime\""), containsString(">2005-04-03T01:02<")));
  }

  @Test
  public void dateTimeWithSeconds() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_SECONDS));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.DateTime\""), containsString(">2006-05-04T01:02:03<")));
  }

  @Test
  public void dateTimeWithMillis() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_MILLIS));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.DateTime\""), containsString(">2007-06-05T01:02:03.004<")));
  }

  @Test
  public void dateTimeNoOffset() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_BEFORE_1970_NO_OFFSET));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.DateTimeOffset\""), containsString(">1969-08-07T05:06:00Z<")));
  }

  @Test
  public void dateTimeWithSecondsPositiveOffset() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_SECONDS_POSITIVE_OFFSET));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.DateTimeOffset\""), containsString(">2006-05-04T01:02:03+07:00<")));
  }

  @Test
  public void dateTimeWithMillisNegativeOffset() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_MILLIS_NEGATIVE_OFFSET));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.DateTimeOffset\""), containsString(">2007-06-05T01:02:03.004-08:00<")));
  }

  @Test
  public void time() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(TIME));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.Time\""), containsString(">PT1H2M3S<")));
  }

  @Test
  public void timeWithMillis() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(TIME_WITH_MILLIS));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.Time\""), containsString(">PT1H2M3.004S<")));
  }

  @Test
  public void bool() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(BOOLEAN_PROPERTY));
    assertThat(stringWriter.toString(), allOf(containsString("m:type=\"Edm.Boolean\""), containsString(">false<")));
  }

  @Test
  public void string() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(STRING_PROPERTY));
    assertThat(stringWriter.toString(), containsString(">&lt;\"\t€\"&gt;<"));
  }

  @Test
  public void guid() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(GUID_PROPERTY));
    assertThat(stringWriter.toString(), containsString(">4786c33c-1e3d-4b57-b5cf-a4b759acac44<"));
  }

  @Test
  public void decimal() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DECIMAL_PROPERTY));
    assertThat(stringWriter.toString(), containsString(">-12345.67890<"));
  }

  @Test
  public void decimalLarge() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DECIMAL_PROPERTY_LARGE));
    assertThat(stringWriter.toString(), containsString(">12345678901234567890<"));
  }

  @Test
  public void doubleWithExponent() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DOUBLE_PROPERTY));
    assertThat(stringWriter.toString(), containsString(">-1.23456789E-10<"));
  }

  @Test
  public void int16() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(INT16_PROPERTY));
    assertThat(stringWriter.toString(), containsString(">-32768<"));
  }
}
