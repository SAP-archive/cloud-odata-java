package com.sap.core.odata.core.ep.consumer;

import static com.sap.core.odata.core.ep.util.FormatXml.ATOM_CONTENT;
import static com.sap.core.odata.core.ep.util.FormatXml.ATOM_ENTRY;
import static com.sap.core.odata.core.ep.util.FormatXml.ATOM_HREF;
import static com.sap.core.odata.core.ep.util.FormatXml.ATOM_ID;
import static com.sap.core.odata.core.ep.util.FormatXml.ATOM_LINK;
import static com.sap.core.odata.core.ep.util.FormatXml.ATOM_REL;
import static com.sap.core.odata.core.ep.util.FormatXml.ATOM_SRC;
import static com.sap.core.odata.core.ep.util.FormatXml.M_ETAG;
import static com.sap.core.odata.core.ep.util.FormatXml.M_PROPERTIES;
import static com.sap.core.odata.core.ep.util.FormatXml.M_TYPE;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.entry.EntryMetadataImpl;
import com.sap.core.odata.core.ep.entry.MediaMetadataImpl;
import com.sap.core.odata.core.ep.entry.ODataEntryImpl;

/**
 * Atom/XML format reader/consumer for entries.
 */
public class XmlEntryConsumer {

  final ODataEntryImpl readEntryResult;
  final Map<String, Object> properties;
  final MediaMetadataImpl mediaMetadata;
  final EntryMetadataImpl entryMetadata;

  public XmlEntryConsumer() {
    properties = new HashMap<String, Object>();
    mediaMetadata = new MediaMetadataImpl();
    entryMetadata = new EntryMetadataImpl();
    readEntryResult = new ODataEntryImpl(properties, mediaMetadata, entryMetadata);
  }

