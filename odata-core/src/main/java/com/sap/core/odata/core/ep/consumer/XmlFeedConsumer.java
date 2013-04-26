package com.sap.core.odata.core.ep.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.feed.FeedMetadataImpl;
import com.sap.core.odata.core.ep.feed.ODataFeedImpl;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Atom/XML format reader/consumer for feeds.
 * 
 * {@link XmlFeedConsumer} instance use {@link XmlEntryConsumer#readEntry(XMLStreamReader, EntityInfoAggregator, EntityProviderReadProperties)} 
 * for read/consume of several entries.
 * 
 * @author SAP AG
 */
public class XmlFeedConsumer {

  /**
   * 
   * @param reader
   * @param eia
   * @param readProperties
   * @return {@link ODataFeed} object
   * @throws EntityProviderException
   */
  public ODataFeed readFeed(final XMLStreamReader reader, final EntityInfoAggregator eia, final EntityProviderReadProperties readProperties) throws EntityProviderException {
    try {
      // read xml tag
      reader.require(XMLStreamConstants.START_DOCUMENT, null, null);
      reader.nextTag();

      // read feed tag
      reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_FEED);
      Map<String, String> foundPrefix2NamespaceUri = extractNamespacesFromTag(reader);
      foundPrefix2NamespaceUri.putAll(readProperties.getValidatedPrefixNamespaceUris());
      checkAllMandatoryNamespacesAvailable(foundPrefix2NamespaceUri);
      EntityProviderReadProperties entryReadProperties =
          EntityProviderReadProperties.initFrom(readProperties).addValidatedPrefixes(foundPrefix2NamespaceUri).build();

      // read entries
      XmlEntryConsumer xec = new XmlEntryConsumer();
      List<ODataEntry> results = new ArrayList<ODataEntry>();
      while (reader.hasNext() && !isFeedEndTag(reader)) {
        ODataEntry entry = xec.readEntry(reader, eia, entryReadProperties);
        results.add(entry);
        readTillNextStartTag(reader);
      }
      //TODO: fill ODataFeedImpl with count and nextLink
      return new ODataFeedImpl(results, new FeedMetadataImpl());
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void readTillNextStartTag(final XMLStreamReader reader) throws XMLStreamException {
    while (reader.hasNext() && !reader.isStartElement()) {
      reader.next();
    }
  }

  private boolean isFeedEndTag(final XMLStreamReader reader) {
    return reader.isEndElement()
        && Edm.NAMESPACE_ATOM_2005.equals(reader.getNamespaceURI())
        && FormatXml.ATOM_FEED.equals(reader.getLocalName());
  }

  /**
   * 
   * @param reader
   * @return 
   * @throws EntityProviderException
   */
  private Map<String, String> extractNamespacesFromTag(final XMLStreamReader reader) throws EntityProviderException {
    // collect namespaces
    Map<String, String> foundPrefix2NamespaceUri = new HashMap<String, String>();
    int namespaceCount = reader.getNamespaceCount();
    for (int i = 0; i < namespaceCount; i++) {
      String namespacePrefix = reader.getNamespacePrefix(i);
      String namespaceUri = reader.getNamespaceURI(i);

      foundPrefix2NamespaceUri.put(namespacePrefix, namespaceUri);
    }
    return foundPrefix2NamespaceUri;
  }

  /**
   * 
   * @param foundPrefix2NamespaceUri 
   * @throws EntityProviderException
   */
  private void checkAllMandatoryNamespacesAvailable(final Map<String, String> foundPrefix2NamespaceUri) throws EntityProviderException {
    if (!foundPrefix2NamespaceUri.containsValue(Edm.NAMESPACE_D_2007_08)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_D_2007_08));
    } else if (!foundPrefix2NamespaceUri.containsValue(Edm.NAMESPACE_M_2007_08)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_M_2007_08));
    } else if (!foundPrefix2NamespaceUri.containsValue(Edm.NAMESPACE_ATOM_2005)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_ATOM_2005));
    }
  }
}
