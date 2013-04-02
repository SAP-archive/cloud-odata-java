package com.sap.core.odata.core.ep.consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.callback.OnReadEntryContent;
import com.sap.core.odata.api.ep.callback.ReadCallbackResult;
import com.sap.core.odata.api.ep.callback.ReadEntryCallbackContext;
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
  private EntityProviderReadProperties consumerProperties;

  private String currentHandledStartTagName = null;

  public XmlEntryConsumer() {
    properties = new HashMap<String, Object>();
    mediaMetadata = new MediaMetadataImpl();
    entryMetadata = new EntryMetadataImpl();
    readEntryResult = new ODataEntryImpl(properties, mediaMetadata, entryMetadata);
    foundPrefix2NamespaceUri = new HashMap<String, String>();
  }

  public ODataEntry readEntry(final XMLStreamReader reader, final EntityInfoAggregator eia, final boolean merge) throws EntityProviderException {
    EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(merge).build();
    return readEntry(reader, eia, properties);
  }

  public ODataEntry readEntry(final XMLStreamReader reader, final EntityInfoAggregator eia, final EntityProviderReadProperties consumerProperties) throws EntityProviderException {
    try {
      this.consumerProperties = consumerProperties;
      typeMappings = EntityTypeMapping.create(consumerProperties.getTypeMappings());
      foundPrefix2NamespaceUri.putAll(consumerProperties.getValidatedPrefixNamespaceUris());
      boolean merge = consumerProperties.getMergeSemantic();

      while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_ATOM_2005.equals(reader.getNamespaceURI()) && FormatXml.ATOM_ENTRY.equals(reader.getLocalName()))) {
        reader.next();
        while (reader.hasNext() && !reader.hasName()) {
          reader.next();
        }

        if (reader.isStartElement()) {
          handleStartedTag(reader, eia, merge);
        }
      }

      if (!merge) {
        validate(eia, readEntryResult);
      }

      return readEntryResult;
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  public void validate(final EntityInfoAggregator eia, final ODataEntryImpl entry) throws EntityProviderException {
    Collection<EntityPropertyInfo> propertyInfos = new ArrayList<EntityPropertyInfo>(eia.getPropertyInfos());
    propertyInfos.removeAll(eia.getKeyPropertyInfos());
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

  private void handleStartedTag(final XMLStreamReader reader, final EntityInfoAggregator eia, final boolean merge)
      throws EntityProviderException, XMLStreamException, EdmException {

    currentHandledStartTagName = reader.getLocalName();

    if (FormatXml.ATOM_ID.equals(currentHandledStartTagName)) {
      readId(reader);
    } else if (FormatXml.ATOM_ENTRY.equals(currentHandledStartTagName)) {
      readEntry(reader);
    } else if (FormatXml.ATOM_LINK.equals(currentHandledStartTagName)) {
      readLink(reader);
    } else if (FormatXml.ATOM_CONTENT.equals(currentHandledStartTagName)) {
      readContent(reader, eia);
    } else if (FormatXml.M_PROPERTIES.equals(currentHandledStartTagName)) {
      readProperties(reader, eia);
    } else if (!merge) {
      readCustomElement(reader, currentHandledStartTagName, eia);
    }
  }

  private void readCustomElement(final XMLStreamReader reader, final String tagName, final EntityInfoAggregator eia) throws EdmException, EntityProviderException, XMLStreamException {
    EntityPropertyInfo targetPathInfo = eia.getTargetPathInfo(tagName);
    NamespaceContext nsctx = reader.getNamespaceContext();

    if (!Edm.NAMESPACE_ATOM_2005.equals(reader.getName().getNamespaceURI())) {

      if (targetPathInfo != null) {
        final String customPrefix = targetPathInfo.getCustomMapping().getFcNsPrefix();
        final String customNamespaceURI = targetPathInfo.getCustomMapping().getFcNsUri();

        if (customPrefix != null && customNamespaceURI != null) {
          String xmlPrefix = nsctx.getPrefix(customNamespaceURI);
          String xmlNamespaceUri = reader.getNamespaceURI(customPrefix);

          if (customNamespaceURI.equals(xmlNamespaceUri) && customPrefix.equals(xmlPrefix)) {
            reader.require(XMLStreamConstants.START_ELEMENT, customNamespaceURI, tagName);
            reader.next();
            reader.require(XMLStreamConstants.CHARACTERS, null, null);
            final String text = reader.getText();
            reader.nextTag();
            reader.require(XMLStreamConstants.END_ELEMENT, customNamespaceURI, tagName);

            final EntityPropertyInfo propertyInfo = getValidatedPropertyInfo(eia, tagName);
            final Class<?> typeMapping = typeMappings.getMappingClass(propertyInfo.getName());
            final EdmSimpleType type = (EdmSimpleType) propertyInfo.getType();
            final Object value = type.valueOfString(text, EdmLiteralKind.DEFAULT, propertyInfo.getFacets(),
                typeMapping == null ? type.getDefaultType() : typeMapping);
            properties.put(tagName, value);
          }
        }
      } else {
        throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(tagName));
      }

    }
  }

  private void readEntry(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_ENTRY);

    extractNamespacesFromTag(reader);
    final String etag = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, FormatXml.M_ETAG);
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

  private void readLink(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_LINK);

    final String rel = reader.getAttributeValue(null, FormatXml.ATOM_REL);
    final String uri = reader.getAttributeValue(null, FormatXml.ATOM_HREF);
    final String type = reader.getAttributeValue(null, FormatXml.ATOM_TYPE);
    final String title = reader.getAttributeValue(null, FormatXml.ATOM_TITLE);
    final String etag = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, FormatXml.M_ETAG);

    reader.next();
    if (reader.isEndElement()) {
      reader.require(XMLStreamConstants.END_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_LINK);

      if (rel == null || uri == null) {
        throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(
            "'" + FormatXml.ATOM_HREF + "' and/or '" + FormatXml.ATOM_REL + "' at tag '" + FormatXml.ATOM_LINK + "'"));
      } else if (rel.startsWith(Edm.NAMESPACE_REL_2007_08)) {
        final String navigationPropertyName = rel.substring(Edm.NAMESPACE_REL_2007_08.length());
        entryMetadata.putAssociationUri(navigationPropertyName, uri);
      } else if (rel.equals(Edm.LINK_REL_EDIT_MEDIA)) {
        mediaMetadata.setEditLink(uri);
        mediaMetadata.setEtag(etag);
      }
    } else {
      Map<String, String> attributes = new HashMap<String, String>();
      attributes.put(FormatXml.ATOM_REL, rel);
      attributes.put(FormatXml.ATOM_HREF, uri);
      attributes.put(FormatXml.ATOM_TYPE, type);
      attributes.put(FormatXml.ATOM_TITLE, title);
      attributes.put(FormatXml.M_ETAG, etag);
      readInlineContent(reader, attributes);
    }

  }

  private void readInlineContent(final XMLStreamReader reader, final Map<String, String> linkAttributes) throws XMLStreamException, EntityProviderException {
    reader.nextTag();
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_M_2007_08, FormatXml.M_INLINE);

    List<ODataEntry> inlineEntries = new ArrayList<ODataEntry>();
    
    while (!(reader.isEndElement() && Edm.NAMESPACE_M_2007_08.equals(reader.getNamespaceURI()) && FormatXml.M_INLINE.equals(reader.getLocalName()))) {
      reader.next();
 
      if (reader.isStartElement() && Edm.NAMESPACE_ATOM_2005.equals(reader.getNamespaceURI()) && FormatXml.ATOM_ENTRY.equals(reader.getLocalName())) {
        ReadCallbackResult callbackResult = doCallback(consumerProperties, linkAttributes);
        
        if (callbackResult != null) {
          XmlEntryConsumer xec = new XmlEntryConsumer();

          EntityInfoAggregator eia = EntityInfoAggregator.create(callbackResult.getEntitySet());
          EntityProviderReadProperties inlineConsumerProperties = callbackResult.getConsumerProperties();

          ODataEntry inlineEntry = xec.readEntry(reader, eia, inlineConsumerProperties);
          inlineEntries.add(inlineEntry);
        }
      }
    }

    properties.put(linkAttributes.get(FormatXml.ATOM_TITLE), inlineEntries);

    reader.require(XMLStreamConstants.END_ELEMENT, Edm.NAMESPACE_M_2007_08, FormatXml.M_INLINE);
  }

  private ReadCallbackResult doCallback(EntityProviderReadProperties properties, final Map<String, String> linkAttributes) {
    OnReadEntryContent callback = properties.getCallback();
    if(callback == null) {
      return null;
    } else {
      EntityProviderReadProperties callbackProperties = EntityProviderReadProperties.initFrom(properties).addValidatedPrefixes(foundPrefix2NamespaceUri).build();
      
      String title = linkAttributes.get(FormatXml.ATOM_TITLE);
      String type = linkAttributes.get(FormatXml.ATOM_TYPE);
      ReadEntryCallbackContext callbackContext = new ReadEntryCallbackContext(callbackProperties, type, title);
      callbackContext.addInfos(linkAttributes);

      return callback.retrieveReadResult(callbackContext);
    }
  }


  private void readContent(final XMLStreamReader reader, final EntityInfoAggregator eia) throws EntityProviderException, XMLStreamException, EdmException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_CONTENT);

    extractNamespacesFromTag(reader);

    checkAllMandatoryNamespacesAvailable();

    final String contentType = reader.getAttributeValue(null, FormatXml.ATOM_TYPE);
    final String sourceLink = reader.getAttributeValue(null, FormatXml.ATOM_SRC);

    reader.nextTag();

    if (reader.isStartElement() && reader.getLocalName().equals(FormatXml.M_PROPERTIES)) {
      readProperties(reader, eia);
    } else if (reader.isEndElement()) {
      reader.require(XMLStreamConstants.END_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_CONTENT);
    } else {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE
          .addContent("Expected closing 'content' or starting 'properties' but found '" + reader.getLocalName() + "'."));
    }

    mediaMetadata.setContentType(contentType);
    mediaMetadata.setSourceLink(sourceLink);
  }

  private void readId(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_ID);
    reader.next();
    if (reader.isCharacters()) {
      entryMetadata.setId(reader.getText());
    }
    reader.nextTag();
    reader.require(XMLStreamConstants.END_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_ID);
  }

  private void readProperties(final XMLStreamReader reader, final EntityInfoAggregator entitySet) throws XMLStreamException, EdmException, EntityProviderException {
    // validate namespace
    checkAllMandatoryNamespacesAvailable();
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_M_2007_08, FormatXml.M_PROPERTIES);
    if (entitySet.getEntityType().hasStream()) {
      // external properties
      checkCurrentHandledStartTag(FormatXml.M_PROPERTIES);
    } else {
      // inline properties
      checkCurrentHandledStartTag(FormatXml.ATOM_CONTENT);
    }

    EntityPropertyInfo property;
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();

    String closeTag = null;
    boolean run = true;
    reader.next();

    while (run) {
      if (reader.isStartElement() && closeTag == null) {
        closeTag = reader.getLocalName();
        if (isEdmNamespaceProperty(reader)) {
          if (properties.containsKey(closeTag)) {
            throw new EntityProviderException(EntityProviderException.DOUBLE_PROPERTY.addContent(closeTag));
          }
          property = getValidatedPropertyInfo(entitySet, closeTag);
          final Object value = xpc.readStartedElement(reader, property, typeMappings);
          properties.put(closeTag, value);
          closeTag = null;
        }
      } else if (reader.isEndElement()) {
        if (reader.getLocalName().equals(closeTag)) {
          closeTag = null;
        } else if (Edm.NAMESPACE_M_2007_08.equals(reader.getNamespaceURI()) && FormatXml.M_PROPERTIES.equals(reader.getLocalName())) {
          run = false;
        }
      }
      reader.next();
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
    final String nsUri = reader.getNamespaceURI();
    if (nsUri == null) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(reader.getLocalName()));
    } else {
      return Edm.NAMESPACE_D_2007_08.equals(nsUri);
    }
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
}
