/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.producer;

import java.util.Locale;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.core.ep.util.FormatXml;

public class XmlErrorDocumentProducer {

  public void writeErrorDocument(final XMLStreamWriter writer, final String errorCode, final String message, final Locale locale, final String innerError) throws XMLStreamException {
    writer.writeStartDocument();
    writer.writeStartElement(FormatXml.M_ERROR);
    writer.writeDefaultNamespace(Edm.NAMESPACE_M_2007_08);
    writer.writeStartElement(FormatXml.M_CODE);
    if (errorCode != null) {
      writer.writeCharacters(errorCode);
    }
    writer.writeEndElement();
    writer.writeStartElement(FormatXml.M_MESSAGE);
    if (locale != null) {
      writer.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, FormatXml.XML_LANG, getLocale(locale));
    } else {
      writer.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, FormatXml.XML_LANG, "");
    }
    if (message != null) {
      writer.writeCharacters(message);
    }
    writer.writeEndElement();

    if (innerError != null) {
      writer.writeStartElement(FormatXml.M_INNER_ERROR);
      writer.writeCharacters(innerError);
      writer.writeEndElement();
    }

    writer.writeEndDocument();
  }

  /**
   * Gets language and country as defined in RFC 4646 based on {@link Locale}.
   */
  private String getLocale(final Locale locale) {
    if (locale.getCountry().isEmpty()) {
      return locale.getLanguage();
    } else {
      return locale.getLanguage() + "-" + locale.getCountry();
    }
  }

}
