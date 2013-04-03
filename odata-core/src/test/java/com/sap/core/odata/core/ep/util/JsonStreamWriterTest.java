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
    JsonStreamWriter.beginArray(writer);
    JsonStreamWriter.beginObject(writer);
    JsonStreamWriter.name(writer, "value");
    JsonStreamWriter.stringValue(writer, "value");
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedBooleanValue(writer, "boolean", false);
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedBooleanValue(writer, "booleanTrue", true);
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedNumberValue(writer, "number", "42.42");
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedStringValue(writer, "string", "value");
    JsonStreamWriter.endObject(writer);
    JsonStreamWriter.endArray(writer);
    writer.flush();
    assertEquals("[{\"value\":\"value\",\"boolean\":false,\"booleanTrue\":true,"
        + "\"number\":42.42,\"string\":\"value\"}]",
        writer.toString());
  }

  @Test
  public void nullValues() throws Exception {
    StringWriter writer = new StringWriter();
    JsonStreamWriter.beginObject(writer);
    JsonStreamWriter.namedBooleanValue(writer, "boolean", null);
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedNumberValue(writer, "number", null);
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedStringValue(writer, "string", null);
    JsonStreamWriter.endObject(writer);
    writer.flush();
    assertEquals("{\"boolean\":null,\"number\":null,\"string\":null}", writer.toString());
  }

  @Test
  public void escape() throws Exception {
    final String outsideBMP = String.valueOf(Character.toChars(0x1F603));
    StringWriter writer = new StringWriter();
    JsonStreamWriter.beginObject(writer);
    JsonStreamWriter.namedStringValue(writer, "normal", "abc / ? \u007F € \uFDFC");
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedStringValue(writer, "outsideBMP", outsideBMP);
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedStringValue(writer, "control", "\b\t\n\f\r\u001F " + "€ \uFDFC " + outsideBMP);
    JsonStreamWriter.separator(writer);
    JsonStreamWriter.namedStringValue(writer, "escaped", "\"\\");
    JsonStreamWriter.endObject(writer);
    writer.flush();
    assertEquals("{\"normal\":\"abc / ? \u007F € \uFDFC\","
        + "\"outsideBMP\":\"\uD83D\uDE03\","
        + "\"control\":\"\\b\\t\\n\\f\\r\\u001F € \uFDFC \uD83D\uDE03\","
        + "\"escaped\":\"\\\"\\\\\"}",
        writer.toString());
  }
}
