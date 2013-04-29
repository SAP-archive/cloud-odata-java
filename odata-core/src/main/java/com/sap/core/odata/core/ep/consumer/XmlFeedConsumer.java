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

      // read feed data (metadata and entries)
      return readFeedData(reader, eia, entryReadProperties);
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
  

  /**
   * Read all feed specific data (like <code>inline count</code> and <code>next link</code>) as well as all feed entries (<code>entry</code>).
   * 
   * @param reader
   * @param eia
   * @param entryReadProperties
   * @return
   * @throws XMLStreamException
   * @throws EntityProviderException
   */
  private ODataFeed readFeedData(final XMLStreamReader reader, final EntityInfoAggregator eia, EntityProviderReadProperties entryReadProperties) throws XMLStreamException, EntityProviderException {
    FeedMetadataImpl metadata = new FeedMetadataImpl();
    XmlEntryConsumer xec = new XmlEntryConsumer();
    List<ODataEntry> results = new ArrayList<ODataEntry>();

    while (reader.hasNext() && !isFeedEndTag(reader)) {
      if(FormatXml.ATOM_ENTRY.equals(reader.getLocalName())) {
        ODataEntry entry = xec.readEntry(reader, eia, entryReadProperties);
        results.add(entry);
      } else if(FormatXml.M_COUNT.equals(reader.getLocalName())) {
        reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_M_2007_08, FormatXml.M_COUNT);
        
        reader.next();
        if(reader.hasText()) {
          String inlineCount = reader.getText();
          metadata.setInlineCount(Integer.valueOf(inlineCount));
        } 
      } else if(FormatXml.ATOM_LINK.equals(reader.getLocalName())) {
        reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_LINK);

        final String rel = reader.getAttributeValue(null, FormatXml.ATOM_REL);
        if(FormatXml.ATOM_NEXT_LINK.equals(rel)) {
          final String uri = reader.getAttributeValue(null, FormatXml.ATOM_HREF);
          metadata.setNextLink(uri);
        }
        
        reader.next();
      } else {
        reader.next();
      }
      readTillNextStartTag(reader);
    }
    return new ODataFeedImpl(results, metadata);
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
