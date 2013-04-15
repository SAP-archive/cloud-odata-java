/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.util;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class JsonStreamWriterTest extends BaseTest {

  @Test
  public void basic() throws Exception {
    StringWriter writer = new StringWriter();
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    jsonStreamWriter.beginArray();
    jsonStreamWriter.beginObject();
    jsonStreamWriter.name("value");
    jsonStreamWriter.stringValue("value");
    jsonStreamWriter.separator();
    jsonStreamWriter.name("boolean");
    jsonStreamWriter.unquotedValue(FormatJson.FALSE);
    jsonStreamWriter.separator();
    jsonStreamWriter.name("booleanTrue");
    jsonStreamWriter.unquotedValue(FormatJson.TRUE);
    jsonStreamWriter.separator();
    jsonStreamWriter.name("number");
    jsonStreamWriter.unquotedValue("42.42");
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValue("string", "value");
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValueRaw("string raw", "1");
    jsonStreamWriter.endObject();
    jsonStreamWriter.endArray();
    writer.flush();
    assertEquals("[{\"value\":\"value\",\"boolean\":false,\"booleanTrue\":true,"
        + "\"number\":42.42,\"string\":\"value\",\"string raw\":\"1\"}]",
        writer.toString());
  }

  @Test
  public void nullValues() throws Exception {
    StringWriter writer = new StringWriter();
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    jsonStreamWriter.beginObject();
    jsonStreamWriter.name("number");
    jsonStreamWriter.unquotedValue(null);
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValue("string", null);
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValueRaw("raw", null);
    jsonStreamWriter.endObject();
    writer.flush();
    assertEquals("{\"number\":null,\"string\":null,\"raw\":null}", writer.toString());
  }

  @Test
  public void escape() throws Exception {
    final String outsideBMP = String.valueOf(Character.toChars(0x1F603));
    StringWriter writer = new StringWriter();
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    jsonStreamWriter.beginObject();
    jsonStreamWriter.namedStringValue("normal", "abc / ? \u007F € \uFDFC");
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValue("outsideBMP", outsideBMP);
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValue("control", "\b\t\n\f\r\u001F " + "€ \uFDFC " + outsideBMP);
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValue("escaped", "\"\\");
    jsonStreamWriter.endObject();
    writer.flush();
    assertEquals("{\"normal\":\"abc / ? \u007F € \uFDFC\","
        + "\"outsideBMP\":\"\uD83D\uDE03\","
        + "\"control\":\"\\b\\t\\n\\f\\r\\u001F € \uFDFC \uD83D\uDE03\","
        + "\"escaped\":\"\\\"\\\\\"}",
        writer.toString());
  }
}
