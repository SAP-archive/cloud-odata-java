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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityTypeMapping;
import com.sap.core.odata.core.ep.entry.EntryMetadataImpl;
import com.sap.core.odata.core.ep.entry.MediaMetadataImpl;
import com.sap.core.odata.core.ep.entry.ODataEntryImpl;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Atom/XML format reader/consumer for entries.
 */
public class XmlEntryConsumer {

  final private Map<String, String> foundPrefix2NamespaceUri;
  final private ODataEntryImpl readEntryResult;
  final private Map<String, Object> properties;
  final private MediaMetadataImpl mediaMetadata;
  final private EntryMetadataImpl entryMetadata;
  private EntityTypeMapping typeMappings;
  private ConsumerProperties consumerProperties;
  
  private String currentHandledStartTagName = null;

  public XmlEntryConsumer() {
    properties = new HashMap<String, Object>();
    mediaMetadata = new MediaMetadataImpl();
    entryMetadata = new EntryMetadataImpl();
    readEntryResult = new ODataEntryImpl(properties, mediaMetadata, entryMetadata);
    foundPrefix2NamespaceUri = new HashMap<String, String>();
  }

  public ODataEntry readEntry(final XMLStreamReader reader, final EntityInfoAggregator eia, final boolean merge) throws EntityProviderException {
    return readEntry(reader, eia, new ConsumerProperties(merge));
  }

  public ODataEntry readEntry(final XMLStreamReader reader, final EntityInfoAggregator eia, final ConsumerProperties consumerProperties) throws EntityProviderException {
    try {
      this.consumerProperties = consumerProperties;
      this.typeMappings = EntityTypeMapping.create(consumerProperties.getTypeMappings());
      this.foundPrefix2NamespaceUri.putAll(consumerProperties.getValidatedPrefixNamespaceUris());
      boolean merge = consumerProperties.getMergeSemantic();

      int eventType = -1;
      String tagName = null;
      boolean run = true;
      while (run) {
        eventType = readTillNextTagOrDocumentEnd(reader);
        tagName = getTagNameIfAvailable(reader, eventType);
        
        if (XMLStreamConstants.END_DOCUMENT == eventType || (XMLStreamConstants.END_ELEMENT == eventType && FormatXml.ATOM_ENTRY.equals(tagName))) {
          run = false;
        } else if(XMLStreamConstants.START_ELEMENT == eventType){
          handleStartedTag(reader, tagName, eia, merge);
        }
      }

      if (!merge) {
        validate(eia, readEntryResult);
      }

      return readEntryResult;
    } catch (EntityProviderException e) {
      throw e;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private String getTagNameIfAvailable(final XMLStreamReader reader, int eventType) {
    if (eventType == XMLStreamConstants.START_ELEMENT || eventType == XMLStreamConstants.END_ELEMENT || eventType == XMLStreamConstants.ENTITY_REFERENCE) {
      return reader.getLocalName();
    }
    return null;
  }

  private int readTillNextTagOrDocumentEnd(final XMLStreamReader reader) throws XMLStreamException {
    int eventType;
    eventType = reader.next();
    while (eventType != XMLStreamConstants.START_ELEMENT && eventType != XMLStreamConstants.END_ELEMENT && XMLStreamConstants.END_DOCUMENT != eventType) {
      eventType = reader.next();
    }
    return eventType;
  }

  public void validate(final EntityInfoAggregator eia, final ODataEntryImpl entry) throws EntityProviderException {
    Collection<EntityPropertyInfo> propertyInfos = eia.getPropertyInfos();
    Map<String, Object> data = entry.getProperties();

    for (EntityPropertyInfo entityPropertyInfo : propertyInfos) {
      boolean mandatory = entityPropertyInfo.isMandatory();
      if (mandatory) {
        if (!data.containsKey(entityPropertyInfo.getName())) {
          throw new EntityProviderException(EntityProviderException.MISSING_PROPERTY.addContent(entityPropertyInfo.getName()));
        }
      }
    }
  }

  /**
   * 
   * @param reader
   * @param tagName
   * @param eia 
   * @param merge 
   * @throws EntityProviderException
   * @throws XMLStreamException
   * @throws EdmException
   */
  private void handleStartedTag(final XMLStreamReader reader, final String tagName, final EntityInfoAggregator eia, final boolean merge)
      throws EntityProviderException, XMLStreamException, EdmException {

    currentHandledStartTagName = tagName;

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
    } else if (!merge) {
      readCustomElement(reader, tagName, eia);
    }
  }

  private void readCustomElement(final XMLStreamReader reader, final String tagName, final EntityInfoAggregator eia) throws EdmException, EntityProviderException, XMLStreamException {
    EntityPropertyInfo targetPathInfo = eia.getTargetPathInfo(tagName);
    NamespaceContext nsctx = reader.getNamespaceContext();

    if (!Edm.NAMESPACE_ATOM_2005.equals(reader.getName().getNamespaceURI())) {

      if (targetPathInfo != null) {
        String edmPrefix = targetPathInfo.getCustomMapping().getFcNsPrefix();
        String edmNamespaceURI = targetPathInfo.getCustomMapping().getFcNsUri();
        XmlPropertyConsumer xpc = new XmlPropertyConsumer();

        if (edmPrefix != null && edmNamespaceURI != null) {
          String xmlPrefix = nsctx.getPrefix(edmNamespaceURI);
          String xmlNamespaceUri = reader.getNamespaceURI(edmPrefix);

          if (edmNamespaceURI.equals(xmlNamespaceUri) && edmPrefix.equals(xmlPrefix)) {
            String name = reader.getLocalName();
            Object value = xpc.readStartedElement(reader, getValidatedPropertyInfo(eia, name), typeMappings);
            properties.put(name, value);
          }
        }
      } else {
        throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(tagName));
      }

    }
  }

