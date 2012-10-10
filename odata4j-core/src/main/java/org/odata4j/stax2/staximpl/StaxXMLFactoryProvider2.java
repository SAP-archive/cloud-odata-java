package org.odata4j.stax2.staximpl;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.core4j.Enumerable;
import org.odata4j.core.Throwables;
import org.odata4j.stax2.Attribute2;
import org.odata4j.stax2.Characters2;
import org.odata4j.stax2.EndElement2;
import org.odata4j.stax2.Namespace2;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.StartElement2;
import org.odata4j.stax2.XMLEvent2;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.XMLEventWriter2;
import org.odata4j.stax2.XMLFactoryProvider2;
import org.odata4j.stax2.XMLInputFactory2;
import org.odata4j.stax2.XMLOutputFactory2;
import org.odata4j.stax2.XMLWriter2;
import org.odata4j.stax2.XMLWriterFactory2;

public class StaxXMLFactoryProvider2 extends XMLFactoryProvider2 {

  public static QName toQName(QName2 qname) {
    if (qname.getPrefix() == null)
      return new QName(qname.getNamespaceUri(), qname.getLocalPart());
    return new QName(qname.getNamespaceUri(), qname.getLocalPart(), qname.getPrefix());
  }

  @Override
  public XMLWriterFactory2 newXMLWriterFactory2() {
    return new StaxXMLWriterFactory2();
  }

  private static class StaxXMLWriterFactory2 implements XMLWriterFactory2 {

    @Override
    public XMLWriter2 createXMLWriter(Writer writer) {
      return new StaxXMLWriter2(writer);
    }

  }

  @Override
  public XMLInputFactory2 newXMLInputFactory2() {
    return new StaxXMLInputFactory2(XMLInputFactory.newInstance());
  }

  private static class StaxXMLInputFactory2 implements XMLInputFactory2 {

    private final XMLInputFactory factory;

    public StaxXMLInputFactory2(XMLInputFactory factory) {
      this.factory = factory;
    }

    @Override
    public XMLEventReader2 createXMLEventReader(Reader reader) {
      try {
        XMLEventReader real = factory.createXMLEventReader(reader);
        return new StaxXMLEventReader2(real);
      } catch (XMLStreamException e) {
        throw Throwables.propagate(e);
      }
    }

  }

  private static class StaxXMLEventReader2 implements XMLEventReader2 {
    private final XMLEventReader real;

    public StaxXMLEventReader2(XMLEventReader real) {
      this.real = real;
    }

