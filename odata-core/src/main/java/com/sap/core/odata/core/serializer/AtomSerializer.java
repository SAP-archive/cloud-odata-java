package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.core.edm.provider.EdmMetadata;

// TODO usage of "ByteArrayInputStream(out.toByteArray())":  check synchronized call / copy of data

public class AtomSerializer extends ODataSerializer {

  private static final Logger LOG = LoggerFactory.getLogger(AtomSerializer.class);

  AtomSerializer(ODataContext ctx) throws ODataSerializationException {
    super(ctx);
  }

  @Override
  public InputStream serializeServiceDocument(Edm edm, String serviceRoot) throws ODataSerializationException {
    OutputStreamWriter writer = null;

    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      writer = new OutputStreamWriter(outputStream, "UTF-8");
      AtomServiceDocumentSerializer.writeServiceDocument(edm, serviceRoot, writer);
      return new ByteArrayInputStream(outputStream.toByteArray());
    } catch (UnsupportedEncodingException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          throw new ODataSerializationException(ODataSerializationException.COMMON, e);
        }
      }
    }
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
      ps.append(writer, edmProperty, value, true);

      writer.flush();
      out.flush();
      out.close();

      return new ByteArrayInputStream(out.toByteArray());

    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

}
