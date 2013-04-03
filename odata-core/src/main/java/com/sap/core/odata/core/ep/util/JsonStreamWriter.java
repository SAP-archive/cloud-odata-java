package com.sap.core.odata.core.ep.util;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

/**
 * Writes JSON output.
 * @author SAP AG
 */
public class JsonStreamWriter {
  private static final Pattern JSON_TO_BE_ESCAPED = Pattern.compile("\"|\\\\|\\p{Cntrl}");

  public static void beginObject(final Writer writer) throws IOException {
    writer.append('{');
  }

  public static void endObject(final Writer writer) throws IOException {
    writer.append('}');
  }

  public static void beginArray(final Writer writer) throws IOException {
    writer.append('[');
  }

  public static void endArray(final Writer writer) throws IOException {
    writer.append(']');
  }

  public static void name(final Writer writer, final String name) throws IOException {
    stringValue(writer, name);
    writer.append(':');
  }

  public static void stringValue(final Writer writer, final String value) throws IOException {
    if (value == null) {
      writer.append("null");
    } else {
      writer.append('"');
      escape(writer, value);
      writer.append('"');
    }
  }

  public static void namedStringValue(final Writer writer, final String name, final String value) throws IOException {
    name(writer, name);
    stringValue(writer, value);
  }

  public static void namedNumberValue(final Writer writer, final String name, final String number) throws IOException {
    name(writer, name);
    writer.append(number == null ? "null" : number);
  }

  public static void namedBooleanValue(final Writer writer, final String name, final Boolean value) throws IOException {
    name(writer, name);
    writer.append(value == null ? "null" : value ? "true" : "false");
  }

  public static void separator(final Writer writer) throws IOException {
    writer.append(',');
  }

  /**
   * Writes the JSON-escaped form of a Java String value according to RFC 4627.
   * @param writer the writer the JSON-formatted String is written to
   * @param value the Java String
   * @throws IOException if an I/O error occurs
   */
  protected static void escape(final Writer writer, final String value) throws IOException {
    // RFC 4627 says: "All Unicode characters may be placed within the
    // quotation marks except for the characters that must be escaped:
    // quotation mark, reverse solidus, and the control characters
    // (U+0000 through U+001F)."
    if (JSON_TO_BE_ESCAPED.matcher(value).find()) {
      for (int i = 0; i < value.length(); i++) {
        final char c = value.charAt(i);
        if (c == '\\') {
          writer.append("\\\\");
        } else if (c == '"') {
          writer.append("\\\"");
        } else if (c <= '\u001F') {
          switch (c) {
          case '\b':
            writer.append("\\b");
            break;
          case '\t':
            writer.append("\\t");
            break;
          case '\n':
            writer.append("\\n");
            break;
          case '\f':
            writer.append("\\f");
            break;
          case '\r':
            writer.append("\\r");
            break;
          default:
            writer.append(String.format("\\u%04X", (short) c));
          }
        } else {
          writer.write(c);
        }
      }
    } else {
      writer.append(value);
    }
  }
}
