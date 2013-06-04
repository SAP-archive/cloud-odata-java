package com.sap.core.odata.core.ep.producer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatXml;

public class TombstoneProducer implements ODataCallback {

  private String defaultDateString;

  public static final String TOMBSTONE_NAMESPACE_PREFIX = "at";
  public static final String TOMBSTONE_NAMESPACE = "http://purl.org/atompub/tombstones/1.0";

  private static final String WHEN = "when";
  private static final String REF = "ref";

  List<Map<String, Object>> deletedEntries = new ArrayList<Map<String, Object>>();

  public TombstoneProducer(final List<Map<String, Object>> deletedEntries) {
    this.deletedEntries = deletedEntries;
  }

  public void write(final XMLStreamWriter writer, final EntityInfoAggregator eia, final EntityProviderWriteProperties properties) throws EntityProviderException {
    try {
      for (Map<String, Object> deletedEntry : deletedEntries) {
        writer.writeStartElement(TOMBSTONE_NAMESPACE, FormatXml.ATOM_TOMBSTONE_DELETED_ENTRY);
        // "ref" attribute whose value specifies the value of the atom:id of the entry that has been removed
        String ref = properties.getServiceRoot().toASCIIString() + AtomEntryEntityProducer.createSelfLink(eia, deletedEntry, null);
        writer.writeAttribute(REF, ref);

        // "when" attribute whose value is an "date-time", specifying the instant the entry was remove. 
        Object updateDate = null;
        EntityPropertyInfo updatedInfo = eia.getTargetPathInfo(EdmTargetPath.SYNDICATION_UPDATED);
        if (updatedInfo != null) {
          updateDate = deletedEntry.get(updatedInfo.getName());
        }

        if (updateDate == null) {
          writeDefaultWhenAttribute(writer);
        } else {
          EdmFacets updateFacets = updatedInfo.getFacets();
          writer.writeAttribute(WHEN, EdmDateTimeOffset.getInstance().valueToString(updateDate, EdmLiteralKind.DEFAULT, updateFacets));
        }

        writer.writeEndElement();
      }
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmSimpleTypeException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void writeDefaultWhenAttribute(final XMLStreamWriter writer) throws XMLStreamException, EdmSimpleTypeException {
    if (defaultDateString == null) {
      Object defaultDate = new Date();
      defaultDateString = EdmDateTimeOffset.getInstance().valueToString(defaultDate, EdmLiteralKind.DEFAULT, null);
    }
    writer.writeAttribute(WHEN, defaultDateString);

  }
}
