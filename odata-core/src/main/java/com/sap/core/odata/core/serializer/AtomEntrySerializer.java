package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;

public class AtomEntrySerializer extends ODataSerializer {

  public static final String NS_DATASERVICES = "http://schemas.microsoft.com/ado/2007/08/dataservices";
  public static final String NS_DATASERVICES_METADATA = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  public static final String NS_ATOM = "http://www.w3.org/2005/Atom";
  public static final String NS_XML ="http://www.w3.org/XML/1998/namespace";
  public static final String BASE_URI = "http://localhost/";
  
  
  @Override
  public InputStream serialize() throws ODataSerializationException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out, "utf-8");

      
      writer.writeStartElement("entry");
      writer.writeDefaultNamespace(NS_ATOM);
      writer.writeNamespace("m", NS_DATASERVICES_METADATA);
      writer.writeNamespace("d", NS_DATASERVICES);
      writer.writeAttribute(NS_XML, "base", BASE_URI);
      
//      writer.writeStartElement("id");
//      writer.writeCharacters("https://scenarioodata.prod.jpaas.sapbydesign.com/com.sap.core.odata4j.sandbox.scenario.web/scenario.jpa.svc/Employees('1')");
//      writer.writeEndElement();
//      
//      writer.writeStartElement("title");
//      writer.writeAttribute("type", "text");
//      writer.writeEndElement();

      writer.writeEndElement();

      writer.flush();

      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }
}
