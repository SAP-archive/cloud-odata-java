package com.sap.core.odata.core.ep;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * @author SAP AG
 */
public class ODataExceptionSerializer {

  private final static Logger LOG = LoggerFactory.getLogger(ODataExceptionSerializer.class);

  public static InputStream serialize(String errorCode, String message, String innerError, ContentType contentType, Locale locale) {
    if (contentType.getODataFormat() == ODataFormat.JSON)
      return serializeJson(errorCode, message, innerError, locale);
    else
      return serializeXml(errorCode, message, innerError, locale);
  }

  private static InputStream serializeJson(String errorCode, String message, String innerError, Locale locale) {
    String notsupported = "not supported error format JSON";
    return new ByteArrayInputStream(notsupported.getBytes(Charset.forName("utf-8")));
  }

  private static InputStream serializeXml(String errorCode, String message, String innerError, Locale locale) {
    InputStream outputMessage = null;
    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      OutputStream outputStream = csb.getOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
      XMLStreamWriter xmlStreamWriter = XMLOutputFactory
          .newInstance().createXMLStreamWriter(writer);

      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.writeStartElement(FormatXml.M_ERROR);
      xmlStreamWriter.writeDefaultNamespace(Edm.NAMESPACE_M_2007_08);
      xmlStreamWriter.writeStartElement(FormatXml.M_CODE);
      xmlStreamWriter.writeCharacters(errorCode);
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeStartElement(FormatXml.M_MESSAGE);
      xmlStreamWriter.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, FormatXml.XML_LANG, getLang(locale));
      if (message != null)
        xmlStreamWriter.writeCharacters(message);
      xmlStreamWriter.writeEndElement();
      if (innerError != null) {
        xmlStreamWriter.writeStartElement(FormatXml.M_INNER_ERROR);
        xmlStreamWriter.writeCharacters(innerError);
        xmlStreamWriter.writeEndElement();
      }
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndDocument();

      outputStream.flush();
      writer.flush();
      xmlStreamWriter.flush();

      outputMessage = csb.getInputStream();
    } catch (XMLStreamException e) {
      LOG.error("Fatal Error when serializing an Exception.", e);
    } catch (IOException e) {
      LOG.error("Fatal Error when serializing an Exception.", e);
    }
    return outputMessage;
  }

  /**
   * Gets language and country as defined in RFC 4646 based on {@link Locale}.
   */
  private static String getLang(Locale locale) {
    if (locale.getCountry().isEmpty())
      return locale.getLanguage();
    else
      return locale.getLanguage() + "-" + locale.getCountry();
  }

}
