package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

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
  private JsonStreamWriter jsonStreamWriter;

  public JsonEntryEntityProducer(final EntityProviderProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(Writer writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data, final boolean isRootElement) throws EntityProviderException {
    try {
      jsonStreamWriter = new JsonStreamWriter(writer);
      if (isRootElement) {
        jsonStreamWriter.beginObject();
        jsonStreamWriter.name(FormatJson.D);
      }

      jsonStreamWriter.beginObject();

      jsonStreamWriter.name(FormatJson.METADATA);
      jsonStreamWriter.beginObject();
      final String self = AtomEntryEntityProducer.createSelfLink(entityInfo, data, null);
      location = properties.getServiceRoot().toASCIIString() + self;
      jsonStreamWriter.namedStringValue(FormatJson.ID, location);
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValue(FormatJson.URI, location);
      jsonStreamWriter.separator();
      final EdmEntityType type = entityInfo.getEntityType();
      jsonStreamWriter.namedStringValueRaw(FormatJson.TYPE,
          type.getNamespace() + Edm.DELIMITER + type.getName());
      eTag = AtomEntryEntityProducer.createETag(entityInfo, data);
      if (eTag != null) {
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValue(FormatJson.ETAG, eTag);
      }
      if (type.hasStream()) {
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValueRaw(FormatJson.CONTENT_TYPE,
            properties.getMediaResourceMimeType() == null ?
                type.getMapping() == null || type.getMapping().getMimeType() == null ?
                    HttpContentType.APPLICATION_OCTET_STREAM : data.get(type.getMapping().getMimeType()).toString() :
                properties.getMediaResourceMimeType());
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValue(FormatJson.MEDIA_SRC, self + "/$value");
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValue(FormatJson.EDIT_MEDIA, location + "/$value");
      }
      jsonStreamWriter.endObject();

      for (final String propertyName : entityInfo.getSelectedPropertyNames()) {
        jsonStreamWriter.separator();
        JsonPropertyEntityProducer.appendProperty(writer, entityInfo.getPropertyInfo(propertyName), data.get(propertyName));
      }

      for (final String navigationPropertyName : entityInfo.getSelectedNavigationPropertyNames()) {
        jsonStreamWriter.separator();
        jsonStreamWriter.name(navigationPropertyName);
        if (entityInfo.getExpandedNavigationPropertyNames().contains(navigationPropertyName)) {
          if (properties.getCallbacks() != null && properties.getCallbacks().containsKey(navigationPropertyName)) {
            properties.getCallbacks().get(navigationPropertyName);
            final NavigationPropertyInfo navigationPropertyInfo = entityInfo.getNavigationPropertyInfo(navigationPropertyName);
            if (navigationPropertyInfo.getMultiplicity() == EdmMultiplicity.MANY) {
              ;
            } else {
              ;
            }
            throw new EntityProviderException(EntityProviderException.EXPANDNOTSUPPORTED);
          }
        } else {
          jsonStreamWriter.beginObject();
          jsonStreamWriter.name(FormatJson.DEFERRED);
          JsonLinkEntityProducer.appendUri(writer, location + "/" + Encoder.encode(navigationPropertyName));
          jsonStreamWriter.endObject();
        }
      }

      jsonStreamWriter.endObject();

      if (isRootElement) {
        jsonStreamWriter.endObject();
      }
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
