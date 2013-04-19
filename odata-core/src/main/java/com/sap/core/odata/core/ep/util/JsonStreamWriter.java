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

  private final Writer writer;

  public JsonStreamWriter(final Writer writer) {
    this.writer = writer;
  }

  public void beginObject() throws IOException {
    writer.append('{');
  }

  public void endObject() throws IOException {
    writer.append('}');
  }

  public void beginArray() throws IOException {
    writer.append('[');
  }

  public void endArray() throws IOException {
    writer.append(']');
  }

  public void name(final String name) throws IOException {
    writer.append('"');
    writer.append(name);
    writer.append('"');
    writer.append(':');
  }

  public void unquotedValue(final String value) throws IOException {
    writer.append(value == null ? FormatJson.NULL : value);
  }

  public void stringValueRaw(final String value) throws IOException {
    if (value == null) {
      writer.append(FormatJson.NULL);
    } else {
      writer.append('"');
      writer.append(value);
      writer.append('"');
    }
  }

  public void stringValue(final String value) throws IOException {
    if (value == null) {
      writer.append(FormatJson.NULL);
    } else {
      writer.append('"');
      escape(value);
      writer.append('"');
    }
  }

  public void namedStringValueRaw(final String name, final String value) throws IOException {
    name(name);
    stringValueRaw(value);
  }

  public void namedStringValue(final String name, final String value) throws IOException {
    name(name);
    stringValue(value);
  }

  public void separator() throws IOException {
    writer.append(',');
  }

  /**
   * Writes the JSON-escaped form of a Java String value according to RFC 4627.
   * @param value the Java String
   * @throws IOException if an I/O error occurs
   */
  protected void escape(final String value) throws IOException {
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
