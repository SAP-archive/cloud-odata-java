package com.sap.core.odata.core.ep;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.consumer.JsonEntityConsumer;
import com.sap.core.odata.core.ep.producer.JsonCollectionEntityProducer;
import com.sap.core.odata.core.ep.producer.JsonEntryEntityProducer;
import com.sap.core.odata.core.ep.producer.JsonErrorDocumentProducer;
import com.sap.core.odata.core.ep.producer.JsonFeedEntityProducer;
import com.sap.core.odata.core.ep.producer.JsonLinkEntityProducer;
import com.sap.core.odata.core.ep.producer.JsonLinksEntityProducer;
import com.sap.core.odata.core.ep.producer.JsonPropertyEntityProducer;
import com.sap.core.odata.core.ep.producer.JsonServiceDocumentProducer;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * @author SAP AG
 */
public class JsonEntityProvider implements ContentTypeBasedEntityProvider {

  private static final String DEFAULT_CHARSET = "UTF-8";

  /**
   * <p>Serializes an error message according to the OData standard.</p>
   * <p>In case an error occurs, it is logged.
   * An exception is not thrown because this method is used in exception handling.</p>
   * @param status      the {@link HttpStatusCodes status code} associated with this error  
   * @param errorCode   a String that serves as a substatus to the HTTP response code
   * @param message     a human-readable message describing the error
   * @param locale      the {@link Locale} that should be used to format the error message
   * @param innerError  the inner error for this message; if it is null or an empty String
   *                    no inner error tag is shown inside the response structure
   * @return            an {@link ODataResponse} containing the serialized error message
   */
  @Override
  public ODataResponse writeErrorDocument(final HttpStatusCodes status, final String errorCode, final String message, final Locale locale, final String innerError) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      new JsonErrorDocumentProducer().writeErrorDocument(writer, errorCode, message, locale, innerError);
      writer.flush();
      outStream.flush();
      outStream.close();

      return ODataResponse.status(status)
          .entity(buffer.getInputStream())
          .contentHeader(HttpContentType.APPLICATION_JSON)
          .header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10)
          .build();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  /**
   * Writes service document based on given {@link Edm} and <code>service root</code>.
   * @param edm the Entity Data Model
   * @param serviceRoot the root URI of the service
   * @return resulting {@link ODataResponse} with written service document
   * @throws EntityProviderException
   */
  @Override
  public ODataResponse writeServiceDocument(final Edm edm, final String serviceRoot) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      JsonServiceDocumentProducer.writeServiceDocument(writer, edm);
      writer.flush();
      outStream.flush();
      outStream.close();

      return ODataResponse.entity(buffer.getInputStream())
          .contentHeader(HttpContentType.APPLICATION_JSON)
          .header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10)
          .build();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  @Override
  public ODataResponse writeEntry(final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      JsonEntryEntityProducer producer = new JsonEntryEntityProducer(properties);
      producer.append(writer, entityInfo, data, true);
      writer.flush();
      outStream.flush();
      outStream.close();

      return ODataResponse.entity(buffer.getInputStream())
          .contentHeader(HttpContentType.APPLICATION_JSON)
          .eTag(producer.getETag())
          .idLiteral(producer.getLocation())
          .build();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  @Override
  public ODataResponse writeProperty(final EdmProperty edmProperty, final Object value) throws EntityProviderException {
    return writeSingleTypedElement(EntityInfoAggregator.create(edmProperty), value);
  }

  private ODataResponse writeSingleTypedElement(final EntityPropertyInfo propertyInfo, final Object value) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      new JsonPropertyEntityProducer().append(writer, propertyInfo, value);
      writer.flush();
      outStream.flush();
      outStream.close();

      return ODataResponse.entity(buffer.getInputStream())
          .contentHeader(HttpContentType.APPLICATION_JSON)
          .header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10)
          .build();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  @Override
  public ODataResponse writeFeed(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      new JsonFeedEntityProducer(properties).append(writer, entityInfo, data, true);
      writer.flush();
      outStream.flush();
      outStream.close();
      return ODataResponse.entity(buffer.getInputStream()).contentHeader(HttpContentType.APPLICATION_JSON).build();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  @Override
  public ODataResponse writeLink(final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      new JsonLinkEntityProducer(properties).append(writer, entityInfo, data);
      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }

    return ODataResponse.entity(buffer.getInputStream())
        .contentHeader(HttpContentType.APPLICATION_JSON)
        .header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10)
        .build();
  }

  @Override
  public ODataResponse writeLinks(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      new JsonLinksEntityProducer(properties).append(writer, entityInfo, data);
      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }

    ODataResponseBuilder response = ODataResponse.entity(buffer.getInputStream()).contentHeader(HttpContentType.APPLICATION_JSON);
    if (properties.getInlineCountType() != InlineCount.ALLPAGES) {
      response = response.header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10);
    }
    return response.build();
  }

  private ODataResponse writeCollection(final EntityPropertyInfo propertyInfo, final List<?> data) throws EntityProviderException {
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();
    EntityProviderException cachedException = null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, DEFAULT_CHARSET));
      new JsonCollectionEntityProducer().append(writer, propertyInfo, data);
      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (final IOException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }

    return ODataResponse.entity(buffer.getInputStream()).contentHeader(HttpContentType.APPLICATION_JSON).build();
  }

  @Override
  public ODataResponse writeFunctionImport(final EdmFunctionImport functionImport, final Object data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    try {
      if (functionImport.getReturnType().getType().getKind() == EdmTypeKind.ENTITY) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) data;
        return writeEntry(functionImport.getEntitySet(), map, properties);
      }

      final EntityPropertyInfo info = EntityInfoAggregator.create(functionImport);
      if (functionImport.getReturnType().getMultiplicity() == EdmMultiplicity.MANY) {
        return writeCollection(info, (List<?>) data);
      } else {
        return writeSingleTypedElement(info, data);
      }
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  @Override
  public ODataFeed readFeed(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    JsonEntityConsumer jec = new JsonEntityConsumer();
    return jec.readFeed(entitySet, content, properties);
  }

  @Override
  public ODataEntry readEntry(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    JsonEntityConsumer jec = new JsonEntityConsumer();
    return jec.readEntry(entitySet, content, properties);
  }

  @Override
  public Map<String, Object> readProperty(final EdmProperty edmProperty, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    throw new EntityProviderException(EntityProviderException.COMMON, new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(HttpContentType.APPLICATION_JSON)));
  }

  @Override
  public String readLink(final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    throw new EntityProviderException(EntityProviderException.COMMON, new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(HttpContentType.APPLICATION_JSON)));
  }

  @Override
  public List<String> readLinks(final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    throw new EntityProviderException(EntityProviderException.COMMON, new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(HttpContentType.APPLICATION_JSON)));
  }
}
