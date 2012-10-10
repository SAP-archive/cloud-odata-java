package org.odata4j.stax2;

public interface XMLWriter2 {

  void startElement(String name);

  void startElement(QName2 qname);

  void startElement(QName2 qname, String xmlns);

  void writeAttribute(String localName, String value);

  void writeAttribute(QName2 qname, String value);

  void writeText(String content);

  void writeNamespace(String prefix, String namespaceUri);

  void startDocument();

  void endElement(String localName);

  void endDocument();

}