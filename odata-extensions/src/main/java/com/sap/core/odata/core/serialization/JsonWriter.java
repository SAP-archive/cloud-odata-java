package com.sap.core.odata.core.serialization;

import java.io.IOException;
import java.io.Writer;

import com.sap.core.odata.api.annotations.EdmProperty;
import com.sap.core.odata.api.annotations.EdmTypeKind;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class JsonWriter {

  private final Writer writer;
  private boolean firstProperty = true;

  public JsonWriter(Writer writer) {
    this.writer = writer;
  }

  public void startCallback(String functionName) {
    try {
      writer.write(encode(functionName) + "(");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void endCallback() {
    try {
      writer.write(");");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void startObject() {
    try {
      firstProperty = true;
      writer.write("{\n");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void endObject() {
    try {
      firstProperty = false;
      writer.write("\n}");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void writeName(String name) {
    try {
      writer.write("\"" + encode(name) + "\" : ");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void startArray() {
    try {
      writer.write("[\n");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void endArray() {
    try {
      writer.write("\n]");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void writeSeparator() {
    try {
      writer.write(", ");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void writeString(String value) {
    try {
      writer.write("\"" + encode(value) + "\"");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void writeNull() {
    try {
      writer.write("null");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void writeNumber(int value) {
    try {
      writer.write(Integer.toString(value));
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
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
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void writeBoolean(boolean value) {
    try {
      writer.write(value ? "true" : "false");
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
    }
  }

  public void writeProperty(String key, Object value) {
    writeProperty(key, value, EdmTypeKind.STRING);
  }

  public void writeEdmProperty(EdmProperty property, Object value) {
    writeProperty(property.name(), value, property.type().value());
  }
  
  private void writeProperty(String key, Object value, EdmTypeKind type) {
    if(firstProperty) {
      firstProperty = false;
    } else {
      writeSeparator();
    }

    // write content
    this.writeName(key);
    if(value == null) {
      this.writeNull();
    } else {
      
      try {
        this.writeEdmType(value, type);
      } catch(Exception e) {
        writeString("exception for key '" + key +
        		"' with message '" + e.getMessage() +
        		"' and toString '" + value.toString() + "'.");      
      }
    }
  }
  
  private void writeEdmType(Object value, EdmTypeKind type) {
      
      switch (type) {
        case INT16:
          writeNumber(((Integer)value).intValue());
          break;
        case STRING:
          writeString((String)value);
          break;
        default:
          writeString("toString = " + value.toString());
          break;
      }
  }

  public void writeRaw(String value) {
    try {
      writer.write(value);
    } catch (IOException e) {
      throw new ODataRuntimeException(e.getMessage(), e);
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
