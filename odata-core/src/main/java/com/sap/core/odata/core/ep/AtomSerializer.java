package com.sap.core.odata.core.ep;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.ODataSerializationException;
import com.sap.core.odata.api.ep.ODataSerializer;
import com.sap.core.odata.api.processor.ODataContext;

// TODO usage of "ByteArrayInputStream(out.toByteArray())":  check synchronized call / copy of data
public class AtomSerializer extends ODataSerializer {

  AtomSerializer(ODataContext ctx) throws ODataSerializationException {
    super(ctx);
  }

  @Override
  public InputStream serializeServiceDocument(Edm edm, String serviceRoot) throws ODataSerializationException {
    OutputStreamWriter writer = null;

    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      writer = new OutputStreamWriter(outputStream, "utf-8");
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
  public InputStream serializeEntry(EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType) throws ODataSerializationException {
    ByteArrayOutputStream outStream = null;

    try {
      AtomEntrySerializer as = new AtomEntrySerializer(this.getContext());

      outStream = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, "utf-8");
      as.append(writer, entitySet, data, true, mediaResourceMimeType);

      writer.flush();
      outStream.flush();
      outStream.close();
      
      return new ByteArrayInputStream(outStream.toByteArray());
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (IOException e) {
          throw new ODataSerializationException(ODataSerializationException.COMMON, e);
        }
      }
    }
  }

  @Override
  public InputStream serializeProperty(EdmProperty edmProperty, Object value) throws ODataSerializationException {
    ByteArrayOutputStream outStream = null;

    try {
      XmlPropertySerializer ps = new XmlPropertySerializer();

      outStream = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, "utf-8");
      ps.append(writer, edmProperty, value, true, null);

      writer.flush();
      outStream.flush();
      outStream.close();

      return new ByteArrayInputStream(outStream.toByteArray());
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (IOException e) {
          throw new ODataSerializationException(ODataSerializationException.COMMON, e);
        }
      }
    }
  }

}