    @Override
    public String getElementText() {
      try {
        return real.getElementText();
      } catch (XMLStreamException e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public boolean hasNext() {
      return real.hasNext();
    }

    @Override
    public XMLEvent2 nextEvent() {
      try {
        return new StaxXMLEvent2(real.nextEvent());
      } catch (XMLStreamException e) {
        throw Throwables.propagate(e);
      }
    }

  }

  @Override
  public XMLOutputFactory2 newXMLOutputFactory2() {
    return new StaxXMLOutputFactory2(XMLOutputFactory.newInstance());
  }

  private static class StaxXMLOutputFactory2 implements XMLOutputFactory2 {

    private final XMLOutputFactory factory;

    public StaxXMLOutputFactory2(XMLOutputFactory factory) {
      this.factory = factory;
    }

    @Override
    public XMLEventWriter2 createXMLEventWriter(Writer writer) {

      try {
        XMLEventWriter real = factory.createXMLEventWriter(writer);
        return new StaxXMLEventWriter2(real);
      } catch (XMLStreamException e) {
        throw Throwables.propagate(e);
      }

    }

  }

  private static class StaxXMLEventWriter2 implements XMLEventWriter2 {
    private final XMLEventWriter real;

    public StaxXMLEventWriter2(XMLEventWriter real) {
      this.real = real;
    }

    @Override
    public void add(XMLEvent2 event) {
      XMLEvent realXMLEvent = ((StaxXMLEvent2) event).getXMLEvent();
      try {
        real.add(realXMLEvent);
      } catch (XMLStreamException e) {
        throw Throwables.propagate(e);
      }
    }

  }

  private static class StaxXMLEvent2 implements XMLEvent2 {
    private final XMLEvent real;

    public StaxXMLEvent2(XMLEvent real) {
      this.real = real;
    }

    @Override
    public String toString() {
      return String.format("%s[%s]", StaxXMLEvent2.class.getSimpleName(), getEventTypeName());
    }

    private String getEventTypeName() {
      switch (real.getEventType()) {
      case XMLStreamConstants.START_ELEMENT:
        return "START_ELEMENT";
      case XMLStreamConstants.END_ELEMENT:
        return "END_ELEMENT";
      case XMLStreamConstants.CHARACTERS:
        return "CHARACTERS";
      case XMLStreamConstants.ATTRIBUTE:
        return "ATTRIBUTE";
      case XMLStreamConstants.NAMESPACE:
        return "NAMESPACE";
      case XMLStreamConstants.PROCESSING_INSTRUCTION:
        return "PROCESSING_INSTRUCTION";
      case XMLStreamConstants.COMMENT:
        return "COMMENT";
      case XMLStreamConstants.START_DOCUMENT:
        return "START_DOCUMENT";
      case XMLStreamConstants.END_DOCUMENT:
        return "END_DOCUMENT";
      case XMLStreamConstants.DTD:
        return "DTD";
      default:
        return "UNKNOWN TYPE " + real.getEventType();
      }
    }

    public XMLEvent getXMLEvent() {
      return real;
    }

    @Override
    public EndElement2 asEndElement() {
      return new StaxEndElement2(real.asEndElement());
    }

    @Override
    public StartElement2 asStartElement() {
      return new StaxStartElement2(real.asStartElement());
    }

    @Override
    public Characters2 asCharacters() {
      return new StaxCharacters2(real.asCharacters());
    }

    @Override
    public boolean isEndElement() {
      return real.isEndElement();
    }

    @Override
    public boolean isStartElement() {
      return real.isStartElement();
    }

    @Override
    public boolean isCharacters() {
      return real.isCharacters();
    }

  }

  private static class StaxEndElement2 implements EndElement2 {
    private final EndElement real;

    public StaxEndElement2(EndElement real) {
      this.real = real;
    }

    @Override
    public QName2 getName() {
      return new QName2(real.getName().getNamespaceURI(), real.getName().getLocalPart(), real.getName().getPrefix());
    }
  }

  public static class StaxStartElement2 implements StartElement2 {
    public final StartElement real;

    public StaxStartElement2(StartElement real) {
      this.real = real;
    }

    @Override
    public QName2 getName() {
      return new QName2(real.getName().getNamespaceURI(), real.getName().getLocalPart(), real.getName().getPrefix());
    }

    @Override
    public Attribute2 getAttributeByName(String name) {
      return getAttributeByName(new QName2(name));
    }

    @Override
    public Attribute2 getAttributeByName(QName2 name) {
      Attribute att = real.getAttributeByName(toQName(name));
      if (att == null)
        return null;
      return new StaxAttribute2(att);
    }

    @Override
    public Enumerable<Attribute2> getAttributes() {
      Iterator<?> i = real.getAttributes();
      List<Attribute2> atts = new ArrayList<Attribute2>();
      while (i.hasNext()) {
        atts.add(new StaxAttribute2((Attribute) i.next()));
      }
      return Enumerable.create(atts);
    }

    @Override
    public Enumerable<Namespace2> getNamespaces() {
      Iterator<?> i = real.getNamespaces();
      List<Namespace2> namespaces = new ArrayList<Namespace2>();
      while (i.hasNext()) {
        namespaces.add(new StaxNamespace2((Namespace) i.next()));
      }
      return Enumerable.create(namespaces);
    }
  }

  private static class StaxNamespace2 extends StaxAttribute2 implements Namespace2 {

    public StaxNamespace2(Namespace real) {
      super(real);
    }

    @Override
    public String getNamespaceURI() {
      return ((Namespace) real).getNamespaceURI();
    }

    @Override
    public String getPrefix() {
      return ((Namespace) real).getPrefix();
    }

    @Override
    public boolean isDefaultNamespaceDeclaration() {
      return ((Namespace) real).isDefaultNamespaceDeclaration();
    }

  }

  private static class StaxAttribute2 implements Attribute2 {
    protected final Attribute real;

    public StaxAttribute2(Attribute real) {
      this.real = real;
    }

    @Override
    public String getValue() {
      return real.getValue();
    }

    @Override
    public QName2 getName() {
      return new QName2(real.getName().getNamespaceURI(),
          real.getName().getLocalPart(), real.getName().getPrefix());
    }
  }

  private static class StaxCharacters2 implements Characters2 {
    protected final Characters real;

    public StaxCharacters2(Characters real) {
      this.real = real;
    }

    @Override
    public String getData() {
      return real.getData();
    }

  }

}
