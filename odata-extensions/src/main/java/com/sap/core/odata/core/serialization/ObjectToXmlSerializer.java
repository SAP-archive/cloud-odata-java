/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sap.core.odata.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.annotations.ODataSerializer;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * 
 * @author michael
 */
public class ObjectToXmlSerializer implements ODataSerializer<InputStream> {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectToXmlSerializer.class);
  
  private final Object toSerialize;

  public ObjectToXmlSerializer(Object object) {
    this.toSerialize = object;
  }

  public InputStream asStream() throws XMLStreamException, IllegalAccessException {
    String content = this.serialize();
    try {
      return new ByteArrayInputStream(content.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UTF-8 encoding should be supported.", e);
    }
  }

  public String serialize() throws XMLStreamException, IllegalAccessException {

    XMLOutputFactory output = XMLOutputFactory.newInstance();

    // XMLStreamWriter writer = output.createXMLStreamWriter(System.out);
    // XMLStreamWriter writer = output.createXMLStreamWriter(swriter);
    MyStringOutStream outStream = new MyStringOutStream();
    XMLStreamWriter writer = output.createXMLStreamWriter(outStream);

    writer.writeStartDocument();

    writeObjectToStream(writer);
    // writer.setPrefix("c", "http://c");
    // writer.setDefaultNamespace("http://c");
    // writer.writeStartElement("http://c", "a");
    // writer.writeAttribute("b", "blah");
    // writer.writeNamespace("c", "http://c");
    // writer.writeDefaultNamespace("http://c");
    // writer.setPrefix("d", "http://c");
    // writer.writeEmptyElement("http://c", "d");
    // writer.writeAttribute("http://c", "chris", "fry");
    // writer.writeNamespace("d", "http://c");
    // writer.writeCharacters("Jean Arp");

    writer.writeEndDocument();
    writer.flush();

    // return swriter.getBuffer().toString();
    return outStream.getContent();
  }

  private void writeObjectToStream(XMLStreamWriter writer) throws IllegalAccessException, XMLStreamException {
    ObjectHelper objHelper = ObjectHelper.init(toSerialize);
    writeToStream(writer, objHelper);
  }

  private void writeToStream(XMLStreamWriter writer, ObjectHelper object) throws IllegalAccessException, XMLStreamException {
    writeToStream(writer, object, 2);
  }
  
  private void writeToStream(XMLStreamWriter writer, ObjectHelper object, int depth) throws IllegalAccessException, XMLStreamException {
    Class<?> objClass = object.getObjectClass();
    writer.writeStartElement(objClass.getSimpleName());
    writer.writeAttribute("package", objClass.getPackage().getName());
    writer.writeStartElement("properties");

    Map<String, Object> objectValues = object.getFieldValues();

    Set<Entry<String, Object>> entries = objectValues.entrySet();
    for (Entry<String, Object> entry : entries) {
      writer.writeStartElement(entry.getKey());
      if (entry.getValue() == null) {
        writer.writeAttribute("isNull", "true");
      } else if(entry.getValue() instanceof ObjectHelper && depth > 0) {
        writeToStream(writer, (ObjectHelper) entry.getValue(), --depth);
      } else {
        String value = parse(entry.getValue());
        writer.writeCharacters(value);
      }
      writer.writeEndElement();
    }

    writer.writeEndElement();
    writer.writeEndElement();
  }

  private String parse(Object value) {
    if (value == null) {
      return "NULL";
    } else if (value instanceof String) {
      return (String) value;
    } else {
      return value.toString();
    }
  }

  private static class ObjectHelper {

    private final Object object;
    private final Map<String, Field> fieldsToIds;
    private Map<String, Object> fieldId2Value;

    private ObjectHelper(Object object) {
      this.object = object;

      fieldsToIds = mapFieldsToIds(object.getClass());
      fieldId2Value = new HashMap<String, Object>();
    }

    public Class<?> getObjectClass() {
      return this.object.getClass();
    }

    private ObjectHelper init() {

      return this;
    }

    public static ObjectHelper init(Object object) {
      return new ObjectHelper(object).init();
    }

    public Map<String, Object> getFieldValues() throws IllegalAccessException {
      fieldId2Value = mapFieldValueToId(object, fieldsToIds);

      return Collections.unmodifiableMap(fieldId2Value);
    }

    private Object getFieldValue(final Object inInstance, final Field field) throws IllegalAccessException {
      final boolean accessible = field.isAccessible();
      if (!accessible) {
        field.setAccessible(true);
      }
      final Object value = field.get(inInstance);
      if (!accessible) {
        field.setAccessible(false);
      }
      return value;
    }

    private Map<String, Field> mapFieldsToIds(final Class<?> clazz) {
      return mapFieldsToIds(clazz, true);
    }

    private Map<String, Field> mapFieldsToIds(final Class<?> clazz, boolean inherited) {
      final Map<String, Field> id2Field = new HashMap<String, Field>();

      final Field[] allFields = clazz.getDeclaredFields();

      for (final Field field : allFields) {
        final String id = field.getName();
        id2Field.put(id, field);
      }

      Class<?> superClazz = clazz.getSuperclass();
      if (inherited && superClazz != Object.class) {
        Map<String, Field> inheritedFields = mapFieldsToIds(superClazz, inherited);
        id2Field.putAll(inheritedFields);
      }

      return id2Field;
    }

    
    private Map<String, Object> mapFieldValueToId(Object object, Map<String, Field> fieldsToIds) throws IllegalAccessException {

      Map<String, Object> fieldId2FieldValue = new HashMap<String, Object>();
      Set<Entry<String, Field>> entries = fieldsToIds.entrySet();

      for (Entry<String, Field> entry : entries) {
        Object value = getFieldValue(object, entry.getValue());
        if(isComplexType(entry.getValue().getType())) {
          value = ObjectHelper.init(value);
        } 
//        else {
//          value = getFieldValue(object, entry.getValue());
//        }
        fieldId2FieldValue.put(entry.getKey(), value);
      }

      return fieldId2FieldValue;
    }

    private boolean isComplexType(Class<?> type) {
      boolean isComplex = true;
      if(type == String.class
          || type == Integer.class
          || type == Boolean.class
          || type == Date.class
          || type == Float.class
          || type == Object.class
          || type.isAssignableFrom(Collection.class)
          || type.isArray()
          || type.isPrimitive()) {
        isComplex = false;
      }
      LOG.debug("Class {} is complex? -> {}", type.getSimpleName(), isComplex);
      return isComplex;
    }
  }

  private static class MyStringOutStream extends OutputStream {

    private StringBuilder content = new StringBuilder();

    @Override
    public void write(int b) throws IOException {
      content.append((char) b);
    }

    public String getContent() {
      return content.toString();
    }
  }

  @Override
  public InputStream serialize(Object obj) {
    try {
      return new ObjectToXmlSerializer(obj).asStream();
    } catch (Exception e) {
      throw new ODataRuntimeException("Exception during serialization.", e);
    }
  }
}