  private void readEntry(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, ATOM_ENTRY);

    extractNamespacesFromTag(reader);

    Map<String, String> attributes = readAttributes(reader);

    String etag = attributes.get(M_ETAG);
    entryMetadata.setEtag(etag);
  }

  private void extractNamespacesFromTag(final XMLStreamReader reader) throws EntityProviderException {
    // collect namespaces
    int namespaceCount = reader.getNamespaceCount();
    for (int i = 0; i < namespaceCount; i++) {
      String namespacePrefix = reader.getNamespacePrefix(i);
      String namespaceUri = reader.getNamespaceURI(i);

      foundPrefix2NamespaceUri.put(namespacePrefix, namespaceUri);
    }
  }

  private void checkAllMandatoryNamespacesAvailable() throws EntityProviderException {
    if (!foundPrefix2NamespaceUri.containsValue(Edm.NAMESPACE_D_2007_08)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_D_2007_08));
    } else if (!foundPrefix2NamespaceUri.containsValue(Edm.NAMESPACE_M_2007_08)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_M_2007_08));
    } else if (!foundPrefix2NamespaceUri.containsValue(Edm.NAMESPACE_ATOM_2005)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_ATOM_2005));
    }
  }

  private void validateNamespace(final QName name, final String expectedNsUriForPrefix) throws EntityProviderException {
    String nsPrefix = name.getPrefix();
    //    String nsUri = name.getNamespaceURI();

    String atomNamespaceUri = foundPrefix2NamespaceUri.get(nsPrefix);
    if (atomNamespaceUri == null) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(name.getLocalPart()));
    }
    if (!atomNamespaceUri.equals(expectedNsUriForPrefix)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(name.getLocalPart()));
    }
  }

  /**
   * 
   * @param reader
   * @throws EntityProviderException
   * @throws XMLStreamException
   */
  private void readLink(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, ATOM_LINK);
    Map<String, String> attributes = readAttributes(reader);

    int nextTagEvent = reader.next();
    if (nextTagEvent == XMLStreamConstants.END_ELEMENT && ATOM_LINK.equals(reader.getLocalName())) {
      validateEndPosition(reader, ATOM_LINK);

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
    } else {
      readInlineContent(reader, attributes);
    }

  }

  private void readInlineContent(final XMLStreamReader reader, final Map<String, String> linkAttributes) throws XMLStreamException, EntityProviderException {
    int nextEventType = reader.nextTag();
    validatePosition(reader, FormatXml.M_INLINE, XMLStreamConstants.START_ELEMENT);
    //
    ConsumerCallback callback = this.consumerProperties.getCallback();
    //
    consumerProperties.addValidatedPrefixNamespaceUris(foundPrefix2NamespaceUri);
    CallbackResult cResult = (callback == null? null: callback.callback(consumerProperties, createCallbackInfo(linkAttributes)));
    //
    nextEventType = reader.next();
    List<ODataEntry> inlineEntries = new ArrayList<ODataEntry>();
    boolean run = true;
    while (run) {
      if (XMLStreamConstants.END_ELEMENT == nextEventType && FormatXml.M_INLINE.equals(reader.getLocalName())) {
        run = false;
      } else if(XMLStreamConstants.START_ELEMENT == nextEventType && FormatXml.ATOM_ENTRY.equals(reader.getLocalName())) {
        if(cResult != null) {
          XmlEntryConsumer xec = new XmlEntryConsumer();
          //
          EntityInfoAggregator eia = EntityInfoAggregator.create(cResult.getEntitySet(), consumerProperties);
          ConsumerProperties inlineConsumerProperties = cResult.getConsumerProperties();
          
          ODataEntry inlineEntry = xec.readEntry(reader, eia, inlineConsumerProperties);
          inlineEntries.add(inlineEntry);
        }
        nextEventType = reader.next();
      } else {
        nextEventType = reader.next();
      }
    }
    
    this.properties.put(linkAttributes.get(FormatXml.ATOM_TITLE), inlineEntries);
    
    validateEndPosition(reader, FormatXml.M_INLINE);
  }

  private CallbackInfo createCallbackInfo(Map<String, String> linkAttributes) {
    CallbackInfo info = new CallbackInfo();
    info.addInfos(linkAttributes);
    return info;
  }

  private void readContent(final XMLStreamReader reader, final EntityInfoAggregator eia) throws EntityProviderException, XMLStreamException, EdmException {
    validateStartPosition(reader, ATOM_CONTENT);
    //
    extractNamespacesFromTag(reader);
    //
    checkAllMandatoryNamespacesAvailable();

    Map<String, String> attributes = readAttributes(reader);
    int nextEventType = reader.nextTag();

    if (XMLStreamConstants.END_ELEMENT == nextEventType) {
      validateEndPosition(reader, ATOM_CONTENT);
    } else if (XMLStreamConstants.START_ELEMENT == nextEventType && reader.getLocalName().equals(M_PROPERTIES)) {
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

  private void readId(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, ATOM_ID);
    int eventType = reader.next();
    String value = null;
    if (eventType == XMLStreamConstants.CHARACTERS) {
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
  private void readProperties(final XMLStreamReader reader, final EntityInfoAggregator entitySet) throws XMLStreamException, EdmException, EntityProviderException {
    // validate namespace
    checkAllMandatoryNamespacesAvailable();
    validateNamespace(reader.getName(), Edm.NAMESPACE_M_2007_08);
    if (entitySet.getEntityType().hasStream()) {
      // external properties
      checkCurrentHandledStartTag(FormatXml.M_PROPERTIES);
    } else {
      // inline properties
      checkCurrentHandledStartTag(ATOM_CONTENT);
    }

    //
    EntityPropertyInfo property;
    String closeTag = null;
    
    int nextTagEventType = reader.next();
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    boolean run = true;
    //
    while (run) {
      if (nextTagEventType == XMLStreamConstants.START_ELEMENT && closeTag == null) {
        closeTag = reader.getLocalName();
        if(isEdmNamespaceProperty(reader)) {
          String name = getValidPropertyName(reader);
          property = getValidatedPropertyInfo(entitySet, name);
          Object value = xpc.readStartedElement(reader, property, typeMappings);
          properties.put(name, value);
          closeTag = null;
        }
      } else if (nextTagEventType == XMLStreamConstants.END_ELEMENT) {
        String name = reader.getLocalName();
        if (M_PROPERTIES.equals(name)) {
          run = false;
        } else if(name.equals(closeTag)) {
          closeTag = null;
        }
      }
      nextTagEventType = reader.next();
    }
  }

  /**
   * Check if the {@link #currentHandledStartTagName} is the same as the <code>expectedTagName</code>.
   * If tag name is not as expected or if {@link #currentHandledStartTagName} is not set an {@link EntityProviderException} is thrown.
   * 
   * @param expectedTagName expected name for {@link #currentHandledStartTagName}
   * @throws EntityProviderException if tag name is not as expected or if {@link #currentHandledStartTagName} is <code>NULL</code>.
   */
  private void checkCurrentHandledStartTag(final String expectedTagName) throws EntityProviderException {
    if (currentHandledStartTagName == null) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent("No current handled start tag name set."));
    } else if (!currentHandledStartTagName.equals(expectedTagName)) {
      throw new EntityProviderException(EntityProviderException.INVALID_PARENT_TAG.addContent(expectedTagName).addContent(currentHandledStartTagName));
    }
  }

  /**
   * Get validated name for property of currently read tag in {@link XMLStreamReader}.
   * If validation fails an {@link EntityProviderException} is thrown.
   * 
   * Currently this is the case if tag has none or a wrong namespace set. 
   * Expected and valid namespace uri for edm properties is {@value Edm#NAMESPACE_D_2007_08}.
   * 
   * @param reader {@link XMLStreamReader} with position at to checked tag
   * @return valid tag name (which is never <code>NULL</code>).
   * @throws EntityProviderException
   */
  private String getValidPropertyName(final XMLStreamReader reader) throws EntityProviderException {
    QName name = reader.getName();
    validateNamespace(name, Edm.NAMESPACE_D_2007_08);

    if(properties.containsKey(name.getLocalPart())) {
      throw new EntityProviderException(EntityProviderException.DOUBLE_PROPERTY.addContent(name.getLocalPart()));
    }

    return name.getLocalPart();
  }
  
  /**
   * Checks if property of currently read tag in {@link XMLStreamReader} is defined in 
   * <code>edm properties namespace</code> {@value Edm#NAMESPACE_D_2007_08}.
   * 
   * If no namespace uri definition is found for namespace prefix of property (<code>tag</code>) an exception is thrown.
   * 
   * @param reader {@link XMLStreamReader} with position at to checked tag
   * @return <code>true</code> if property is in <code>edm properties namespace</code>, otherwise <code>false</code>.
   * @throws EntityProviderException If no namespace uri definition is found for namespace prefix of property (<code>tag</code>).
   */
  private boolean isEdmNamespaceProperty(final XMLStreamReader reader) throws EntityProviderException {
    QName name = reader.getName();
    String nsUri = name.getNamespaceURI();
    if(nsUri == null) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(name.getLocalPart()));
    } else if (Edm.NAMESPACE_D_2007_08.equals(nsUri)) {
      // found correct edm namespace
      return true;
    }
    return false;
  }


  /**
   * Get validated {@link EntityPropertyInfo} for property with given <code>name</code>.
   * If validation fails an {@link EntityProviderException} is thrown.
   * 
   * Currently this is the case if no {@link EntityPropertyInfo} if found for given <code>name</code>.
   * 
   * @param entitySet
   * @param name
   * @return valid {@link EntityPropertyInfo} (which is never <code>NULL</code>).
   * @throws EntityProviderException
   */
  private EntityPropertyInfo getValidatedPropertyInfo(final EntityInfoAggregator entitySet, final String name) throws EntityProviderException {
    EntityPropertyInfo info = entitySet.getPropertyInfo(name);
    if (info == null) {
      throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(name));
    }
    return info;
  }

  /**
   * Read all attributes for current element to map (key=AttributeName; value=AttributeValue).
   * 
   * @param reader
   * @return all the attributes for the current element
   */
  private Map<String, String> readAttributes(final XMLStreamReader reader) {
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
  private void validateStartPosition(final XMLStreamReader reader, final String tagName) throws EntityProviderException {
    validatePosition(reader, tagName, XMLStreamConstants.START_ELEMENT);
  }

  private void validateEndPosition(final XMLStreamReader reader, final String tagName) throws EntityProviderException {
    validatePosition(reader, tagName, XMLStreamConstants.END_ELEMENT);
  }

  private void readAndValidateEndPosition(final XMLStreamReader reader, final String tagName) throws EntityProviderException, XMLStreamException {
    readAndValidatePosition(reader, tagName, XMLStreamConstants.END_ELEMENT);
  }

  private void readAndValidatePosition(final XMLStreamReader reader, final String tagName, final int eventType) throws EntityProviderException, XMLStreamException {
    if (eventType != reader.next() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
          "'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(msg));
    }
  }

  private void validatePosition(final XMLStreamReader reader, final String tagName, final int eventType) throws EntityProviderException {
    if (eventType != reader.getEventType() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
          "'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(msg));
    }
  }
}
