package org.odata4j.stax2.domimpl;

import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

import org.odata4j.core.ODataConstants.Charsets;
import org.odata4j.core.Throwables;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.XMLWriter2;

public class ManualXMLWriter2 implements XMLWriter2 {

  private final Writer writer;
  private boolean isStartElementOpen;
  private final Stack<QName2> elements = new Stack<QName2>();

  public ManualXMLWriter2(Writer writer) {
    this.writer = writer;
  }

  @Override
  public void endDocument() {

    while (!elements.isEmpty())
      endElement(elements.peek().getLocalPart());

    try {
      writer.flush();
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void endElement(String localName) {
    QName2 startElementName = elements.pop();
    if (!startElementName.getLocalPart().equals(localName))
      throw new IllegalArgumentException();

    if (isStartElementOpen) {
      write("/");
      write(">");
      isStartElementOpen = false;
      return;
    }

    write("</");
    if (startElementName.getPrefix() != null) {
      write(startElementName.getPrefix());
      write(":");
    }
    write(localName);
    write(">");
  }

  @Override
  public void startDocument() {
    write("<?xml version=\"1.0\" encoding=\"" + Charsets.Lower.UTF_8 + "\" standalone=\"yes\" ?>");
  }

  @Override
  public void startElement(String name) {
    startElement(new QName2(name));
  }

  @Override
  public void startElement(QName2 qname) {
    startElement(qname, null);
  }

  @Override
  public void startElement(QName2 qname, String xmlns) {
    ensureStartElementClosed();
    write("<");
    if (qname.getPrefix() != null) {
      write(qname.getPrefix());
      write(":");
    }
    write(qname.getLocalPart());

    if (xmlns != null) {
      write(" xmlns=\"" + xmlns + "\"");
    }
    isStartElementOpen = true;
    elements.push(qname);

  }

  @Override
  public void writeAttribute(String localName, String value) {
    writeAttribute(new QName2(localName), value);
  }

  @Override
  public void writeAttribute(QName2 qname, String value) {
    if (!isStartElementOpen)
      throw new IllegalStateException();

    write(" ");
    if (qname.getPrefix() != null) {
      write(qname.getPrefix());
      write(":");
    }
    write(qname.getLocalPart());
    write("=\"");
    write(encodeAttributeValue(value));
    write("\"");
  }

  @Override
  public void writeNamespace(String prefix, String namespaceUri) {
    if (!isStartElementOpen)
      throw new IllegalStateException();
    write(" xmlns:" + prefix + "=\"" + namespaceUri + "\"");
  }

  @Override
  public void writeText(String content) {
    ensureStartElementClosed();
    write(encodeElementValue(content));
  }

  private void ensureStartElementClosed() {
    if (isStartElementOpen) {
      write(">");
      isStartElementOpen = false;
    }
  }

  private void write(String value) {
    try {
      writer.write(value);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private String encodeElementValue(String value) {
    return encodeAttributeValue(value); // TODO
  }

  private String encodeAttributeValue(String value) {
    if (value == null)
      return null;

    int len = value.length();
    if (len == 0)
      return value;

    StringBuffer encoded = new StringBuffer();
    for (int i = 0; i < len; i++) {
      char c = value.charAt(i);
      if (c == '<')
        encoded.append("&lt;");
      else if (c == '\"')
        encoded.append("&quot;");
      else if (c == '>')
        encoded.append("&gt;");
      else if (c == '\'')
        encoded.append("&apos;");
      else if (c == '&')
        encoded.append("&amp;");
      else
        encoded.append(c);
    }

    return encoded.toString();
  }

}
