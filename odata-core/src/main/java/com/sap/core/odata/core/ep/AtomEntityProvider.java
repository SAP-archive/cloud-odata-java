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
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.ep.ODataEntityProviderProperties;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
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
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);

      AtomEntryEntityProvider as = new AtomEntryEntityProvider(properties);
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
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
    EntityPropertyInfo propertyInfo = EntityInfoAggregator.create(edmProperty);
    return writeSingleTypedElement(propertyInfo, value);
  }

  private ODataEntityContent writeSingleTypedElement(final EntityPropertyInfo propertyInfo, final Object value) throws ODataEntityProviderException {
    OutputStream outStream = null;
    ODataEntityContentImpl content = new ODataEntityContentImpl();

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);

      XmlPropertyEntityProvider ps = new XmlPropertyEntityProvider();
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
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);

      AtomFeedProvider atomFeedProvider = new AtomFeedProvider(properties);
      EdmEntitySet entitySet = entitySetView.getTargetEntitySet();
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
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
  public ODataEntityContent writePropertyValue(final EdmProperty edmProperty, Object value) throws ODataEntityProviderException {
    try {
      Map<?, ?> mappedData;
      if (value instanceof Map) {
        mappedData = (Map<?, ?>) value;
        value = mappedData.get(edmProperty.getName());
      } else {
        mappedData = Collections.emptyMap();
      }

      final EdmSimpleType type = (EdmSimpleType) edmProperty.getType();

      if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
        String contentType = ContentType.APPLICATION_OCTET_STREAM.toContentTypeString();
        if (edmProperty.getMimeType() != null) {
          contentType = edmProperty.getMimeType();
        } else {
          if (edmProperty.getMapping() != null && edmProperty.getMapping().getMimeType() != null) {
            String mimeTypeMapping = edmProperty.getMapping().getMimeType();
            contentType = (String) mappedData.get(mimeTypeMapping);
          }
        }
        return writeBinary(contentType, (byte[]) value);

      } else {
        return writeText(type.valueToString(value, EdmLiteralKind.DEFAULT, edmProperty.getFacets()));
      }

    } catch (EdmException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  @Override
  public ODataEntityContent writeText(final String value) throws ODataEntityProviderException {
    ODataEntityContentImpl content = new ODataEntityContentImpl();
    if (value != null) {
      ByteArrayInputStream stream;
      try {
        stream = new ByteArrayInputStream(value.getBytes(DEFAULT_CHARSET));
      } catch (UnsupportedEncodingException e) {
        throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
      }
      content.setContentStream(stream);
    }
    content.setContentHeader(ContentType.TEXT_PLAIN.toContentTypeString());
    return content;
  }

  @Override
  public ODataEntityContent writeBinary(String mimeType, byte[] data) throws ODataEntityProviderException {
    ODataEntityContentImpl content = new ODataEntityContentImpl();
    if (data != null) {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      content.setContentStream(bais);
    }
    content.setContentHeader(mimeType);
    return content;
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
    } catch (FactoryConfigurationError e1) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e1);
    } catch (XMLStreamException e2) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e2);
    } catch (IOException e3) {
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

  @Override
  public ODataEntityContent writeLinks(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final ODataEntityProviderProperties properties) throws ODataEntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();

    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);

      XmlLinksEntityProvider entity = new XmlLinksEntityProvider(properties);
      final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet);
      entity.append(writer, entityInfo, data);

      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (FactoryConfigurationError e1) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e1);
    } catch (XMLStreamException e2) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e2);
    } catch (IOException e3) {
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

  private ODataEntityContent writeCollection(final String name, final EntityPropertyInfo propertyInfo, final List<?> data) throws ODataEntityProviderException {
    OutputStream outStream = null;
    ODataEntityContentImpl content = new ODataEntityContentImpl();

    try {
      CircleStreamBuffer buffer = new CircleStreamBuffer();
      outStream = buffer.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);

      XmlCollectionEntityProvider.append(writer, name, propertyInfo, data);

      writer.flush();
      outStream.flush();
      outStream.close();

      content.setContentStream(buffer.getInputStream());
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
  public ODataEntityContent writeFunctionImport(final EdmFunctionImport functionImport, Object data, final ODataEntityProviderProperties properties) throws ODataEntityProviderException {
    try {
      final String name = functionImport.getName();
      final EdmType type = functionImport.getReturnType().getType();
      final boolean isCollection = functionImport.getReturnType().getMultiplicity() == EdmMultiplicity.MANY;

      if (type.getKind() == EdmTypeKind.ENTITY) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) data;
        return writeEntry(functionImport.getEntitySet(), map, properties);
      }

      final EntityPropertyInfo info = (type.getKind() == EdmTypeKind.COMPLEX) ?
          new EntityComplexPropertyInfo(isCollection ? FormatXml.D_ELEMENT : name, type, null, null,
              EntityInfoAggregator.create((EdmComplexType) type)) :
          new EntityPropertyInfo(isCollection ? FormatXml.D_ELEMENT : name, type, null, null);

      if (isCollection)
        return writeCollection(name, info, (List<?>) data);
      else if (type.getKind() == EdmTypeKind.COMPLEX)
        return writeSingleTypedElement(new EntityComplexPropertyInfo(name, type, null, null,
            EntityInfoAggregator.create((EdmComplexType) type)), data);
      else
        return writeSingleTypedElement(info, data);

    } catch (EdmException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }
}
