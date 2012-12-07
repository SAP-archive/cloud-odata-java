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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;

// TODO usage of "ByteArrayInputStream(out.toByteArray())":  check synchronized call / copy of data
public class AtomEntityProvider extends ODataEntityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(AtomEntityProvider.class);
  
  AtomEntityProvider(ODataContext ctx) throws ODataEntityProviderException {
    super(ctx);
  }

  @Override
  public InputStream writeServiceDocument(Edm edm, String serviceRoot) throws ODataEntityProviderException {
    OutputStreamWriter writer = null;

    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      writer = new OutputStreamWriter(outputStream, "utf-8");
      AtomServiceDocumentProvider.writeServiceDocument(edm, serviceRoot, writer);
      return new ByteArrayInputStream(outputStream.toByteArray());
    } catch (UnsupportedEncodingException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          // don't throw in finally!  
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

  @Override
  public InputStream writeFeed(EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType) throws ODataEntityProviderException {
    return null;
  }
  
  @Override
  public InputStream writeEntry(EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType) throws ODataEntityProviderException {
    ByteArrayOutputStream outStream = null;

    try {
      AtomEntryEntityProvider as = new AtomEntryEntityProvider(this.getContext());
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);

      outStream = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, "utf-8");
      as.append(writer, eia, data, true, mediaResourceMimeType);

      writer.flush();
      outStream.flush();
      outStream.close();
      
      return new ByteArrayInputStream(outStream.toByteArray());
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (IOException e) {
          // don't throw in finally!  
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

  @Override
  public InputStream writeProperty(EdmProperty edmProperty, Object value) throws ODataEntityProviderException {
    ByteArrayOutputStream outStream = null;

    try {
      XmlPropertyEntityProvider ps = new XmlPropertyEntityProvider();
      EntityPropertyInfo propertyInfo = EntityInfoAggregator.create(edmProperty);

      outStream = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, "utf-8");
      ps.append(writer, propertyInfo, value, true);

      writer.flush();
      outStream.flush();
      outStream.close();

      return new ByteArrayInputStream(outStream.toByteArray());
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (IOException e) {
          // don't throw in finally!  
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

}
