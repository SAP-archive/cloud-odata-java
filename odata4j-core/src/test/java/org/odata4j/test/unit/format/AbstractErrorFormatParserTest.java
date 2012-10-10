package org.odata4j.test.unit.format;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.junit.Test;
import org.odata4j.core.OError;
import org.odata4j.format.FormatParser;

public abstract class AbstractErrorFormatParserTest {

  protected static FormatParser<OError> formatParser;

  @Test
  public void error() throws Exception {
    OError error = formatParser.parse(buildError("error code", "error message", "inner error"));
    assertThat(error, notNullValue());
    assertThat(error.getCode(), is("error code"));
    assertThat(error.getMessage(), is("error message"));
    assertThat(error.getInnerError(), is("inner error"));
  }

  @Test
  public void errorWithoutInnerError() throws Exception {
    OError error = formatParser.parse(buildError("error code", "error message", null));
    assertThat(error, notNullValue());
    assertThat(error.getCode(), is("error code"));
    assertThat(error.getMessage(), is("error message"));
    assertThat(error.getInnerError(), nullValue());
  }

  @Test
  public void noCodeError() throws Exception {
    OError error = formatParser.parse(buildError(null, "error message", "inner error"));
    assertThat(error, notNullValue());
    assertThat(error.getCode(), nullValue());
    assertThat(error.getMessage(), is("error message"));
    assertThat(error.getInnerError(), is("inner error"));
  }

  @Test
  public void onlyMessageError() throws Exception {
    OError error = formatParser.parse(buildError(null, "error message", null));
    assertThat(error, notNullValue());
    assertThat(error.getCode(), nullValue());
    assertThat(error.getMessage(), is("error message"));
    assertThat(error.getInnerError(), nullValue());
  }

  @Test
  public void onlyCodeError() throws Exception {
    OError error = formatParser.parse(buildError("error code", null, null));
    assertThat(error, notNullValue());
    assertThat(error.getCode(), is("error code"));
    assertThat(error.getMessage(), nullValue());
    assertThat(error.getInnerError(), nullValue());
  }

  protected abstract StringReader buildError(String code, String message, String innerError);
}
