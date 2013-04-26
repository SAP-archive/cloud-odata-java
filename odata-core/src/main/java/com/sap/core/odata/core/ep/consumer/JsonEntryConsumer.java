package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.callback.OnReadInlineContent;
import com.sap.core.odata.api.ep.callback.ReadEntryResult;
import com.sap.core.odata.api.ep.callback.ReadFeedResult;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.NavigationPropertyInfo;
import com.sap.core.odata.core.ep.entry.EntryMetadataImpl;
import com.sap.core.odata.core.ep.entry.MediaMetadataImpl;
import com.sap.core.odata.core.ep.entry.ODataEntryImpl;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonUtils;
import com.sap.core.odata.core.uri.ExpandSelectTreeNodeImpl;

public class JsonEntryConsumer {

  private final Map<String, Object> properties = new HashMap<String, Object>();
  private final MediaMetadataImpl mediaMetadata = new MediaMetadataImpl();
  private final EntryMetadataImpl entryMetadata = new EntryMetadataImpl();
  private final ExpandSelectTreeNodeImpl expandSelectTree = new ExpandSelectTreeNodeImpl();
  private final Map<String, Object> typeMappings;
  private final EntityInfoAggregator eia;
  private final JsonReader reader;
  private final EntityProviderReadProperties readProperties;
  private int openJsonObjects = 0;

  public JsonEntryConsumer(final JsonReader reader, final EntityInfoAggregator eia, final EntityProviderReadProperties readProperties) {
    typeMappings = readProperties.getTypeMappings();
    this.eia = eia;
    this.readProperties = readProperties;
    this.reader = reader;
  }

