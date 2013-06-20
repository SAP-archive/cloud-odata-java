package com.sap.core.odata.core.ep.util;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes JSON output.
 * @author SAP AG
 */
public class JsonStreamWriter {
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
    writer.append('"').append(name).append('"').append(':');
  }

  public void unquotedValue(final String value) throws IOException {
    writer.append(value == null ? FormatJson.NULL : value);
  }

  public void stringValueRaw(final String value) throws IOException {
    if (value == null)
      writer.append(FormatJson.NULL);
    else
      writer.append('"').append(value).append('"');
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
    // All output here is done on character basis which should be faster
    // than writing Strings.
    for (int i = 0; i < value.length(); i++) {
      final char c = value.charAt(i);
      switch (c) {
      case '\\':
        writer.append('\\').append(c);
        break;
      case '"':
        writer.append('\\').append(c);
        break;
      case '\b':
        writer.append('\\').append('b');
        break;
      case '\t':
        writer.append('\\').append('t');
        break;
      case '\n':
        writer.append('\\').append('n');
        break;
      case '\f':
        writer.append('\\').append('f');
        break;
      case '\r':
        writer.append('\\').append('r');
        break;
      case '\u0000':
      case '\u0001':
      case '\u0002':
      case '\u0003':
      case '\u0004':
      case '\u0005':
      case '\u0006':
      case '\u0007':
      case '\u000B':
      case '\u000E':
      case '\u000F':
      case '\u0010':
      case '\u0011':
      case '\u0012':
      case '\u0013':
      case '\u0014':
      case '\u0015':
      case '\u0016':
      case '\u0017':
      case '\u0018':
      case '\u0019':
      case '\u001A':
      case '\u001B':
      case '\u001C':
      case '\u001D':
      case '\u001E':
      case '\u001F':
        final int lastHexDigit = c % 0x10;
        writer.append('\\').append('u').append('0').append('0')
            .append(c >= '\u0010' ? '1' : '0')
            .append((char) ((lastHexDigit > 9 ? 'A' : '0') + lastHexDigit % 10));
        break;
      default:
        writer.append(c);
      }
    }
  }
}
