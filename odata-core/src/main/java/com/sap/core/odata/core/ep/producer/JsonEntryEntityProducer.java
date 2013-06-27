package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.OnWriteEntryContent;
import com.sap.core.odata.api.ep.callback.OnWriteFeedContent;
import com.sap.core.odata.api.ep.callback.WriteCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.core.commons.Encoder;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Producer for writing an entity in JSON, also usable for function imports
 * returning a single instance of an entity type.
 * @author SAP AG
 */
public class JsonEntryEntityProducer {

  private final EntityProviderWriteProperties properties;
  private String eTag;
  private String location;
  private JsonStreamWriter jsonStreamWriter;

  public JsonEntryEntityProducer(final EntityProviderWriteProperties properties) throws EntityProviderException {
    this.properties = properties == null ? EntityProviderWriteProperties.serviceRoot(null).build() : properties;
  }

  public void append(final Writer writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data, final boolean isRootElement) throws EntityProviderException {
    final EdmEntityType type = entityInfo.getEntityType();

    try {
      jsonStreamWriter = new JsonStreamWriter(writer);
      if (isRootElement)
        jsonStreamWriter.beginObject().name(FormatJson.D);

      jsonStreamWriter.beginObject();

      jsonStreamWriter.name(FormatJson.METADATA)
          .beginObject();
      final String self = AtomEntryEntityProducer.createSelfLink(entityInfo, data, null);
      location = (properties.getServiceRoot() == null ? "" : properties.getServiceRoot().toASCIIString()) + self;
      jsonStreamWriter.namedStringValue(FormatJson.ID, location).separator()
          .namedStringValue(FormatJson.URI, location).separator()
          .namedStringValueRaw(FormatJson.TYPE,
              type.getNamespace() + Edm.DELIMITER + type.getName());
      eTag = AtomEntryEntityProducer.createETag(entityInfo, data);
      if (eTag != null)
        jsonStreamWriter.separator()
            .namedStringValue(FormatJson.ETAG, eTag);
      if (type.hasStream())
        jsonStreamWriter.separator()
            .namedStringValueRaw(FormatJson.CONTENT_TYPE,
                properties.getMediaResourceMimeType() == null ?
                    type.getMapping() == null || type.getMapping().getMimeType() == null || data.get(type.getMapping().getMimeType()) == null ?
                        HttpContentType.APPLICATION_OCTET_STREAM : data.get(type.getMapping().getMimeType()).toString() :
                    properties.getMediaResourceMimeType())
            .separator()
            .namedStringValue(FormatJson.MEDIA_SRC, self + "/$value").separator()
            .namedStringValue(FormatJson.EDIT_MEDIA, location + "/$value");
      jsonStreamWriter.endObject();

      for (final String propertyName : type.getPropertyNames()) {
        if (entityInfo.getSelectedPropertyNames().contains(propertyName)) {
          jsonStreamWriter.separator()
              .name(propertyName);
          JsonPropertyEntityProducer.appendPropertyValue(jsonStreamWriter, entityInfo.getPropertyInfo(propertyName), data.get(propertyName));
        }
      }

      for (final String navigationPropertyName : type.getNavigationPropertyNames()) {
        if (entityInfo.getSelectedNavigationPropertyNames().contains(navigationPropertyName)) {
          jsonStreamWriter.separator()
              .name(navigationPropertyName);
          if (entityInfo.getExpandedNavigationPropertyNames().contains(navigationPropertyName)) {
            if (properties.getCallbacks() != null && properties.getCallbacks().containsKey(navigationPropertyName)) {
              final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) type.getProperty(navigationPropertyName);
              final boolean isFeed = navigationProperty.getMultiplicity() == EdmMultiplicity.MANY;
              final EdmEntitySet entitySet = entityInfo.getEntitySet();
              final EdmEntitySet inlineEntitySet = entitySet.getRelatedEntitySet(navigationProperty);

              WriteCallbackContext context = isFeed ? new WriteFeedCallbackContext() : new WriteEntryCallbackContext();
              context.setSourceEntitySet(entitySet);
              context.setNavigationProperty(navigationProperty);
              context.setEntryData(data);
              context.setCurrentExpandSelectTreeNode(properties.getExpandSelectTree().getLinks().get(navigationPropertyName));

              ODataCallback callback = properties.getCallbacks().get(navigationPropertyName);
              if (callback == null) {
                throw new EntityProviderException(EntityProviderException.EXPANDNOTSUPPORTED);
              }
              try {
                if (isFeed) {
                  final WriteFeedCallbackResult result = ((OnWriteFeedContent) callback).retrieveFeedResult((WriteFeedCallbackContext) context);
                  List<Map<String, Object>> inlineData = result.getFeedData();
                  if (inlineData != null) {
                    final EntityProviderWriteProperties inlineProperties = result.getInlineProperties();
                    final EntityInfoAggregator inlineEntityInfo = EntityInfoAggregator.create(inlineEntitySet, inlineProperties.getExpandSelectTree());
                    new JsonFeedEntityProducer(inlineProperties).append(writer, inlineEntityInfo, inlineData, false);
                  }
                } else {
                  final WriteEntryCallbackResult result = ((OnWriteEntryContent) callback).retrieveEntryResult((WriteEntryCallbackContext) context);
                  Map<String, Object> inlineData = result.getEntryData();
                  if (inlineData != null) {
                    final EntityProviderWriteProperties inlineProperties = result.getInlineProperties();
                    final EntityInfoAggregator inlineEntityInfo = EntityInfoAggregator.create(inlineEntitySet, inlineProperties.getExpandSelectTree());
                    new JsonEntryEntityProducer(inlineProperties).append(writer, inlineEntityInfo, inlineData, false);
                  }
                }
              } catch (final ODataApplicationException e) {
                throw new EntityProviderException(EntityProviderException.COMMON, e);
              }
            } else {
              writeDeferredUri(navigationPropertyName);
            }
          } else {
            writeDeferredUri(navigationPropertyName);
          }
        }
      }

      jsonStreamWriter.endObject();

      if (isRootElement)
        jsonStreamWriter.endObject();

      writer.flush();

    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void writeDeferredUri(final String navigationPropertyName) throws IOException {
    jsonStreamWriter.beginObject()
        .name(FormatJson.DEFERRED);
    JsonLinkEntityProducer.appendUri(jsonStreamWriter, location + "/" + Encoder.encode(navigationPropertyName));
    jsonStreamWriter.endObject();
  }

  public String getETag() {
    return eTag;
  }

  public String getLocation() {
    return location;
  }
}
