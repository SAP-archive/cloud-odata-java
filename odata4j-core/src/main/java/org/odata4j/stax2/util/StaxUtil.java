package org.odata4j.stax2.util;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.odata4j.stax2.Attribute2;
import org.odata4j.stax2.Characters2;
import org.odata4j.stax2.EndElement2;
import org.odata4j.stax2.StartElement2;
import org.odata4j.stax2.XMLEvent2;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.XMLEventWriter2;
import org.odata4j.stax2.XMLFactoryProvider2;
import org.odata4j.stax2.XMLInputFactory2;
import org.odata4j.stax2.XMLOutputFactory2;
import org.odata4j.stax2.XMLWriter2;
import org.odata4j.stax2.XMLWriterFactory2;

public class StaxUtil {

  public static XMLEventReader2 newXMLEventReader(Reader reader) {
    XMLInputFactory2 f = XMLFactoryProvider2.getInstance()
        .newXMLInputFactory2();
    return f.createXMLEventReader(reader);
  }

  public static XMLEventWriter2 newXMLEventWriter(Writer writer) {
    XMLOutputFactory2 f = XMLFactoryProvider2.getInstance()
        .newXMLOutputFactory2();
    return f.createXMLEventWriter(writer);
  }

  public static XMLWriter2 newXMLWriter(Writer writer) {
    XMLWriterFactory2 f = XMLFactoryProvider2.getInstance()
        .newXMLWriterFactory2();
    return f.createXMLWriter(writer);
  }

  public static String outerXml(XMLEvent2 currentEvent, XMLEventReader2 xmlReader) {
    StringWriter writer = new StringWriter();
    XMLWriter2 xmlWriter = newXMLWriter(writer);
    outerXml(currentEvent, xmlReader, xmlWriter);
    return writer.toString();
  }

  public static void outerXml(XMLEvent2 currentEvent, XMLEventReader2 xmlReader, XMLWriter2 xmlWriter) {
    writeXml(true, currentEvent, xmlReader, xmlWriter);
  }

  public static String innerXml(XMLEvent2 currentEvent, XMLEventReader2 xmlReader) {
    StringWriter writer = new StringWriter();
    XMLWriter2 xmlWriter = newXMLWriter(writer);
    innerXml(currentEvent, xmlReader, xmlWriter);
    return writer.toString();
  }

  public static void innerXml(XMLEvent2 currentEvent, XMLEventReader2 xmlReader, XMLWriter2 xmlWriter) {
    writeXml(false, currentEvent, xmlReader, xmlWriter);
  }

  private static void writeXml(boolean includeCurrent, XMLEvent2 currentEvent, XMLEventReader2 xmlReader, XMLWriter2 xmlWriter) {
    if (currentEvent == null || !currentEvent.isStartElement())
      throw new IllegalStateException("currentEvent must be a start element");
    if (includeCurrent)
      writeStartElement(currentEvent.asStartElement(), xmlWriter);
    int level = 0;
    while (true) {
      currentEvent = xmlReader.nextEvent();
      if (currentEvent.isStartElement()) {
        writeStartElement(currentEvent.asStartElement(), xmlWriter);
        level++;
      } else if (currentEvent.isEndElement()) {
        if (level == 0)
          break;
        writeEndElement(currentEvent.asEndElement(), xmlWriter);
        level--;
      } else if (currentEvent.isCharacters()) {
        writeCharacters(currentEvent.asCharacters(), xmlWriter);
      }
    }
    if (includeCurrent)
      writeEndElement(currentEvent.asEndElement(), xmlWriter);
  }

  private static void writeStartElement(StartElement2 start, XMLWriter2 xmlWriter) {
    xmlWriter.startElement(start.getName());
    for (Attribute2 attribute : start.getAttributes()) {
      xmlWriter.writeAttribute(attribute.getName(), attribute.getValue());
    }
  }

  private static void writeEndElement(EndElement2 end, XMLWriter2 xmlWriter) {
    xmlWriter.endElement(end.getName().getLocalPart());
  }

  private static void writeCharacters(Characters2 characters, XMLWriter2 xmlWriter) {
    xmlWriter.writeText(characters.getData());
  }

}
