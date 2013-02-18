package com.sap.core.odata.core.ep;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Serializes an error message according to the OData standard.
 * @author SAP AG
 */
public class ODataExceptionSerializer {

  private final static Logger LOG = LoggerFactory.getLogger(ODataExceptionSerializer.class);

  /**
   * <p>Serializes an error message according to the OData standard.</p>
   * <p>In case an error occurs, it is logged.
   * An exception is not thrown because this method is used in exception handling.</p> 
   * @param errorCode   a String that serves as a substatus to the HTTP response code
   * @param message     a human-readable message describing the error
   * @param innerError  debugging information to assist the person responsible for a
   *                    service implementation in determining the cause of an error
   *                    (should only be used in development environments and
   *                     should not be present in a response from a production data
   *                     service to guard against information disclosure security concerns)
   * @param contentType the content type the error message should correspond to
   * @param locale      the {@link Locale} that should be used to format the error message
   * @return            an {@link InputStream} containing the serialized error message
   */
  public static InputStream serialize(String errorCode, String message, ContentType contentType, Locale locale) {
    if (contentType.getODataFormat() == ODataFormat.JSON)
      return serializeJson(errorCode, message, locale);
    else
      return serializeXml(errorCode, message, locale);
  }

  private static InputStream serializeJson(String errorCode, String message, Locale locale) {
    String notsupported = "not supported error format JSON; " + errorCode + ", " + message;
    try {
      return new ByteArrayInputStream(notsupported.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      LOG.error("Fatal Error when serializing an Exception.", e);
      return null;
    }
  }

  private static InputStream serializeXml(final String errorCode, final String message, final Locale locale) {
    InputStream outputMessage = null;
    try {
      CircleStreamBuffer buffer = new CircleStreamBuffer();
      OutputStream outputStream = buffer.getOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
      XMLStreamWriter xmlStreamWriter = XMLOutputFactory
          .newInstance().createXMLStreamWriter(writer);

      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.writeStartElement(FormatXml.M_ERROR);
      xmlStreamWriter.writeDefaultNamespace(Edm.NAMESPACE_M_2007_08);
      xmlStreamWriter.writeStartElement(FormatXml.M_CODE);
      if (errorCode != null)
        xmlStreamWriter.writeCharacters(errorCode);
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeStartElement(FormatXml.M_MESSAGE);
      if (locale != null)
        xmlStreamWriter.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, FormatXml.XML_LANG, getLocale(locale));
      if (message != null)
        xmlStreamWriter.writeCharacters(message);
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndDocument();

      outputStream.flush();
      writer.flush();
      xmlStreamWriter.flush();

      outputMessage = buffer.getInputStream();
    } catch (XMLStreamException e) {
      LOG.error("Fatal Error when serializing an Exception", e);
    } catch (IOException e) {
      LOG.error("Fatal Error when serializing an Exception", e);
    }
    return outputMessage;
  }

  /**
   * Gets language and country as defined in RFC 4646 based on {@link Locale}.
   */
  private static String getLocale(final Locale locale) {
    if (locale.getCountry().isEmpty())
      return locale.getLanguage();
    else
      return locale.getLanguage() + "-" + locale.getCountry();
  }

}