  public ODataEntry readEntry(XMLStreamReader reader, EntityInfoAggregator eia, boolean merge) throws EntityProviderException {
    try {
      int eventType;
      while ((eventType = reader.next()) != XMLStreamReader.END_DOCUMENT) {
        if (eventType == XMLStreamReader.START_ELEMENT) {
          String tagName = reader.getLocalName();
          handleStartedTag(reader, tagName, eia);
        }
      }

      if(!merge) {
        readEntryResult.validate(eia);
      }
      
      return readEntryResult;
    } catch (EntityProviderException e) {
      throw e;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /**
   * 
   * @param reader
   * @param tagName
   * @param eia 
   * @throws EntityProviderException
   * @throws XMLStreamException
   * @throws EdmException
   */
  private void handleStartedTag(XMLStreamReader reader, String tagName, EntityInfoAggregator eia) throws EntityProviderException, XMLStreamException, EdmException {
    if (ATOM_ID.equals(tagName)) {
      readId(reader);
    } else if (ATOM_ENTRY.equals(tagName)) {
      readEntry(reader);
    } else if (ATOM_LINK.equals(tagName)) {
      readLink(reader);
    } else if (ATOM_CONTENT.equals(tagName)) {
      readContent(reader, eia);
    } else if (M_PROPERTIES.equals(tagName)) {
      readProperties(reader, eia);
    } else {
      readCustomElement(reader, tagName, eia);
    }
  }

  private void readCustomElement(XMLStreamReader reader, String tagName, EntityInfoAggregator eia) throws EdmException, EntityProviderException, XMLStreamException {
    EntityPropertyInfo targetPathInfo = eia.getTargetPathInfo(tagName);
    if (targetPathInfo != null) {
      String edmPrefix = targetPathInfo.getCustomMapping().getFcNsPrefix();
      String edmNamespaceURI = targetPathInfo.getCustomMapping().getFcNsUri();
      XmlPropertyConsumer xpc = new XmlPropertyConsumer();

      NamespaceContext nsctx = reader.getNamespaceContext();
      if (edmPrefix != null && edmNamespaceURI != null) {
        String xmlPrefix = nsctx.getPrefix(edmNamespaceURI);
        String xmlNamespaceUri = reader.getNamespaceURI(edmPrefix);

        if (edmNamespaceURI.equals(xmlNamespaceUri) && edmPrefix.equals(xmlPrefix)) {
          String name = reader.getLocalName();
          Object value = xpc.readStartedElement(reader, eia.getPropertyInfo(name));
          properties.put(name, value);
        }
      }

    }
  }

  private void readEntry(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, ATOM_ENTRY);
    Map<String, String> attributes = readAttributes(reader);

    String etag = attributes.get(M_ETAG);
    entryMetadata.setEtag(etag);
  }

  /**
   * 
   * @param reader
   * @throws EntityProviderException
   * @throws XMLStreamException
   */
  private void readLink(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, ATOM_LINK);
    Map<String, String> attributes = readAttributes(reader);
    readAndValidateEndPosition(reader, ATOM_LINK);

    String uri = attributes.get(ATOM_HREF);
    String rel = attributes.get(ATOM_REL);

    if (rel == null || uri == null) {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(
          "'" + ATOM_HREF + "' and/or '" + ATOM_REL + "' at tag '" + ATOM_LINK + "'"));
    } else if (rel.startsWith(Edm.NAMESPACE_REL_2007_08)) {
      String navigationPropertyName = rel.substring(Edm.NAMESPACE_REL_2007_08.length());
      entryMetadata.putAssociationUri(navigationPropertyName, uri);
    } else if (rel.equals(Edm.LINK_REL_EDIT_MEDIA)) {
      mediaMetadata.setEditLink(uri);
      String etag = attributes.get(M_ETAG);
      mediaMetadata.setEtag(etag);
    }
  }

  private void readContent(XMLStreamReader reader, EntityInfoAggregator eia) throws EntityProviderException, XMLStreamException, EdmException {
    validateStartPosition(reader, ATOM_CONTENT);
    Map<String, String> attributes = readAttributes(reader);
    int nextEventType = reader.nextTag();

    if (XMLStreamReader.END_ELEMENT == nextEventType) {
      validateEndPosition(reader, ATOM_CONTENT);
    } else if (XMLStreamReader.START_ELEMENT == nextEventType && reader.getLocalName().equals(M_PROPERTIES)) {
      readProperties(reader, eia);
    } else {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE
          .addContent("Expected closing 'content' or starting 'properties' but found '" + reader.getLocalName() + "'."));
    }
    //
    String contentType = attributes.get(M_TYPE);
    mediaMetadata.setContentType(contentType);
    String sourceLink = attributes.get(ATOM_SRC);
    mediaMetadata.setSourceLink(sourceLink);
  }

  private void readId(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, ATOM_ID);
    int eventType = reader.next();
    String value = null;
    if (eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, ATOM_ID);

    entryMetadata.setId(value);
  }

  /**
   * 
   * @param reader
   * @param entitySet
   * @throws XMLStreamException
   * @throws EdmException
   * @throws EntityProviderException
   */
  private void readProperties(XMLStreamReader reader, EntityInfoAggregator entitySet) throws XMLStreamException, EdmException, EntityProviderException {
    //
    int nextTagEventType = reader.next();

    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    boolean run = true;
    while (run) {
      if (nextTagEventType == XMLStreamReader.START_ELEMENT) {
        String name = reader.getLocalName();
        //        EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty(name);
        EntityPropertyInfo property = entitySet.getPropertyInfo(name);
        Object value = xpc.readStartedElement(reader, property);
        properties.put(name, value);
      } else if (nextTagEventType == XMLStreamReader.END_ELEMENT) {
        String name = reader.getLocalName();
        if (M_PROPERTIES.equals(name)) {
          run = false;
        }
      }
      nextTagEventType = reader.next();
    }
  }

  /**
   * Read all attributes for current element to map (key=AttributeName; value=AttributeValue).
   * 
   * @param reader
   * @return all the attributes for the current element
   */
  private Map<String, String> readAttributes(XMLStreamReader reader) {
    int attributesCount = reader.getAttributeCount();

    Map<String, String> attributes = new HashMap<String, String>();
    for (int i = 0; i < attributesCount; i++) {
      String name = reader.getAttributeName(i).getLocalPart();
      String value = reader.getAttributeValue(i);
      attributes.put(name, value);
    }
    return attributes;
  }

  /**
   * 
   * @param reader
   * @param tagName
   * @throws EntityProviderException
   */
  private void validateStartPosition(XMLStreamReader reader, String tagName) throws EntityProviderException {
    validatePosition(reader, tagName, XMLStreamReader.START_ELEMENT);
  }

  private void validateEndPosition(XMLStreamReader reader, String tagName) throws EntityProviderException {
    validatePosition(reader, tagName, XMLStreamReader.END_ELEMENT);
  }

  private void readAndValidateEndPosition(XMLStreamReader reader, String tagName) throws EntityProviderException, XMLStreamException {
    readAndValidatePosition(reader, tagName, XMLStreamReader.END_ELEMENT);
  }

  private void readAndValidatePosition(XMLStreamReader reader, String tagName, int eventType) throws EntityProviderException, XMLStreamException {
    if (eventType != reader.next() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
          "'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(msg));
    }
  }

  private void validatePosition(XMLStreamReader reader, String tagName, int eventType) throws EntityProviderException {
    if (eventType != reader.getEventType() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
          "'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(msg));
    }
  }
}
