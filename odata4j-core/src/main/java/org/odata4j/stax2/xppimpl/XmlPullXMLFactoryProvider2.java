package org.odata4j.stax2.xppimpl;

import java.io.Reader;
import java.io.Writer;

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
import org.odata4j.stax2.domimpl.ManualXMLWriter2;
import org.odata4j.stax2.util.InMemoryAttributes;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlPullXMLFactoryProvider2 extends XMLFactoryProvider2 {

  @Override
  public XMLInputFactory2 newXMLInputFactory2() {
    return new XmlPullXMLInputFactory2();
  }

  @Override
  public XMLOutputFactory2 newXMLOutputFactory2() {
    return new XmlPullXMLOutputFactory2();
  }

  @Override
  public XMLWriterFactory2 newXMLWriterFactory2() {
    return new XmlPullXMLWriterFactory2();
  }

  private static class XmlPullXMLWriterFactory2 implements XMLWriterFactory2 {

    @Override
    public XMLWriter2 createXMLWriter(Writer writer) {
      return new ManualXMLWriter2(writer);
    }

  }

  private static class XmlPullXMLOutputFactory2 implements XMLOutputFactory2 {

    @Override
    public XMLEventWriter2 createXMLEventWriter(Writer writer) {
      return new XmlPullXMLEventWriter2(writer);
    }

  }

  private static class XmlPullXMLEventWriter2 implements XMLEventWriter2 {

    @SuppressWarnings("unused")
    private final Writer writer;

    public XmlPullXMLEventWriter2(Writer writer) {
      this.writer = writer;
    }

    @Override
    public void add(XMLEvent2 event) {
      throw new UnsupportedOperationException();

    }

  }

  private static class XmlPullXMLInputFactory2 implements XMLInputFactory2 {

    @Override
    public XMLEventReader2 createXMLEventReader(Reader reader) {
      try {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(reader);

        return new XmlPullXMLEventReader2(xpp);
      } catch (XmlPullParserException e) {
        throw Throwables.propagate(e);
      }

    }

  }

  private static class XmlPullXMLEventReader2 implements XMLEventReader2 {

    private final XmlPullParser xpp;
    private boolean peeked;
    private boolean hasNext;

    public XmlPullXMLEventReader2(XmlPullParser xpp) {
      this.xpp = xpp;
    }

    @Override
    public String getElementText() {
      try {
        if (xpp.getEventType() == XmlPullParser.TEXT)
          return xpp.getText();
        peeked = false;
        return xpp.nextText();
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public boolean hasNext() {
      if (peeked)
        return hasNext;
      int eventType = advance();
      peeked = true;
      hasNext = eventType != XmlPullParser.END_DOCUMENT;
      return hasNext;
    }

    @Override
    public XMLEvent2 nextEvent() {
      if (peeked)
        peeked = false;
      else
        advance();
      return new XmlPullXMLEvent2(xpp);
    }

    private int advance() {
      try {
        return xpp.next();
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }

  }

  private static class XmlPullXMLEvent2 implements XMLEvent2 {
    private final XmlPullParser xpp;

    public XmlPullXMLEvent2(XmlPullParser xpp) {
      this.xpp = xpp;
    }

    @Override
    public String toString() {
      return String.format("%s[%s]", XmlPullXMLEvent2.class.getSimpleName(), getEventTypeName());
    }

    private String getEventTypeName() {
      try {
        switch (xpp.getEventType()) {
        case XmlPullParser.START_DOCUMENT:
          return "START_DOCUMENT";
        case XmlPullParser.END_DOCUMENT:
          return "END_DOCUMENT";
        case XmlPullParser.START_TAG:
          return "START_TAG";
        case XmlPullParser.END_TAG:
          return "END_TAG";
        case XmlPullParser.TEXT:
          return "TEXT";
        case XmlPullParser.CDSECT:
          return "CDSECT";
        case XmlPullParser.ENTITY_REF:
          return "ENTITY_REF";
        case XmlPullParser.IGNORABLE_WHITESPACE:
          return "IGNORABLE_WHITESPACE";
        case XmlPullParser.PROCESSING_INSTRUCTION:
          return "PROCESSING_INSTRUCTION";
        case XmlPullParser.COMMENT:
          return "COMMENT";
        case XmlPullParser.DOCDECL:
          return "DOCDECL";
        default:
          return "UNKNOWN TYPE " + xpp.getEventType();
        }
      } catch (XmlPullParserException e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public EndElement2 asEndElement() {
      if (!isEndElement())
        return null;
      return new XmlPullEndElement2(new QName2(xpp.getNamespace(), xpp.getName()));
    }

    @Override
    public StartElement2 asStartElement() {
      if (!isStartElement())
        return null;
      return new XmlPullStartElement2(xpp);
    }

    @Override
    public Characters2 asCharacters() {
      if (!isCharacters())
        return null;
      return new XmlPullCharacters2(xpp);
    }

    @Override
    public boolean isEndElement() {
      try {
        return xpp.getEventType() == XmlPullParser.END_TAG;
      } catch (XmlPullParserException e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public boolean isStartElement() {
      try {
        return xpp.getEventType() == XmlPullParser.START_TAG;
      } catch (XmlPullParserException e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public boolean isCharacters() {
      try {
        return xpp.getEventType() == XmlPullParser.TEXT;
      } catch (XmlPullParserException e) {
        throw Throwables.propagate(e);
      }
    }

  }

  private static class XmlPullStartElement2 implements StartElement2 {
    private final XmlPullParser xpp;
    private final QName2 name;
    private InMemoryAttributes attributes;

    public XmlPullStartElement2(XmlPullParser xpp) {
      this.xpp = xpp;
      name = new QName2(xpp.getNamespace(), xpp.getName());
    }

    @Override
    public Attribute2 getAttributeByName(QName2 name) {
      ensureAttributesCached();
      return attributes.getAttributeByName(name);
    }

    @Override
    public Attribute2 getAttributeByName(String name) {
      ensureAttributesCached();
      return attributes.getAttributeByName(name);
    }

    @Override
    public QName2 getName() {
      return name;
    }

    private void ensureAttributesCached() {
      if (attributes != null)
        return;
      attributes = new InMemoryAttributes();
      for (int i = 0; i < xpp.getAttributeCount(); i++) {
        String ns = xpp.getAttributeNamespace(i);
        attributes.put(
            ns == null || ns.length() == 0 ? null : ns,
            xpp.getAttributeName(i),
            xpp.getAttributePrefix(i),
            xpp.getAttributeValue(i));
      }
    }

    @Override
    public Enumerable<Attribute2> getAttributes() {
      ensureAttributesCached();
      return attributes.getAttributes();
    }

    @Override
    public Enumerable<Namespace2> getNamespaces() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }

  private static class XmlPullEndElement2 implements EndElement2 {
    private final QName2 name;

    public XmlPullEndElement2(QName2 name) {
      this.name = name;
    }

    @Override
    public QName2 getName() {
      return name;
    }

  }

  private static class XmlPullCharacters2 implements Characters2 {
    private final XmlPullParser xpp;

    public XmlPullCharacters2(XmlPullParser xpp) {
      this.xpp = xpp;
    }

    @Override
    public String getData() {
      return xpp.getText();
    }

  }

}
