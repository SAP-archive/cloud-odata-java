package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.core.commons.Encoder;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.NavigationPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Producer for writing an entity in JSON, also usable for function imports
 * returning a single instance of an entity type.
 * @author SAP AG
 */
public class JsonEntryEntityProducer {

  private final EntityProviderProperties properties;
  private String eTag;
  private String location;

  public JsonEntryEntityProducer(final EntityProviderProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(Writer writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data, final boolean isRootElement) throws EntityProviderException {
    try {
      if (isRootElement) {
        JsonStreamWriter.beginObject(writer);
        JsonStreamWriter.name(writer, FormatJson.D);
      }

      JsonStreamWriter.beginObject(writer);

      JsonStreamWriter.name(writer, FormatJson.METADATA);
      JsonStreamWriter.beginObject(writer);
      final String self = properties.getSelfLink() == null ?
          AtomEntryEntityProducer.createSelfLink(entityInfo, data, null) : properties.getSelfLink().toASCIIString();
      location = properties.getSelfLink() == null ?
          properties.getServiceRoot().toASCIIString() + self : self;
      JsonStreamWriter.namedStringValue(writer, FormatJson.ID, location);
      JsonStreamWriter.separator(writer);
      JsonStreamWriter.namedStringValue(writer, FormatJson.URI, location);
      JsonStreamWriter.separator(writer);
      final EdmEntityType type = entityInfo.getEntityType();
      JsonStreamWriter.namedStringValueRaw(writer, FormatJson.TYPE,
          type.getNamespace() + Edm.DELIMITER + type.getName());
      eTag = AtomEntryEntityProducer.createETag(entityInfo, data);
      if (eTag != null) {
        JsonStreamWriter.separator(writer);
        JsonStreamWriter.namedStringValue(writer, FormatJson.ETAG, eTag);
      }
      if (type.hasStream()) {
        JsonStreamWriter.separator(writer);
        JsonStreamWriter.namedStringValueRaw(writer, FormatJson.CONTENT_TYPE,
            properties.getMediaResourceMimeType() == null ?
                type.getMapping() == null || type.getMapping().getMimeType() == null ?
                    HttpContentType.APPLICATION_OCTET_STREAM : data.get(type.getMapping().getMimeType()).toString() :
                properties.getMediaResourceMimeType());
        JsonStreamWriter.separator(writer);
        JsonStreamWriter.namedStringValue(writer, FormatJson.MEDIA_SRC, self + "/$value");
        JsonStreamWriter.separator(writer);
        JsonStreamWriter.namedStringValue(writer, FormatJson.EDIT_MEDIA, location + "/$value");
      }
      JsonStreamWriter.endObject(writer);

      for (final String propertyName : entityInfo.getSelectedPropertyNames()) {
        JsonStreamWriter.separator(writer);
        JsonPropertyEntityProducer.appendProperty(writer, entityInfo.getPropertyInfo(propertyName), data.get(propertyName));
      }

      for (final String navigationPropertyName : entityInfo.getSelectedNavigationPropertyNames()) {
        JsonStreamWriter.separator(writer);
        JsonStreamWriter.name(writer, navigationPropertyName);
        if (entityInfo.getExpandedNavigationPropertyNames().contains(navigationPropertyName)) {
          if (properties.getCallbacks() != null && properties.getCallbacks().containsKey(navigationPropertyName)) {
            ODataCallback callback = properties.getCallbacks().get(navigationPropertyName);
            final NavigationPropertyInfo navigationPropertyInfo = entityInfo.getNavigationPropertyInfo(navigationPropertyName);
            if (navigationPropertyInfo.getMultiplicity() == EdmMultiplicity.MANY) {
            } else {
            }
            throw new EntityProviderException(EntityProviderException.EXPANDNOTSUPPORTED);
          }
        } else {
          JsonStreamWriter.beginObject(writer);
          JsonStreamWriter.name(writer, FormatJson.DEFERRED);
          JsonStreamWriter.beginObject(writer);
          JsonStreamWriter.namedStringValue(writer, FormatJson.URI, location + "/" + Encoder.encode(navigationPropertyName));
          JsonStreamWriter.endObject(writer);
          JsonStreamWriter.endObject(writer);
        }
      }

      JsonStreamWriter.endObject(writer);

      if (isRootElement)
        JsonStreamWriter.endObject(writer);
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  public String getETag() {
    return eTag;
  }

  public String getLocation() {
    return location;
  }
}
