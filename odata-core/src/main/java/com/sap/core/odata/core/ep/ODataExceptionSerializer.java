package com.sap.core.odata.core.ep;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.enums.Format;

public class ODataExceptionSerializer {
  
  private final static Logger LOG = LoggerFactory.getLogger(ODataExceptionSerializer.class);
  
  public static String serialize(String errorCode, String message, Format format, Locale locale) {

    String returnMessage = null;
    switch (format) {
    case ATOM:
    case XML:
      returnMessage = serializeXml(errorCode, message, locale);
      break;
    case JSON:
      returnMessage = serialzieJson(errorCode, message, locale);
      break;
    default:
      returnMessage = serializeXml(errorCode, message, locale);
      break;

    }

    return returnMessage;
  }

  private static String serialzieJson(String errorCode, String message, Locale locale) {
    return null;
  }

  private static String serializeXml(String errorCode, String message, Locale locale) {
    String outputMessage = null;
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(
          outputStream, "utf-8");
      XMLStreamWriter xmlStreamWriter = XMLOutputFactory
          .newInstance().createXMLStreamWriter(writer);

      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.writeStartElement("error");
      xmlStreamWriter.writeNamespace("xmlns", Edm.NAMESPACE_M_2007_08);
      xmlStreamWriter.writeStartElement("code");
      xmlStreamWriter.writeCharacters(errorCode);
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeStartElement("message");
      xmlStreamWriter.writeAttribute(Edm.PREFIX_XML,Edm.NAMESPACE_XML_1998, "lang", getLang(locale));
      xmlStreamWriter.writeCharacters(message);
      xmlStreamWriter.writeEndDocument();

      outputStream.flush();
      writer.flush();
      xmlStreamWriter.flush();
      byte[] data = outputStream.toByteArray();
      outputStream.close();
      outputMessage = new String(data);
    } catch (Exception e) {
      LOG.error("Fatal Error when serializing an Exception.", e);
      outputMessage = "Fatal Error when serializing an Exception.";
    }
    return outputMessage;
  }
  
  /**
   * Get language as defined in RFC 4646 based on {@link Locale}.
   * 
   * @return
   */
  private static String getLang(Locale locale) {
    if (locale.getCountry().isEmpty()) {
      return locale.getLanguage();
    }
    return locale.getLanguage() + "-" + locale.getCountry();
  }

}
