package org.odata4j.test.unit.format.json;

import org.junit.BeforeClass;
import org.odata4j.format.FormatType;
import org.odata4j.test.unit.format.AbstractErrorFormatWriterTest;

public class JsonErrorFormatWriterTest extends AbstractErrorFormatWriterTest {

  @BeforeClass
  public static void setupClass() throws Exception {
    createFormatWriter(FormatType.JSON);
  }

  @Override
  protected String buildRegex(String code, String message, String innerError) {
    StringBuilder regex = new StringBuilder();
    regex.append("\\{\\s*\"error\"\\s*:\\s*\\{\\s*");
    regex.append("\"code\"\\s*:\\s*\"" + code + "\"\\s*,\\s*");
    regex.append("\"message\"\\s*:\\s*\\{\\s*");
    regex.append("\"lang\"\\s*:\\s*\".+\"\\s*,\\s*");
    regex.append("\"value\"\\s*:\\s*\"" + message + "\"\\s*");
    regex.append("\\}\\s*");
    if (innerError != null)
      regex.append(",\\s*\"innererror\"\\s*:\\s*\"" + innerError + "\"\\s*");
    regex.append("\\}\\s*");
    regex.append("\\}");
    return regex.toString();
  }
}
