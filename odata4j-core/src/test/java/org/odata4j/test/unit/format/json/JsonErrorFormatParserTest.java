package org.odata4j.test.unit.format.json;

import java.io.StringReader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.core.OError;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.FormatType;
import org.odata4j.test.unit.format.AbstractErrorFormatParserTest;

public class JsonErrorFormatParserTest extends AbstractErrorFormatParserTest {

  @BeforeClass
  public static void setupClass() throws Exception {
    formatParser = FormatParserFactory.getParser(OError.class, FormatType.JSON, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyError() throws Exception {
    formatParser.parse(new StringReader("{ \"error\": \"wrong error format\"}"));
  }

  @Override
  protected StringReader buildError(String code, String message, String innerError) {
    return new StringReader("{ \"error\": { "
        + (code == null ? "" : "\"code\": \"" + code + "\", ")
        + "\"message\": { \"lang\": \"en-US\""
        + (message == null ? "" : ", \"value\": \"" + message + "\"")
        + "}"
        + (innerError == null ? "" : ", \"innererror\": \"" + innerError + "\"")
        + "}}");
  }
}
