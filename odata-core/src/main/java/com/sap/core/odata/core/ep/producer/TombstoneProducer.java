package com.sap.core.odata.core.ep.producer;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.TombstoneCallback;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * @author SAP AG
 */
public class TombstoneProducer {

  private String defaultDateString;

  /**
   * Appends tombstones to an already started feed.
   * If the list is empty no elements will be appended.
   * @param writer         same as in feed
   * @param eia            same as in feed
   * @param properties     same as in feed
   * @param deletedEntries data to be appended
   * @throws EntityProviderException
   */
  public void appendTombstones(final XMLStreamWriter writer, final EntityInfoAggregator eia, final EntityProviderWriteProperties properties, final List<Map<String, Object>> deletedEntries) throws EntityProviderException {
    try {
      for (Map<String, Object> deletedEntry : deletedEntries) {
        writer.writeStartElement(TombstoneCallback.NAMESPACE_TOMBSTONE, FormatXml.ATOM_TOMBSTONE_DELETED_ENTRY);

        appendRefAttribute(writer, eia, properties, deletedEntry);
        appendWhenAttribute(writer, eia, deletedEntry);

        writer.writeEndElement();
      }
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmSimpleTypeException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendWhenAttribute(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> deletedEntry) throws XMLStreamException, EdmSimpleTypeException {
    Object updateDate = null;
    EntityPropertyInfo updatedInfo = eia.getTargetPathInfo(EdmTargetPath.SYNDICATION_UPDATED);
    if (updatedInfo != null) {
      updateDate = deletedEntry.get(updatedInfo.getName());
    }

    if (updateDate == null) {
      appendDefaultWhenAttribute(writer);
    } else {
      appendCustomWhenAttribute(writer, updateDate, updatedInfo);
    }
  }

  private void appendCustomWhenAttribute(final XMLStreamWriter writer, final Object updateDate, final EntityPropertyInfo updatedInfo) throws XMLStreamException, EdmSimpleTypeException {
    EdmFacets updateFacets = updatedInfo.getFacets();
    writer.writeAttribute(FormatXml.ATOM_TOMBSTONE_WHEN, EdmDateTimeOffset.getInstance().valueToString(updateDate, EdmLiteralKind.DEFAULT, updateFacets));
  }

  private void appendRefAttribute(final XMLStreamWriter writer, final EntityInfoAggregator eia, final EntityProviderWriteProperties properties, final Map<String, Object> deletedEntry) throws XMLStreamException, EntityProviderException {
    String ref = properties.getServiceRoot().toASCIIString() + AtomEntryEntityProducer.createSelfLink(eia, deletedEntry, null);
    writer.writeAttribute(FormatXml.ATOM_TOMBSTONE_REF, ref);
  }

  private void appendDefaultWhenAttribute(final XMLStreamWriter writer) throws XMLStreamException, EdmSimpleTypeException {
    if (defaultDateString == null) {
      Object defaultDate = new Date();
      defaultDateString = EdmDateTimeOffset.getInstance().valueToString(defaultDate, EdmLiteralKind.DEFAULT, null);
    }
    writer.writeAttribute(FormatXml.ATOM_TOMBSTONE_WHEN, defaultDateString);

  }
}
