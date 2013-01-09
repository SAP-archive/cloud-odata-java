package com.sap.core.odata.core.ep;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * @author SAP AG
 */
public class AtomEntityProvider extends EntityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(AtomEntityProvider.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

  AtomEntityProvider() throws EntityProviderException {
    super();
  }

  @Override
  public ODataResponse writeEntry(EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument();

      AtomEntryEntityProvider as = new AtomEntryEntityProvider(properties);
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      as.append(writer, eia, data, true);

      writer.flush();
      outStream.flush();
      outStream.close();

      ODataResponse response = ODataResponse.entity(csb.getInputStream()).contentHeader(createContentHeader(ContentType.APPLICATION_ATOM_XML_ENTRY)).eTag(as.getETag()).build();
      return response;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
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
  public ODataResponse writeProperty(EdmProperty edmProperty, Object value) throws EntityProviderException {
    EntityPropertyInfo propertyInfo = EntityInfoAggregator.create(edmProperty);
    return writeSingleTypedElement(propertyInfo, value);
  }

  private ODataResponse writeSingleTypedElement(final EntityPropertyInfo propertyInfo, final Object value) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument();

      XmlPropertyEntityProvider ps = new XmlPropertyEntityProvider();
      ps.append(writer, propertyInfo, value);

      writer.flush();
      outStream.flush();
      outStream.close();

      ODataResponse response = ODataResponse.entity(csb.getInputStream()).contentHeader(createContentHeader(ContentType.APPLICATION_XML)).build();
      return response;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
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
  public ODataResponse writeFeed(EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument();

      AtomFeedProvider atomFeedProvider = new AtomFeedProvider(properties);
      //EdmEntitySet entitySet = entitySetView.getTargetEntitySet();
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      atomFeedProvider.append(writer, eia, data);

      writer.flush();
      outStream.flush();
      outStream.close();

      ODataResponse response = ODataResponse.entity(csb.getInputStream()).contentHeader(createContentHeader(ContentType.APPLICATION_ATOM_XML_FEED)).build();
      return response;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
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

  private String createContentHeader(ContentType mediaType) {
    return mediaType.toString() + "; charset=" + DEFAULT_CHARSET;
  }

  @Override
  public ODataResponse writeLink(final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderProperties properties) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();

    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument();

      XmlLinkEntityProvider entity = new XmlLinkEntityProvider(properties);
      final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet);
      entity.append(writer, entityInfo, data, true);

      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (FactoryConfigurationError e1) {
      throw new EntityProviderException(EntityProviderException.COMMON, e1);
    } catch (XMLStreamException e2) {
      throw new EntityProviderException(EntityProviderException.COMMON, e2);
    } catch (IOException e3) {
      throw new EntityProviderException(EntityProviderException.COMMON, e3);
    } finally {
      if (outStream != null)
        try {
          outStream.close();
        } catch (IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
    }

    return ODataResponse.entity(buffer.getInputStream()).contentHeader(createContentHeader(ContentType.APPLICATION_XML)).build();
  }

  @Override
  public ODataResponse writeLinks(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderProperties properties) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();

    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument();

      XmlLinksEntityProvider entity = new XmlLinksEntityProvider(properties);
      final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet);
      entity.append(writer, entityInfo, data);

      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (FactoryConfigurationError e1) {
      throw new EntityProviderException(EntityProviderException.COMMON, e1);
    } catch (XMLStreamException e2) {
      throw new EntityProviderException(EntityProviderException.COMMON, e2);
    } catch (IOException e3) {
      throw new EntityProviderException(EntityProviderException.COMMON, e3);
    } finally {
      if (outStream != null)
        try {
          outStream.close();
        } catch (IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
    }


    return ODataResponse.entity(buffer.getInputStream()).contentHeader(createContentHeader(ContentType.APPLICATION_XML)).build();
  }

  private ODataResponse writeCollection(final EntityPropertyInfo propertyInfo, final List<?> data) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer buffer = new CircleStreamBuffer();
      outStream = buffer.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument();

      XmlCollectionEntityProvider.append(writer, propertyInfo, data);

      writer.flush();
      outStream.flush();
      outStream.close();


      return ODataResponse.entity(buffer.getInputStream()).contentHeader(createContentHeader(ContentType.APPLICATION_XML)).build();
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
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
  public ODataResponse writeFunctionImport(final EdmFunctionImport functionImport, Object data, final EntityProviderProperties properties) throws EntityProviderException {
    try {
      final EdmType type = functionImport.getReturnType().getType();
      final boolean isCollection = functionImport.getReturnType().getMultiplicity() == EdmMultiplicity.MANY;

      if (type.getKind() == EdmTypeKind.ENTITY) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) data;
        return writeEntry(functionImport.getEntitySet(), map, properties);
      }

      final EntityPropertyInfo info = EntityInfoAggregator.create(functionImport);
      if (isCollection) {
        return writeCollection(info, (List<?>) data);
      } else {
        return writeSingleTypedElement(info, data);
      }
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
