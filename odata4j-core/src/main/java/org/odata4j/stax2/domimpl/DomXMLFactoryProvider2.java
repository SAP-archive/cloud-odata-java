package org.odata4j.stax2.domimpl;

import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.core4j.Enumerable;
import org.core4j.ReadOnlyIterator;
import org.odata4j.core.Throwables;
import org.odata4j.internal.AndroidCompat;
import org.odata4j.stax2.Attribute2;
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
import org.odata4j.stax2.util.InMemoryXMLEvent2;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DomXMLFactoryProvider2 extends XMLFactoryProvider2 {

  @Override
  public XMLInputFactory2 newXMLInputFactory2() {
    return new DomXMLInputFactory2();
  }

  @Override
  public XMLOutputFactory2 newXMLOutputFactory2() {
    return new DomXMLOutputFactory2();
  }

  @Override
  public XMLWriterFactory2 newXMLWriterFactory2() {
    return new DomXMLWriterFactory2();
  }

  private static class DomXMLOutputFactory2 implements XMLOutputFactory2 {

    @Override
    public XMLEventWriter2 createXMLEventWriter(Writer writer) {
      return new DomXMLEventWriter2(writer);
    }

  }

  private static class DomXMLEventWriter2 implements XMLEventWriter2 {

    @SuppressWarnings("unused")
    private final Writer writer;

    public DomXMLEventWriter2(Writer writer) {
      this.writer = writer;
    }

    @Override
    public void add(XMLEvent2 event) {
      throw new UnsupportedOperationException();

    }

  }

  private static class DomXMLWriterFactory2 implements XMLWriterFactory2 {

    @Override
    public XMLWriter2 createXMLWriter(Writer writer) {
      return new ManualXMLWriter2(writer);
    }

  }

  private static class DomXMLInputFactory2 implements XMLInputFactory2 {

    @Override
    public XMLEventReader2 createXMLEventReader(Reader reader) {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      try {
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new InputSource(reader));
        return new DomXMLEventReader2(document);
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }

    }

  }

  private static class DomXMLEventReader2 implements XMLEventReader2 {
    private final Document document;

    private EventIterator iterator;

    public DomXMLEventReader2(Document document) {
      this.document = document;
      iterator = new EventIterator();
    }

    @Override
    public String getElementText() {
      return iterator.getElementText();
    }

    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }

    @Override
    public XMLEvent2 nextEvent() {
      return iterator.next();
    }

    private class EventIterator extends ReadOnlyIterator<XMLEvent2> {

      private Element current;
      private boolean down = true;

      public EventIterator() {

      }

      public String getElementText() {
        return AndroidCompat.getTextContent(current);
      }

      private IterationResult<XMLEvent2> startElement2() {
        DomStartElement2 start = new DomStartElement2(current);
        XMLEvent2 event = new InMemoryXMLEvent2(start, null, null);
        return IterationResult.next(event);
      }

      private IterationResult<XMLEvent2> endElement2() {
        DomEndElement2 end = new DomEndElement2(current);
        XMLEvent2 event = new InMemoryXMLEvent2(null, end, null);
        return IterationResult.next(event);
      }

      @Override
      protected IterationResult<XMLEvent2> advance() throws Exception {

        if (current == null) {
          current = document.getDocumentElement();
          return startElement2();

        } else {

          if (down) {
            // traverse down
            NodeList children = current.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
              Node childNode = children.item(i);
              if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                current = (Element) childNode;
                return startElement2();
              }
            }
            down = false;
            return endElement2();

          } else {
            // traverse up
            Node parentNode = current.getParentNode();
            if (parentNode.getNodeType() == Node.DOCUMENT_NODE) {
              return IterationResult.done();
            }
            Element parentElement = (Element) parentNode;
            NodeList children = parentElement.getChildNodes();
            boolean found = false;
            for (int i = 0; i < children.getLength(); i++) {
              Node childNode = children.item(i);
              if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                if (!found) {
                  if (childNode == current)
                    found = true;
                  continue;
                }

                current = (Element) childNode;
                down = true;
                return startElement2();
              }
            }
            current = parentElement;
            return endElement2();
          }

        }

      }

    }
  }

  private static class DomStartElement2 implements StartElement2 {

    private final Element element;

    public DomStartElement2(Element element) {
      this.element = element;
    }

    @Override
    public Attribute2 getAttributeByName(String name) {
      return getAttributeByName(new QName2(name));
    }

    @Override
    public Attribute2 getAttributeByName(QName2 qName2) {

      final Attr attr = element.getAttributeNodeNS(qName2.getNamespaceUri(), qName2.getLocalPart());
      if (attr == null)
        return null;

      return new Attribute2() {
        public String getValue() {
          return attr.getValue();
        }

        @Override
        public QName2 getName() {
          throw new UnsupportedOperationException("Not supported yet.");
        }
      };

    }

    @Override
    public QName2 getName() {
      return new QName2(element.getNamespaceURI(), element.getLocalName());
    }

    @Override
    public String toString() {
      return "StartElement " + getName() + " " + AndroidCompat.getTextContent(element);
    }

    @Override
    public Enumerable<Attribute2> getAttributes() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumerable<Namespace2> getNamespaces() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }

  private static class DomEndElement2 implements EndElement2 {

    private final Element element;

    public DomEndElement2(Element element) {
      this.element = element;
    }

    @Override
    public QName2 getName() {
      return new QName2(element.getNamespaceURI(), element.getLocalName());
    }

    @Override
    public String toString() {
      return "EndElement " + getName() + " " + AndroidCompat.getTextContent(element);
    }

  }

}
