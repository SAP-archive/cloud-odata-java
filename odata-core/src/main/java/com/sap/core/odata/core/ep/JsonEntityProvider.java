package com.sap.core.odata.core.ep;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.producer.JsonErrorDocumentProducer;
import com.sap.core.odata.core.ep.producer.JsonLinkEntityProducer;
import com.sap.core.odata.core.ep.producer.JsonLinksEntityProducer;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * @author SAP AG
 */
public class JsonEntityProvider implements ContentTypeBasedEntityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(JsonEntityProvider.class);
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

    try {
      OutputStreamWriter writer = new OutputStreamWriter(outStream, DEFAULT_CHARSET);
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
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (outStream != null)
        try {
          outStream.close();
        } catch (final IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
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
    return null;
  }

  @Override
  public ODataResponse writeEntry(final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderProperties properties) throws EntityProviderException {
    return null;
  }

  @Override
  public ODataResponse writeProperty(final EdmProperty edmProperty, final Object value) throws EntityProviderException {
    EntityPropertyInfo propertyInfo = EntityInfoAggregator.create(edmProperty);
    return writeSingleTypedElement(propertyInfo, value);
  }

  private ODataResponse writeSingleTypedElement(final EntityPropertyInfo propertyInfo, final Object value) throws EntityProviderException {
    return null;
  }

  @Override
  public ODataResponse writeFeed(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderProperties properties) throws EntityProviderException {
    return null;
  }

  @Override
  public ODataResponse writeLink(final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderProperties properties) throws EntityProviderException {
    final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();

    try {
      OutputStreamWriter writer = new OutputStreamWriter(outStream, DEFAULT_CHARSET);
      new JsonLinkEntityProducer(properties).append(writer, entityInfo, data);
      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (outStream != null)
        try {
          outStream.close();
        } catch (final IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
    }

    return ODataResponse.entity(buffer.getInputStream())
        .contentHeader(HttpContentType.APPLICATION_JSON)
        .header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10)
        .build();
  }

  @Override
  public ODataResponse writeLinks(final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderProperties properties) throws EntityProviderException {
    final EntityInfoAggregator entityInfo = EntityInfoAggregator.create(entitySet, properties.getExpandSelectTree());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    OutputStream outStream = buffer.getOutputStream();

    try {
      OutputStreamWriter writer = new OutputStreamWriter(outStream, DEFAULT_CHARSET);
      new JsonLinksEntityProducer(properties).append(writer, entityInfo, data);
      writer.flush();
      outStream.flush();
      outStream.close();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (final IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }

    ODataResponseBuilder response = ODataResponse.entity(buffer.getInputStream()).contentHeader(HttpContentType.APPLICATION_JSON);
    if (properties.getInlineCountType() != InlineCount.ALLPAGES)
      response = response.header(ODataHttpHeaders.DATASERVICEVERSION, ODataServiceVersion.V10);
    return response.build();
  }

  private ODataResponse writeCollection(final EntityPropertyInfo propertyInfo, final List<?> data) throws EntityProviderException {
    return null;
  }

  @Override
  public ODataResponse writeFunctionImport(final EdmFunctionImport functionImport, final Object data, final EntityProviderProperties properties) throws EntityProviderException {
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
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  @Override
  public ODataEntry readEntry(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    return null;
  }

  @Override
  public Map<String, Object> readProperty(final EdmProperty edmProperty, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    return null;
  }

  @Override
  public String readLink(final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    return null;
  }

  @Override
  public List<String> readLinks(final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    return null;
  }
}
