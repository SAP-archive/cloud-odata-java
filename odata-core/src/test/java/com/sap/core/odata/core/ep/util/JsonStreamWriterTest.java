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
    jsonStreamWriter.beginArray()
        .beginObject()
        .name("value").stringValue("value").separator()
        .name("boolean").unquotedValue(FormatJson.FALSE).separator()
        .name("booleanTrue").unquotedValue(FormatJson.TRUE).separator()
        .name("number").unquotedValue("42.42").separator()
        .namedStringValue("string", "value").separator()
        .namedStringValueRaw("string raw", "1")
        .endObject()
        .endArray();
    writer.flush();
    assertEquals("[{\"value\":\"value\",\"boolean\":false,\"booleanTrue\":true,"
        + "\"number\":42.42,\"string\":\"value\",\"string raw\":\"1\"}]",
        writer.toString());
  }

  @Test
  public void nullValues() throws Exception {
    StringWriter writer = new StringWriter();
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    jsonStreamWriter.beginObject()
        .name("number").unquotedValue(null).separator()
        .namedStringValue("string", null).separator().namedStringValueRaw("raw", null);
    jsonStreamWriter.endObject();
    writer.flush();
    assertEquals("{\"number\":null,\"string\":null,\"raw\":null}", writer.toString());
  }

  @Test
  public void escape() throws Exception {
    final String outsideBMP = String.valueOf(Character.toChars(0x1F603));
    StringWriter writer = new StringWriter();
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    jsonStreamWriter.beginObject()
        .namedStringValue("normal", "abc / ? \u007F € \uFDFC").separator()
        .namedStringValue("outsideBMP", outsideBMP).separator()
        .namedStringValue("control", "\b\t\n\f\r\u0001\u000B\u0011\u001F " + "€ \uFDFC " + outsideBMP).separator()
        .namedStringValue("escaped", "\"\\")
        .endObject();
    writer.flush();
    assertEquals("{\"normal\":\"abc / ? \u007F € \uFDFC\","
        + "\"outsideBMP\":\"\uD83D\uDE03\","
        + "\"control\":\"\\b\\t\\n\\f\\r\\u0001\\u000B\\u0011\\u001F € \uFDFC \uD83D\uDE03\","
        + "\"escaped\":\"\\\"\\\\\"}",
        writer.toString());
  }
}
