package com.sap.core.odata.core.ep;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.consumer.XmlEntityConsumer;
import com.sap.core.odata.core.ep.producer.AtomEntryEntityProducer;
import com.sap.core.odata.core.ep.producer.AtomFeedProducer;
import com.sap.core.odata.core.ep.producer.AtomServiceDocumentProducer;
import com.sap.core.odata.core.ep.producer.XmlCollectionEntityProducer;
import com.sap.core.odata.core.ep.producer.XmlErrorDocumentProducer;
import com.sap.core.odata.core.ep.producer.XmlLinkEntityProducer;
import com.sap.core.odata.core.ep.producer.XmlLinksEntityProducer;
import com.sap.core.odata.core.ep.producer.XmlPropertyEntityProducer;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * @author SAP AG
 */
public class AtomEntityProvider implements ContentTypeBasedEntityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(AtomEntityProvider.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = ContentType.CHARSET_UTF_8;
  private static final String XML_VERSION = "1.0";
  private final ODataFormat odataFormat;

  public AtomEntityProvider() throws EntityProviderException {
    this(ODataFormat.ATOM);
  }

  public AtomEntityProvider(final ContentType contentType) throws EntityProviderException {
    this(contentType.getODataFormat());
  }

  public AtomEntityProvider(final ODataFormat odataFormat) throws EntityProviderException {
    switch (odataFormat) {
    case ATOM:
    case XML:
      this.odataFormat = odataFormat;
      break;
    default:
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent("Got unsupported ODataFormat '" + odataFormat + "'."));
    }
  }

  /**
   * <p>Serializes an error message according to the OData standard.</p>
   * <p>In case an error occurs, it is logged.
   * An exception is not thrown because this method is used in exception handling.</p>
   * @param status      the {@link HttpStatusCodes} associated with this error  
   * @param errorCode   a String that serves as a substatus to the HTTP response code
   * @param message     a human-readable message describing the error
   * @param locale      the {@link Locale} that should be used to format the error message
   * @param innerError  the inner error for this message. If it is null or an empty String no inner error tag is shown inside the response xml
   * @return            an {@link ODataResponse} containing the serialized error message
   */
  @Override
  public ODataResponse writeErrorDocument(final HttpStatusCodes status, final String errorCode, final String message, final Locale locale, final String innerError) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);

      XmlErrorDocumentProducer producer = new XmlErrorDocumentProducer();
      producer.writeErrorDocument(writer, errorCode, message, locale, innerError);

      writer.flush();
      outStream.flush();
      outStream.close();

      ODataResponseBuilder response = ODataResponse.entity(csb.getInputStream())
          .contentHeader(ContentType.APPLICATION_XML.toContentTypeString())
          .header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10)
          .status(status);
      return response.build();
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

  /**
   * Write service document based on given {@link Edm} and <code>service root</code> as
   * content type "<code>application/atomsvc+xml; charset=utf-8</code>".
   * 
   * @param edm the Entity Data Model
   * @param serviceRoot the root URI of the service
   * @return resulting {@link ODataResponse} with written service document
   * @throws EntityProviderException
   */
  @Override
  public ODataResponse writeServiceDocument(final Edm edm, final String serviceRoot) throws EntityProviderException {
    OutputStreamWriter writer = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      OutputStream outputStream = csb.getOutputStream();
      writer = new OutputStreamWriter(outputStream, DEFAULT_CHARSET);
      AtomServiceDocumentProducer as = new AtomServiceDocumentProducer(edm, serviceRoot);
      as.writeServiceDocument(writer);

      ODataResponse response = ODataResponse.entity(csb.getInputStream())
          .contentHeader(ContentType.APPLICATION_ATOM_SVC_CS_UTF_8.toContentTypeString())
          .build();
      
      return response;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
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
  public ODataResponse writeEntry(final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument(DEFAULT_CHARSET, XML_VERSION);

      AtomEntryEntityProducer as = new AtomEntryEntityProducer(properties);
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
      as.append(writer, eia, data, true, false);

      writer.flush();
      outStream.flush();
      outStream.close();

      ODataResponseBuilder response = ODataResponse.entity(csb.getInputStream())
          .contentHeader(getContentHeader(ContentType.APPLICATION_ATOM_XML_ENTRY))
          .eTag(as.getETag())
          .idLiteral(as.getLocation());
      return response.build();
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
  public ODataResponse writeProperty(final EdmProperty edmProperty, final Object value) throws EntityProviderException {
    EntityPropertyInfo propertyInfo = EntityInfoAggregator.create(edmProperty);
    return writeSingleTypedElement(propertyInfo, value);
  }

  private ODataResponse writeSingleTypedElement(final EntityPropertyInfo propertyInfo, final Object value) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument(DEFAULT_CHARSET, XML_VERSION);

      XmlPropertyEntityProducer ps = new XmlPropertyEntityProducer();
      ps.append(writer, propertyInfo, value);

      writer.flush();
      outStream.flush();
      outStream.close();

      ODataResponse response = ODataResponse.entity(csb.getInputStream()).contentHeader(getContentHeader(ContentType.APPLICATION_XML)).build();
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
  public ODataResponse writeFeed(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      outStream = csb.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument(DEFAULT_CHARSET, XML_VERSION);

      AtomFeedProducer atomFeedProvider = new AtomFeedProducer(properties);
      //EdmEntitySet entitySet = entitySetView.getTargetEntitySet();
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
      atomFeedProvider.append(writer, eia, data);

      writer.flush();
      outStream.flush();
      outStream.close();

      ODataResponse response = ODataResponse.entity(csb.getInputStream()).contentHeader(getContentHeader(ContentType.APPLICATION_ATOM_XML_FEED)).build();
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

  private String getContentHeader(final ContentType mediaType) {
    if (odataFormat == ODataFormat.XML) {
      return ContentType.APPLICATION_XML_CS_UTF_8.toContentTypeString();
    }
    return ContentType.create(mediaType, ContentType.PARAMETER_CHARSET, DEFAULT_CHARSET).toContentTypeString();
  }

  @Override
  public ODataResponse writeLink(final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();

    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument(DEFAULT_CHARSET, XML_VERSION);

      XmlLinkEntityProducer entity = new XmlLinkEntityProducer(properties);
      final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
      entity.append(writer, entityInfo, data, true);

      writer.flush();
      outStream.flush();
      outStream.close();
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

    return ODataResponse.entity(buffer.getInputStream()).contentHeader(getContentHeader(ContentType.APPLICATION_XML)).build();
  }

  @Override
  public ODataResponse writeLinks(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();

    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument(DEFAULT_CHARSET, XML_VERSION);

      XmlLinksEntityProducer entity = new XmlLinksEntityProducer(properties);
      final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
      entity.append(writer, entityInfo, data);

      writer.flush();
      outStream.flush();
      outStream.close();
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

    return ODataResponse.entity(buffer.getInputStream()).contentHeader(getContentHeader(ContentType.APPLICATION_XML)).build();
  }

  private ODataResponse writeCollection(final EntityPropertyInfo propertyInfo, final List<?> data) throws EntityProviderException {
    OutputStream outStream = null;

    try {
      CircleStreamBuffer buffer = new CircleStreamBuffer();
      outStream = buffer.getOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream, DEFAULT_CHARSET);
      writer.writeStartDocument(DEFAULT_CHARSET, XML_VERSION);

      XmlCollectionEntityProducer.append(writer, propertyInfo, data);

      writer.flush();
      outStream.flush();
      outStream.close();

      return ODataResponse.entity(buffer.getInputStream()).contentHeader(getContentHeader(ContentType.APPLICATION_XML)).build();
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
  public ODataResponse writeFunctionImport(final EdmFunctionImport functionImport, final Object data, final EntityProviderWriteProperties properties) throws EntityProviderException {
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

  @Override
  public ODataEntry readEntry(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    return xec.readEntry(entitySet, content, properties);
  }

  @Override
  public Map<String, Object> readProperty(final EdmProperty edmProperty, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    return xec.readProperty(edmProperty, content, properties);
  }

  @Override
  public String readLink(final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    return xec.readLink(entitySet, content);
  }

  @Override
  public List<String> readLinks(final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    return xec.readLinks(entitySet, content);
  }
}
