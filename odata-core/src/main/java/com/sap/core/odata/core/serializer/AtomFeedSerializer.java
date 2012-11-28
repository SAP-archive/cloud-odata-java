package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.api.serialization.ODataSerializerProperties;

public class AtomFeedSerializer extends ODataSerializer {

  private static final String NS_ATOM = "http://www.w3.org/2005/Atom";

  AtomFeedSerializer(ODataSerializerProperties properties) throws ODataSerializationException {
    super(properties);
  }

  @Override
  public InputStream serialize() throws ODataSerializationException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      this.serializeInto(out);

      out.flush();
      out.close();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  @Override
  public void serializeInto(OutputStream stream) throws ODataSerializationException {
    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stream, "utf-8");

      writer.writeStartElement("feed");
      writer.writeDefaultNamespace(NS_ATOM);
      // writer.writeNamespace("m", NS_DATASERVICES_METADATA);
      // writer.writeNamespace("d", NS_DATASERVICES);
      // writer.writeAttribute(NS_XML, "base", getContext().getUriInfo().getBaseUri().toASCIIString());

      // TODO: implement
      Map<String, Object> internalData = getData();

      for (Object object : internalData.values()) {
        if (object instanceof Map) {
          ODataSerializerProperties internalProperties = ODataSerializerFactory.createProperties();
          internalProperties.setContext(getContext());
          internalProperties.setEdmEntitySet(getEdmEntitySet());
          internalProperties.setData((HashMap<String, Object>) object);
          // ODataSerializer internalSerializer = ODataSerializerFactory.create(Format.ATOM, properties);
//          internalSerializer.serializeInto(stream);

          AtomEntrySerializer internalSerializer = new AtomEntrySerializer(internalProperties);
          internalSerializer.appendTo(writer);
          
        }
      }

      writer.writeEndElement();
      writer.flush();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

}
