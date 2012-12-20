package com.sap.core.odata.core.ep;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.ep.ODataEntityProviderProperties;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * @author SAP AG
 */
public class AtomEntityProvider extends ODataEntityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(AtomEntityProvider.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

  AtomEntityProvider() throws ODataEntityProviderException {
    super();
  }

  @Override
  public ODataEntityContent writeServiceDocument(Edm edm, String serviceRoot) throws ODataEntityProviderException {
    OutputStreamWriter writer = null;
    ODataEntityContentImpl content = new ODataEntityContentImpl();

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      OutputStream outputStream = csb.getOutputStream();
      writer = new OutputStreamWriter(outputStream, DEFAULT_CHARSET);
      AtomServiceDocumentProvider.writeServiceDocument(edm, serviceRoot, writer);

      content.setContentStream(csb.getInputStream());
      content.setContentHeader(createContentHeader(ContentType.APPLICATION_ATOM_SVC));

      return content;
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
  public ODataEntityContent writeEntry(EdmEntitySet entitySet, Map<String, Object> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException {
    OutputStream outStream = null;
    ODataEntityContentImpl content = new ODataEntityContentImpl();

    try {
      AtomEntryEntityProvider as = new AtomEntryEntityProvider(properties);
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);

      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      as.append(writer, eia, data, true);

      writer.flush();
      outStream.flush();
      outStream.close();

      content.setContentStream(csb.getInputStream());
      content.setContentHeader(createContentHeader(ContentType.APPLICATION_ATOM_XML_ENTRY));
      content.setETag(as.getETag());

      return content;
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
  public ODataEntityContent writeProperty(EdmProperty edmProperty, Object value) throws ODataEntityProviderException {
    OutputStream outStream = null;
    ODataEntityContentImpl content = new ODataEntityContentImpl();

    try {
      XmlPropertyEntityProvider ps = new XmlPropertyEntityProvider();
      EntityPropertyInfo propertyInfo = EntityInfoAggregator.create(edmProperty);

      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      ps.append(writer, propertyInfo, value, true);

      writer.flush();
      outStream.flush();
      outStream.close();

      content.setContentStream(csb.getInputStream());
      content.setContentHeader(createContentHeader(ContentType.APPLICATION_XML));

      return content;
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
  public ODataEntityContent writeFeed(GetEntitySetView entitySetView, List<Map<String, Object>> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException {
    OutputStream outStream = null;
    ODataEntityContentImpl content = new ODataEntityContentImpl();

    try {
      EdmEntitySet entitySet = entitySetView.getTargetEntitySet();

      AtomFeedProvider atomFeedProvider = new AtomFeedProvider(properties);
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);

      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      atomFeedProvider.append(writer, eia, data, entitySetView);

      writer.flush();
      outStream.flush();
      outStream.close();

      content.setContentStream(csb.getInputStream());
      content.setContentHeader(createContentHeader(ContentType.APPLICATION_ATOM_XML_FEED));
      return content;
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
  public ODataEntityContent writeText(EdmProperty edmProperty, Object value) throws ODataEntityProviderException {
    ODataEntityContentImpl content = new ODataEntityContentImpl();

    try {
      Map<?, ?> mappedData = Collections.emptyMap();

      if (value instanceof Map) {
        mappedData = (Map<?, ?>) value;
        value = mappedData.get(edmProperty.getName());
      }

      EdmSimpleType type = (EdmSimpleType) edmProperty.getType();
      String stringValue = type.valueToString(value, EdmLiteralKind.DEFAULT, edmProperty.getFacets());
      if (stringValue == null) {
        stringValue = "";
      }

      ByteArrayInputStream bais = new ByteArrayInputStream(stringValue.getBytes(DEFAULT_CHARSET));
      content.setContentStream(bais);

      String contentHeader = ContentType.TEXT_PLAIN.toString();
      if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
        if (edmProperty.getMimeType() != null) {
          contentHeader = edmProperty.getMimeType();
        } else {
          if (edmProperty.getMapping() != null && edmProperty.getMapping().getMimeType() != null) {
            String mimeTypeMapping = edmProperty.getMapping().getMimeType();
            contentHeader = (String) mappedData.get(mimeTypeMapping);
          }
        }
      }

      content.setContentHeader(contentHeader);

      return content;
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  @Override
  public ODataEntityContent writeMediaResource(String mimeType, byte[] data) throws ODataEntityProviderException {
    ODataEntityContentImpl content = new ODataEntityContentImpl();
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      content.setContentStream(bais);
      content.setContentHeader(mimeType);
      return content;
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private String createContentHeader(ContentType mediaType) {
    return mediaType.toString() + "; charset=" + DEFAULT_CHARSET;
  }

  @Override
  public ODataEntityContent writeLink(final EdmEntitySet entitySet, final Map<String, Object> data, final ODataEntityProviderProperties properties) throws ODataEntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
      
    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);

      XmlLinkEntityProvider entity = new XmlLinkEntityProvider(properties);
      final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet);
      entity.append(writer, entityInfo, data, true);

      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (IOException e1) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e1);
    } catch (XMLStreamException e2) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e2);
    } catch (FactoryConfigurationError e3) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e3);
    } finally {
      if (outStream != null)
        try {
          outStream.close();
        } catch (IOException e) {
          // don't throw in finally!  
          LOG.error(e.getLocalizedMessage(), e);
        }
    }

    ODataEntityContentImpl content = new ODataEntityContentImpl();
    content.setContentStream(buffer.getInputStream());
    content.setContentHeader(createContentHeader(ContentType.APPLICATION_XML));

    return content;
  }
}
