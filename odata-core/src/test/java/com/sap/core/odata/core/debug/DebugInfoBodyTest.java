package com.sap.core.odata.core.debug;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * 
 * @author SAP AG
 */
public class DebugInfoBodyTest {

  private static final String STRING_CONTENT = "StringContent";
  private static final String STRING_CONTENT_JSON = "\"" + STRING_CONTENT + "\"";

  ODataResponse response = mock(ODataResponse.class);
  DebugInfoBody dib = new DebugInfoBody(response);

  @Test
  public void testAppendJsonNoContent() throws Exception {
    when(response.getEntity()).thenReturn(STRING_CONTENT);

    assertEquals("null", appendJson());
  }

  @Test
  public void testAppendJsonStringContent() throws Exception {
    when(response.getEntity()).thenReturn(STRING_CONTENT);
    when(response.getContentHeader()).thenReturn("text/html");

    assertEquals(STRING_CONTENT_JSON, appendJson());
  }

  @Test
  public void testAppendJsonInputStreamContent() throws Exception {
    ByteArrayInputStream in = new ByteArrayInputStream(STRING_CONTENT.getBytes());
    when(response.getEntity()).thenReturn(in);
    when(response.getContentHeader()).thenReturn("text/html");

    assertEquals(STRING_CONTENT_JSON, appendJson());
  }

  @Test(expected = ClassCastException.class)
  public void testAppendJsonUnsupportedContent() throws Exception {
    when(response.getEntity()).thenReturn(new Object());
    when(response.getContentHeader()).thenReturn("text/html");

    appendJson();
  }

  private String appendJson() throws IOException {
    Writer writer = new StringWriter();
    JsonStreamWriter jsw = new JsonStreamWriter(writer);
    dib.appendJson(jsw);
    return writer.toString();
  }

}
