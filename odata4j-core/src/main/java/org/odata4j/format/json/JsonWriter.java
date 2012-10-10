package org.odata4j.format.json;

import java.io.IOException;
import java.io.Writer;

import org.odata4j.core.Throwables;

public class JsonWriter {

  private final Writer writer;

  public JsonWriter(Writer writer) {
    this.writer = writer;
  }

  public void startCallback(String functionName) {
    try {
      writer.write(encode(functionName) + "(");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void endCallback() {
    try {
      writer.write(");");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void startObject() {
    try {
      writer.write("{\n");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void endObject() {
    try {
      writer.write("\n}");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeName(String name) {
    try {
      writer.write("\"" + encode(name) + "\" : ");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void startArray() {
    try {
      writer.write("[\n");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void endArray() {
    try {
      writer.write("\n]");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeSeparator() {
    try {
      writer.write(", ");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeString(String value) {
    try {
      writer.write("\"" + encode(value) + "\"");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeNull() {
    try {
      writer.write("null");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeNumber(int value) {
    try {
      writer.write(Integer.toString(value));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeNumber(float value) {
    try {
      String fvalue = Float.toString(value);
      while (fvalue.contains(".") && fvalue.endsWith("0"))
        fvalue = fvalue.substring(0, fvalue.length() - 1);
      if (fvalue.endsWith("."))
        fvalue = fvalue.substring(0, fvalue.length() - 1);
      writer.write(fvalue);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeBoolean(boolean value) {
    try {
      writer.write(value ? "true" : "false");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void writeRaw(String value) {
    try {
      writer.write(value);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private String encode(String unencoded) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < unencoded.length(); i++) {
      char c = unencoded.charAt(i);
      if (c == '\\')
        sb.append("\\\\");
      else if (c == '"')
        sb.append("\\\"");
      else if (c == '\n')
        sb.append("\\n");
      else if (c == '\r')
        sb.append("\\r");
      else if (c == '\f')
        sb.append("\\f");
      else if (c == '\b')
        sb.append("\\b");
      else if (c == '\t')
        sb.append("\\t");

      else
        sb.append(c);
    }
    return sb.toString();
  }

}
