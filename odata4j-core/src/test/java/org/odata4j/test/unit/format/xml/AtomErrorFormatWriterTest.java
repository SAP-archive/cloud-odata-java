package org.odata4j.test.unit.format.xml;

import org.junit.BeforeClass;
import org.odata4j.format.FormatType;
import org.odata4j.test.unit.format.AbstractErrorFormatWriterTest;

public class AtomErrorFormatWriterTest extends AbstractErrorFormatWriterTest {

  @BeforeClass
  public static void setupClass() throws Exception {
    createFormatWriter(FormatType.ATOM);
  }

  @Override
  protected String buildRegex(String code, String message, String innerError) {
    StringBuilder regex = new StringBuilder();
    regex.append(".*<error xmlns=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\">\\s*");
    regex.append("<code>" + code + "</code>\\s*");
    regex.append("<message lang=\".+\">" + message + "</message>\\s*");
    if (innerError != null)
      regex.append("<innererror>" + innerError + "</innererror>\\s*");
    regex.append("</error>\\s*");
    return regex.toString();
  }
}
