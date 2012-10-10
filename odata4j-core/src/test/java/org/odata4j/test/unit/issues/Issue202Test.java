package org.odata4j.test.unit.issues;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.core.OError;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.FormatType;

// http://code.google.com/p/odata4j/issues/detail?id=202
public class Issue202Test {

  @Test
  public void issue202() {
    InputStream errorStream = getClass().getResourceAsStream("/META-INF/issue202_complex_innererror.xml");

    OError error = FormatParserFactory.getParser(OError.class, FormatType.ATOM, null).parse(new InputStreamReader(errorStream));
    Assert.assertEquals("", error.getCode());
    Assert.assertEquals("An error occurred while processing this request.", error.getMessage());
    Assert.assertTrue(error.getInnerError().contains("<type>System.InvalidOperationException</type>"));
  }

}
