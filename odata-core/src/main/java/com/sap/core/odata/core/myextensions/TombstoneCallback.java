package com.sap.core.odata.core.myextensions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.core.commons.Encoder;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;

public class TombstoneCallback implements ODataCallback {
  private static final String WHEN = "when";
  private static final String REF = "ref";

  List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();

  public TombstoneCallback(final List<Map<String, Object>> deletedEntries) {
    this.deletedEntries = deletedEntries;
  }

  public void writeNamespace(final XMLStreamWriter writer) throws EntityProviderException {
    try {
      writer.writeNamespace(AtomExtension.TOMBSTONE_NAMESPACE_PREFIX, AtomExtension.TOMBSTONE_NAMESPACE);
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  public void write(final XMLStreamWriter writer, final EntityInfoAggregator eia, final EntityProviderWriteProperties properties) throws EntityProviderException {
    try {

      for (Map<String, Object> deletedEntry : deletedEntries) {
        writer.writeStartElement(AtomExtension.TOMBSTONE_NAMESPACE, AtomExtension.DELETED_ENTRY);
        // "ref" attribute whose value specifies the value of the atom:id of the entry that has been removed
        String ref = properties.getServiceRoot().toASCIIString() + createSelfLink(eia, deletedEntry, null);
        writer.writeAttribute(REF, ref);

        // "when" attribute whose value is an "date-time", specifying the instant the entry was remove. 
        Object updateDate = null;
        EdmFacets updateFacets = null;
        EntityPropertyInfo updatedInfo = eia.getTargetPathInfo(EdmTargetPath.SYNDICATION_UPDATED);
        if (updatedInfo != null) {
          updateDate = deletedEntry.get(updatedInfo.getName());
          if (updateDate != null) {
            updateFacets = updatedInfo.getFacets();
          }
        }
        if (updateDate == null) {
          updateDate = new Date();
        }
        writer.writeAttribute(WHEN, EdmDateTimeOffset.getInstance().valueToString(updateDate, EdmLiteralKind.DEFAULT, updateFacets));
        writer.writeEndElement();
      }
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmSimpleTypeException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

  }

  protected static String createSelfLink(final EntityInfoAggregator eia, final Map<String, Object> data, final String extension) throws EntityProviderException {
    StringBuilder sb = new StringBuilder();
    if (!eia.isDefaultEntityContainer()) {
      sb.append(Encoder.encode(eia.getEntityContainerName())).append(Edm.DELIMITER);
    }
    sb.append(Encoder.encode(eia.getEntitySetName()));

    sb.append("(").append(createEntryKey(eia, data)).append(")").append(extension == null ? "" : ("/" + extension));
    return sb.toString();
  }

  private static String createEntryKey(final EntityInfoAggregator entityInfo, final Map<String, Object> data) throws EntityProviderException {
    final List<EntityPropertyInfo> keyPropertyInfos = entityInfo.getKeyPropertyInfos();

    StringBuilder keys = new StringBuilder();
    for (final EntityPropertyInfo keyPropertyInfo : keyPropertyInfos) {
      if (keys.length() > 0) {
        keys.append(',');
      }

      final String name = keyPropertyInfo.getName();
      if (keyPropertyInfos.size() > 1) {
        keys.append(Encoder.encode(name)).append('=');
      }

      final EdmSimpleType type = (EdmSimpleType) keyPropertyInfo.getType();
      try {
        keys.append(Encoder.encode(type.valueToString(data.get(name), EdmLiteralKind.URI, keyPropertyInfo.getFacets())));
      } catch (final EdmSimpleTypeException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
    }

    return keys.toString();
  }

  public List<Map<String, Object>> getDeletedEntries() {
    return deletedEntries;
  }

}
