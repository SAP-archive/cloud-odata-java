package org.odata4j.test.unit.format.json;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.format.FormatType;
import org.odata4j.producer.Responses;
import org.odata4j.test.unit.format.AbstractPropertyFormatWriterTest;

public class JsonPropertyFormatWriterTest extends AbstractPropertyFormatWriterTest {

  @BeforeClass
  public static void setupClass() throws Exception {
    createFormatWriter(FormatType.JSON);
  }

  @Test
  public void dateTime() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME));
    assertThat(stringWriter.toString(), containsString("\"\\/Date(1112490120000)\\/\""));
  }

  @Test
  public void dateTimeWithSeconds() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_SECONDS));
    assertThat(stringWriter.toString(), containsString("\"\\/Date(1146704523000)\\/\""));
  }

  @Test
  public void dateTimeWithMillis() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_MILLIS));
    assertThat(stringWriter.toString(), containsString("\"\\/Date(1181005323004)\\/\""));
  }

  @Test
  public void dateTimeNoOffset() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_BEFORE_1970_NO_OFFSET));
    assertThat(stringWriter.toString(), containsString("\"\\/Date(-12682440000+0000)\\/\""));
  }

  @Test
  public void dateTimeWithSecondsPositiveOffset() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_SECONDS_POSITIVE_OFFSET));
    assertThat(stringWriter.toString(), containsString("\"\\/Date(1146654123000+0420)\\/\""));
  }

  @Test
  public void dateTimeWithMillisNegativeOffset() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DATETIME_WITH_MILLIS_NEGATIVE_OFFSET));
    assertThat(stringWriter.toString(), containsString("\"\\/Date(1181062923004-0480)\\/\""));
  }

  @Test
  public void time() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(TIME));
    assertThat(stringWriter.toString(), containsString("\"PT1H2M3S\""));
  }

  @Test
  public void timeWithMillis() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(TIME_WITH_MILLIS));
    assertThat(stringWriter.toString(), containsString("\"PT1H2M3.004S\""));
  }

  @Test
  public void bool() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(BOOLEAN_PROPERTY));
    assertTrue(Pattern.compile(".+\\{\\s*\"Boolean\"\\s*:\\s*false\\s*\\}.+", Pattern.DOTALL)
        .matcher(stringWriter.toString()).matches());
  }

  @Test
  public void string() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(STRING_PROPERTY));
    assertThat(stringWriter.toString(), containsString("\"<\\\"\\t€\\\">\""));
  }

  @Test
  public void guid() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(GUID_PROPERTY));
    assertThat(stringWriter.toString(), containsString("\"4786c33c-1e3d-4b57-b5cf-a4b759acac44\""));
  }

  @Test
  public void decimal() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DECIMAL_PROPERTY));
    assertThat(stringWriter.toString(), containsString("\"-12345.67890\""));
  }

  @Test
  public void decimalLarge() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DECIMAL_PROPERTY_LARGE));
    assertThat(stringWriter.toString(), containsString("\"12345678901234567890\""));
  }

  @Test
  public void doubleWithExponent() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(DOUBLE_PROPERTY));
    assertThat(stringWriter.toString(), containsString("\"-1.23456789E-10\""));
  }

  @Test
  public void int16() throws Exception {
    formatWriter.write(null, stringWriter, Responses.property(INT16_PROPERTY));
    assertTrue(Pattern.compile(".+\\{\\s*\"Int16\"\\s*:\\s*-32768\\s*\\}.+", Pattern.DOTALL)
        .matcher(stringWriter.toString()).matches());
  }
}
