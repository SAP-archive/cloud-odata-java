package com.sap.core.odata.core.ep;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProvider.EntityProviderInterface;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;

/**
 * @author SAP AG
 */
public class ProviderFacadeImpl implements EntityProviderInterface {

  private static BasicEntityProvider create() throws EntityProviderException {
    return new BasicEntityProvider();
  }

  private static ContentTypeBasedEntityProvider create(final String contentType) throws EntityProviderException {
    return create(ContentType.create(contentType));
  }

  private static ContentTypeBasedEntityProvider create(final ContentType contentType) throws EntityProviderException {
    try {
      ContentTypeBasedEntityProvider provider;

      switch (contentType.getODataFormat()) {
      case ATOM:
      case XML:
        provider = new AtomEntityProvider(contentType.getODataFormat());
        break;
      case JSON:
        // mibo_130125: currently JSON is a not supported content type
        // -> 'throw NOT_SUPPORTED_CONTENT_TYPE' as 'ODataNotAcceptableException'
        throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(contentType));
      default:
        throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(contentType));
      }

      return provider;
    } catch (ODataNotAcceptableException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  @Override
  public ODataResponse writeServiceDocument(final String contentType, final Edm edm, final String serviceRoot) throws EntityProviderException {
    return create(contentType).writeServiceDocument(edm, serviceRoot);
  }

  @Override
  public ODataResponse writePropertyValue(final EdmProperty edmProperty, final Object value) throws EntityProviderException {
    return create().writePropertyValue(edmProperty, value);
  }

  @Override
  public ODataResponse writeText(final String value) throws EntityProviderException {
    return create().writeText(value);
  }

  @Override
  public ODataResponse writeBinary(final String mimeType, final byte[] data) throws EntityProviderException {
    return create().writeBinary(mimeType, data);
  }

  @Override
  public ODataResponse writeFeed(final String contentType, final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderProperties properties) throws EntityProviderException {
    return create(contentType).writeFeed(entitySet, data, properties);
  }

  @Override
  public ODataResponse writeEntry(final String contentType, final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderProperties properties) throws EntityProviderException {
    return create(contentType).writeEntry(entitySet, data, properties);
  }

  @Override
  public ODataResponse writeProperty(final String contentType, final EdmProperty edmProperty, final Object value) throws EntityProviderException {
    return create(contentType).writeProperty(edmProperty, value);
  }

  @Override
  public ODataResponse writeLink(final String contentType, final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderProperties properties) throws EntityProviderException {
    return create(contentType).writeLink(entitySet, data, properties);
  }

  @Override
  public ODataResponse writeLinks(final String contentType, final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderProperties properties) throws EntityProviderException {
    return create(contentType).writeLinks(entitySet, data, properties);
  }

  @Override
  public ODataResponse writeFunctionImport(final String contentType, final EdmFunctionImport functionImport, final Object data, final EntityProviderProperties properties) throws EntityProviderException {
    return create(contentType).writeFunctionImport(functionImport, data, properties);
  }

  @Override
  public ODataEntry readEntry(final String contentType, final EdmEntitySet entitySet, final InputStream content, final boolean validate, Map<String, Object> typeMappings) throws EntityProviderException {
    return create(contentType).readEntry(entitySet, content, validate, typeMappings);
  }

  @Override
  public Map<String, Object> readProperty(final String contentType, final EdmProperty edmProperty, final InputStream content, final boolean validate, Map<String, Object> typeMappings) throws EntityProviderException {
    return create(contentType).readProperty(edmProperty, content, validate, typeMappings);
  }

  @Override
  public Object readPropertyValue(final EdmProperty edmProperty, final InputStream content, Class<?> typeMapping) throws EntityProviderException {
    return create().readPropertyValue(edmProperty, content, typeMapping);
  }

  @Override
  public List<String> readLinks(final String contentType, final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    return create(contentType).readLinks(entitySet, content);
  }

  @Override
  public String readLink(final String contentType, final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    return create(contentType).readLink(entitySet, content);
  }

  @Override
  public byte[] readBinary(final InputStream content) throws EntityProviderException {
    return create().readBinary(content);
  }

  @Override
  public ODataResponse writeMetadata(final List<Schema> schemas, final Map<String, String> predefinedNamespaces) throws EntityProviderException {
    return create().writeMetadata(schemas, predefinedNamespaces);
  }

}
