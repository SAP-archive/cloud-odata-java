package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;

// TODO usage of "ByteArrayInputStream(out.toByteArray())":  check synchronized call / copy of data

public class AtomSerializer extends ODataSerializer {

  private static final Logger LOG = LoggerFactory.getLogger(AtomSerializer.class);

  AtomSerializer(ODataContext ctx) throws ODataSerializationException {
    super(ctx);
  }

  @Override
  public InputStream serializeEntry(EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      LOG.debug("Start serialization...");
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      this.serializeInto(out, entitySet, data);

      out.flush();
      out.close();
      LOG.debug("...finished serialization.");
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON);
    }
  }

  public void serializeInto(OutputStream stream, EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      AtomEntrySerializer serializer = new AtomEntrySerializer(this.getContext());
      LOG.debug("Use serializer class {}", serializer.getClass());
      serializer.serializeInto(stream, entitySet, data);
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON);
    }
  }

  @Override
  public InputStream serializeProperty(EdmProperty edmProperty, Object value) throws ODataSerializationException {
    try {
      XmlPropertySerializer ps = new XmlPropertySerializer();

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out, "utf-8");
      ps.append(writer, edmProperty, value);

      writer.flush();
      out.flush();
      out.close();

      return new ByteArrayInputStream(out.toByteArray());

    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }
}
