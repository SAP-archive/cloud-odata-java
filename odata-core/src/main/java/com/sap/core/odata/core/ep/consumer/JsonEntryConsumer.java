package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.NavigationPropertyInfo;
import com.sap.core.odata.core.ep.entry.EntryMetadataImpl;
import com.sap.core.odata.core.ep.entry.MediaMetadataImpl;
import com.sap.core.odata.core.ep.entry.ODataEntryImpl;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.uri.ExpandSelectTreeNodeImpl;

public class JsonEntryConsumer {

  private ODataEntryImpl readEntryResult;
  private Map<String, Object> properties;
  private MediaMetadataImpl mediaMetadata;
  private EntryMetadataImpl entryMetadata;
  private ExpandSelectTreeNodeImpl expandSelectTree;
  private Map<String,Object> typeMappings;
  private int openJsonObjects;

  public ODataEntry readEntry(final JsonReader reader, final EntityInfoAggregator eia, final boolean merge) throws EntityProviderException {
    EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(merge).build();
    return readEntry(reader, eia, properties);
  }

  public ODataEntry readEntry(final JsonReader reader, final EntityInfoAggregator eia, final EntityProviderReadProperties readProperties) throws EntityProviderException {
    initialize(readProperties);

    try {
      openJsonObjects = FormatJson.startJson(reader);

      readEntryContent(reader, eia);

      FormatJson.endJson(reader, openJsonObjects);
    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

    return readEntryResult;
  }

  /**
   * Initialize the {@link XmlEntryConsumer} to be ready for read of an entry.
   * 
   * @param readProperties
   * @throws EntityProviderException
   */
  private void initialize(final EntityProviderReadProperties readProperties) throws EntityProviderException {
    properties = new HashMap<String, Object>();
    mediaMetadata = new MediaMetadataImpl();
    entryMetadata = new EntryMetadataImpl();
    expandSelectTree = new ExpandSelectTreeNodeImpl();

    readEntryResult = new ODataEntryImpl(properties, mediaMetadata, entryMetadata, expandSelectTree);
    typeMappings = readProperties.getTypeMappings();
    openJsonObjects = 0;
  }

  private void readEntryContent(JsonReader reader, EntityInfoAggregator eia) throws IOException, EdmException, EntityProviderException {

    while (reader.hasNext()) {
      String name = reader.nextName();

      if (FormatJson.METADATA.equals(name)) {
        readMetadata(reader, eia);
        validateMetadata(eia);
      } else {
 
        EntityPropertyInfo propertyInfo = eia.getPropertyInfo(name);
        if (propertyInfo != null) {
          JsonPropertyConsumer jpc = new JsonPropertyConsumer();
          Object propertyValue = jpc.readProperty(reader, propertyInfo, typeMappings.get(name));
          properties.put(name, propertyValue);
        } else {
          NavigationPropertyInfo navigationPropertyInfo = eia.getNavigationPropertyInfo(name);
          if (navigationPropertyInfo != null) {
            readNavigationProperty(reader, navigationPropertyInfo);
          } else {
            throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent(name));
          }
        }
      }
    }
  }

  private void readMetadata(JsonReader reader, EntityInfoAggregator eia) throws IOException, EdmException, EntityProviderException {
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

  private void validateMetadata(EntityInfoAggregator eia) throws EdmException, EntityProviderException {
    if (eia.getEntityType().hasStream()) {
      if (mediaMetadata.getSourceLink() == null) {
        throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(FormatJson.MEDIA_SRC).addContent(FormatJson.METADATA));
      }
      if (mediaMetadata.getContentType() == null) {
        throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(FormatJson.CONTENT_TYPE).addContent(FormatJson.METADATA));
      }
      //TODO Mime Type Mapping
    } else {
     if(mediaMetadata.getContentType() != null || mediaMetadata.getEditLink() != null 
         || mediaMetadata.getEtag() != null || mediaMetadata.getSourceLink() != null){
       throw new EntityProviderException(EntityProviderException.MEDIA_DATA_NOT_INITIAL);
     }
    }
  }

  private void readNavigationProperty(JsonReader reader, NavigationPropertyInfo navigationPropertyInfo) throws IOException, EntityProviderException {
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
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent(name));
    }

    reader.endObject();
  }
}