  public ODataEntry readEntryStandalone() throws EntityProviderException {
    try {
      openJsonObjects = JsonUtils.startJson(reader);
      readEntryContent();
      JsonUtils.endJson(reader, openJsonObjects);
    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

    return new ODataEntryImpl(properties, mediaMetadata, entryMetadata, expandSelectTree);
  }
  
  public ODataEntry readEntry() throws IOException, EdmException, EntityProviderException{
    reader.beginObject();
    readEntryContent();
    reader.endObject();
    return new ODataEntryImpl(properties, mediaMetadata, entryMetadata, expandSelectTree);
  }

  private void readEntryContent() throws IOException, EdmException, EntityProviderException {
    while (reader.hasNext()) {
      String name = reader.nextName();
      handleName(name);
    }
  }

  private void handleName(String name) throws IOException, EdmException, EntityProviderException {
    if (FormatJson.METADATA.equals(name)) {
      readMetadata();
      validateMetadata();
    } else {
      EntityPropertyInfo propertyInfo = eia.getPropertyInfo(name);
      if (propertyInfo != null) {
        JsonPropertyConsumer jpc = new JsonPropertyConsumer();
        Object propertyValue = jpc.readProperty(reader, propertyInfo, typeMappings.get(name));
        if (properties.containsKey(name)) {
          throw new EntityProviderException(EntityProviderException.DOUBLE_PROPERTY.addContent(name));
        }
        properties.put(name, propertyValue);
      } else {
        readNavigationProperty(name);
      }
    }
  }

  private void readMetadata() throws IOException, EdmException, EntityProviderException {
    String name = null;
    reader.beginObject();
    while (reader.hasNext()) {
      name = reader.nextName();
      String value = reader.nextString();

      if (FormatJson.ID.equals(name)) {
        entryMetadata.setId(value);
      } else if (FormatJson.URI.equals(name)) {
        entryMetadata.setUri(value);
      } else if (FormatJson.TYPE.equals(name)) {
        String fullQualifiedName = eia.getEntityType().getNamespace() + Edm.DELIMITER + eia.getEntityType().getName();
        if (!fullQualifiedName.equals(value)) {
          throw new EntityProviderException(EntityProviderException.INVALID_ENTITYTYPE.addContent(fullQualifiedName).addContent(value));
        }
      } else if (FormatJson.ETAG.equals(name)) {
        entryMetadata.setEtag(value);
      } else if (FormatJson.EDIT_MEDIA.equals(name)) {
        mediaMetadata.setEditLink(value);
      } else if (FormatJson.MEDIA_SRC.equals(name)) {
        mediaMetadata.setSourceLink(value);
      } else if (FormatJson.MEDIA_ETAG.equals(name)) {
        mediaMetadata.setEtag(value);
      } else if (FormatJson.CONTENT_TYPE.equals(name)) {
        mediaMetadata.setContentType(value);
      } else {
        throw new EntityProviderException(EntityProviderException.INVALID_CONTENT.addContent(name).addContent(FormatJson.METADATA));
      }
    }

    reader.endObject();
  }

  private void validateMetadata() throws EdmException, EntityProviderException {
    if (eia.getEntityType().hasStream()) {
      if (mediaMetadata.getSourceLink() == null) {
        throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(FormatJson.MEDIA_SRC).addContent(FormatJson.METADATA));
      }
      if (mediaMetadata.getContentType() == null) {
        throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(FormatJson.CONTENT_TYPE).addContent(FormatJson.METADATA));
      }
      //TODO Mime Type Mapping
    } else {
      if (mediaMetadata.getContentType() != null || mediaMetadata.getEditLink() != null
          || mediaMetadata.getEtag() != null || mediaMetadata.getSourceLink() != null) {
        throw new EntityProviderException(EntityProviderException.MEDIA_DATA_NOT_INITIAL);
      }
    }
  }

  private void readNavigationProperty(final String navigationPropertyName) throws IOException, EntityProviderException, EdmException {
    NavigationPropertyInfo navigationPropertyInfo = eia.getNavigationPropertyInfo(navigationPropertyName);
    if (navigationPropertyInfo == null) {
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent(navigationPropertyName));
    }

    reader.beginObject();
    String name = reader.nextName();
    if (FormatJson.DEFERRED.equals(name)) {
      reader.beginObject();
      String uri = reader.nextName();
      if (FormatJson.URI.equals(uri)) {
        String value = reader.nextString();
        entryMetadata.putAssociationUri(navigationPropertyInfo.getName(), value);
      } else {
        throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent(uri));
      }
      reader.endObject();
    } else {
      EdmNavigationProperty navigationProperty = (EdmNavigationProperty) eia.getEntityType().getProperty(navigationPropertyName);
      EdmEntitySet inlineEntitySet = eia.getEntitySet().getRelatedEntitySet(navigationProperty);
      EntityInfoAggregator inlineEia = EntityInfoAggregator.create(inlineEntitySet);
      EntityProviderReadProperties inlineReadProperties;
      OnReadInlineContent callback = readProperties.getCallback();
      if (callback == null) {
        inlineReadProperties = EntityProviderReadProperties.init().mergeSemantic(readProperties.getMergeSemantic()).build();

      } else {
        inlineReadProperties = callback.receiveReadProperties(readProperties, navigationProperty);
      }
      
      if(navigationProperty.getMultiplicity() == EdmMultiplicity.ONE){
        JsonEntryConsumer inlineConsumer = new JsonEntryConsumer(reader, inlineEia, inlineReadProperties);
        ODataEntry entry = inlineConsumer.readInlineEntry(name);
        if (callback == null) {
          properties.put(navigationPropertyName, entry);
        } else {
          ReadEntryResult result = new ReadEntryResult(inlineReadProperties, navigationProperty, entry);
          callback.handleReadEntry(result);
        }        
      }else{
        JsonFeedConsumer inlineConsumer = new JsonFeedConsumer(reader, inlineEia, inlineReadProperties);
        ODataFeed feed = inlineConsumer.readInlineFeed(name);
        if (callback == null) {
          properties.put(navigationPropertyName, feed);
        } else {
          ReadFeedResult result = new ReadJsonFeedResult(inlineReadProperties, navigationProperty, feed);
          callback.handleReadFeed(result);
        }
      }
      
    }
    reader.endObject();
  }

  private ODataEntryImpl readInlineEntry(String name) throws EdmException, EntityProviderException, IOException {
    //consume the already started content
    handleName(name);
    //consume the rest of the entry content
    readEntryContent();
    return new ODataEntryImpl(properties, mediaMetadata, entryMetadata, expandSelectTree);
  }
}