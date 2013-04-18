package com.sap.core.odata.core.ep.producer;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.OnWriteEntryContent;
import com.sap.core.odata.api.ep.callback.OnWriteFeedContent;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.Encoder;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.NavigationPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Serializes an ATOM entry.
 * @author SAP AG
 */
public class AtomEntryEntityProducer {

  private String etag;
  private String location;
  private final EntityProviderWriteProperties properties;

  public AtomEntryEntityProducer(final EntityProviderWriteProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data, final boolean isRootElement, final boolean isFeedPart) throws EntityProviderException {
    try {
      writer.writeStartElement(FormatXml.ATOM_ENTRY);

      if (isRootElement) {
        writer.writeDefaultNamespace(Edm.NAMESPACE_ATOM_2005);
        writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
        writer.writeNamespace(Edm.PREFIX_D, Edm.NAMESPACE_D_2007_08);
      }
      if (!isFeedPart) {
        writer.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, "base", properties.getServiceRoot().toASCIIString());
      }

      etag = createETag(eia, data);
      if (etag != null) {
        writer.writeAttribute(Edm.NAMESPACE_M_2007_08, FormatXml.M_ETAG, etag);
      }

      // write all atom infos (mandatory and optional)
      appendAtomMandatoryParts(writer, eia, data);
      appendAtomOptionalParts(writer, eia, data);

      if (eia.getEntityType().hasStream()) {
        // write all links
        appendAtomEditLink(writer, eia, data);
        appendAtomContentLink(writer, eia, data, properties.getMediaResourceMimeType());
        appendAtomNavigationLinks(writer, eia, data);
        // write properties/content
        appendCustomProperties(writer, eia, data);
        appendAtomContentPart(writer, eia, data, properties.getMediaResourceMimeType());
        appendProperties(writer, eia, data);
      } else {
        // write all links
        appendAtomEditLink(writer, eia, data);
        appendAtomNavigationLinks(writer, eia, data);
        // write properties/content
        appendCustomProperties(writer, eia, data);
        writer.writeStartElement(FormatXml.ATOM_CONTENT);
        writer.writeAttribute(FormatXml.ATOM_TYPE, ContentType.APPLICATION_XML.toString());
        appendProperties(writer, eia, data);
        writer.writeEndElement();
      }

      writer.writeEndElement();

      writer.flush();
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendCustomProperties(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    List<String> noneSyndicationTargetPaths = eia.getNoneSyndicationTargetPathNames();
    for (String tpName : noneSyndicationTargetPaths) {
      EntityPropertyInfo info = eia.getTargetPathInfo(tpName);
      if (!isKeepInContent(info)) {
        XmlPropertyEntityProducer aps = new XmlPropertyEntityProducer();
        final String name = info.getName();
        aps.append(writer, name, info, data.get(name));
      }
    }
  }

  private boolean isKeepInContent(final EntityPropertyInfo info) {
    EdmCustomizableFeedMappings customMapping = info.getCustomMapping();
    return Boolean.TRUE.equals(customMapping.isFcKeepInContent());
  }

  protected static String createETag(final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    try {
      String etag = null;

      Collection<EntityPropertyInfo> propertyInfos = eia.getETagPropertyInfos();
      for (EntityPropertyInfo propertyInfo : propertyInfos) {
        EdmType edmType = propertyInfo.getType();
        if (edmType instanceof EdmSimpleType) {
          EdmSimpleType edmSimpleType = (EdmSimpleType) edmType;
          if (etag == null) {
            etag = edmSimpleType.valueToString(data.get(propertyInfo.getName()), EdmLiteralKind.DEFAULT, propertyInfo.getFacets());
          } else {
            etag = etag + Edm.DELIMITER + edmSimpleType.valueToString(data.get(propertyInfo.getName()), EdmLiteralKind.DEFAULT, propertyInfo.getFacets());
          }
        }
      }

      if (etag != null) {
        etag = "W/\"" + etag + "\"";
      }

      return etag;
    } catch (EdmSimpleTypeException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendAtomNavigationLinks(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    for (String name : eia.getSelectedNavigationPropertyNames()) {
      NavigationPropertyInfo info = eia.getNavigationPropertyInfo(name);
      boolean isFeed = (info.getMultiplicity() == EdmMultiplicity.MANY);
      String self = createSelfLink(eia, data, info.getName());
      appendAtomNavigationLink(writer, self, info.getName(), isFeed, eia, data);
    }
  }

  private void appendAtomNavigationLink(final XMLStreamWriter writer, final String self, final String navigationPropertyName, final boolean isFeed, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    try {
      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, Edm.NAMESPACE_REL_2007_08 + navigationPropertyName);
      writer.writeAttribute(FormatXml.ATOM_TITLE, navigationPropertyName);
      if (isFeed) {
        writer.writeAttribute(FormatXml.ATOM_TYPE, ContentType.APPLICATION_ATOM_XML_FEED.toString());
        appendInlineFeed(writer, navigationPropertyName, eia, data, self);
      } else {
        writer.writeAttribute(FormatXml.ATOM_TYPE, ContentType.APPLICATION_ATOM_XML_ENTRY.toString());
        appendInlineEntry(writer, navigationPropertyName, eia, data);
      }

      writer.writeEndElement();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendInlineFeed(final XMLStreamWriter writer, final String navigationPropertyName, final EntityInfoAggregator eia, final Map<String, Object> data, String self) throws EntityProviderException {
    try {
      if (eia.getExpandedNavigationPropertyNames().contains(navigationPropertyName)) {
        if (properties.getCallbacks() != null && properties.getCallbacks().containsKey(navigationPropertyName)) {
          writer.writeStartElement(Edm.NAMESPACE_M_2007_08, FormatXml.M_INLINE);

          EdmNavigationProperty navProp = (EdmNavigationProperty) eia.getEntityType().getProperty(navigationPropertyName);
          WriteFeedCallbackContext context = new WriteFeedCallbackContext();
          context.setSourceEntitySet(eia.getEntitySet());
          context.setNavigationProperty(navProp);
          context.setEntryData(data);
          ExpandSelectTreeNode subNode = properties.getExpandSelectTree().getLinks().get(navigationPropertyName);
          context.setCurrentExpandSelectTreeNode(subNode);
          context.setSelfLink(new URI(self));
          
          ODataCallback callback = properties.getCallbacks().get(navigationPropertyName);
          WriteFeedCallbackResult result = ((OnWriteFeedContent) callback).retrieveFeedResult(context);
          List<Map<String, Object>> inlineData = result.getFeedData();
          if (inlineData != null) {
            EntityProviderWriteProperties inlineProperties = result.getInlineProperties();
            EdmEntitySet inlineEntitySet = eia.getEntitySet().getRelatedEntitySet(navProp);
            AtomFeedProducer inlineFeedProducer = new AtomFeedProducer(inlineProperties);
            EntityInfoAggregator inlineEia = EntityInfoAggregator.create(inlineEntitySet, inlineProperties.getExpandSelectTree());
            inlineFeedProducer.append(writer, inlineEia, inlineData);
          }

          writer.writeEndElement();
        }
      }
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendInlineEntry(final XMLStreamWriter writer, final String navigationPropertyName, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    try {
      if (eia.getExpandedNavigationPropertyNames().contains(navigationPropertyName)) {
        if (properties.getCallbacks() != null && properties.getCallbacks().containsKey(navigationPropertyName)) {
          writer.writeStartElement(Edm.NAMESPACE_M_2007_08, FormatXml.M_INLINE);

          EdmNavigationProperty navProp = (EdmNavigationProperty) eia.getEntityType().getProperty(navigationPropertyName);
          WriteEntryCallbackContext context = new WriteEntryCallbackContext();
          context.setSourceEntitySet(eia.getEntitySet());
          context.setNavigationProperty(navProp);
          context.setEntryData(data);
          ExpandSelectTreeNode subNode = properties.getExpandSelectTree().getLinks().get(navigationPropertyName);
          context.setCurrentExpandSelectTreeNode(subNode);

          ODataCallback callback = properties.getCallbacks().get(navigationPropertyName);
          WriteEntryCallbackResult result = ((OnWriteEntryContent) callback).retrieveEntryResult(context);
          Map<String, Object> inlineData = result.getEntryData();
          if (inlineData != null) {
            EntityProviderWriteProperties inlineProperties = result.getInlineProperties();
            EdmEntitySet inlineEntitySet = eia.getEntitySet().getRelatedEntitySet(navProp);
            AtomEntryEntityProducer inlineProducer = new AtomEntryEntityProducer(inlineProperties);
            EntityInfoAggregator inlineEia = EntityInfoAggregator.create(inlineEntitySet, inlineProperties.getExpandSelectTree());
            inlineProducer.append(writer, inlineEia, inlineData, false, false);
          }

          writer.writeEndElement();
        }
      }
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendAtomEditLink(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    try {
      String self = createSelfLink(eia, data, null);

      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, "edit");
      writer.writeAttribute(FormatXml.ATOM_TITLE, eia.getEntityType().getName());
      writer.writeEndElement();
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendAtomContentLink(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data, String mediaResourceMimeType) throws EntityProviderException {
    try {
      String self = createSelfLink(eia, data, "$value");

      if (mediaResourceMimeType == null) {
        mediaResourceMimeType = ContentType.APPLICATION_OCTET_STREAM.toString();
      }

      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, "edit-media");
      writer.writeAttribute(FormatXml.ATOM_TYPE, mediaResourceMimeType);
      writer.writeEndElement();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendAtomContentPart(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data, String mediaResourceMimeType) throws EntityProviderException {
    try {
      String self = createSelfLink(eia, data, "$value");

      if (mediaResourceMimeType == null) {
        mediaResourceMimeType = ContentType.APPLICATION_OCTET_STREAM.toString();
      }

      writer.writeStartElement(FormatXml.ATOM_CONTENT);
      writer.writeAttribute(FormatXml.ATOM_TYPE, mediaResourceMimeType);
      writer.writeAttribute(FormatXml.ATOM_SRC, self);
      writer.writeEndElement();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendAtomMandatoryParts(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    try {
      writer.writeStartElement(FormatXml.ATOM_ID);
      location = properties.getServiceRoot().toASCIIString() + createSelfLink(eia, data, null);
      writer.writeCharacters(location);
      writer.writeEndElement();

      writer.writeStartElement(FormatXml.ATOM_TITLE);
      writer.writeAttribute(FormatXml.M_TYPE, "text");
      EntityPropertyInfo titleInfo = eia.getTargetPathInfo(EdmTargetPath.SYNDICATION_TITLE);
      if (titleInfo != null) {
        EdmSimpleType st = (EdmSimpleType) titleInfo.getType();
        Object object = data.get(titleInfo.getName());
        String title = st.valueToString(object, EdmLiteralKind.DEFAULT, titleInfo.getFacets());
        if (title != null) {
          writer.writeCharacters(title);
        }
      } else {
        writer.writeCharacters(eia.getEntitySetName());
      }
      writer.writeEndElement();

      writer.writeStartElement(FormatXml.ATOM_UPDATED);

      Object updateDate = null;
      EdmFacets updateFacets = null;
      EntityPropertyInfo updatedInfo = eia.getTargetPathInfo(EdmTargetPath.SYNDICATION_UPDATED);
      if (updatedInfo != null) {
        updateDate = data.get(updatedInfo.getName());
        if (updateDate != null) {
          updateFacets = updatedInfo.getFacets();
        }
      }
      if (updateDate == null) {
        updateDate = new Date();
      }
      writer.writeCharacters(EdmDateTimeOffset.getInstance().valueToString(updateDate, EdmLiteralKind.DEFAULT, updateFacets));

      writer.writeEndElement();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmSimpleTypeException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private String getTargetPathValue(final EntityInfoAggregator eia, final String targetPath, final Map<String, Object> data) throws EntityProviderException {
    try {
      EntityPropertyInfo info = eia.getTargetPathInfo(targetPath);
      if (info != null) {
        EdmSimpleType type = (EdmSimpleType) info.getType();
        Object value = data.get(info.getName());
        return type.valueToString(value, EdmLiteralKind.DEFAULT, info.getFacets());
      }
      return null;
    } catch (EdmSimpleTypeException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendAtomOptionalParts(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    try {
      String authorEmail = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_AUTHOREMAIL, data);
      String authorName = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_AUTHORNAME, data);
      String authorUri = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_AUTHORURI, data);
      if (authorEmail != null || authorName != null || authorUri != null) {
        writer.writeStartElement(FormatXml.ATOM_AUTHOR);
        appendAtomOptionalPart(writer, FormatXml.ATOM_AUTHOR_NAME, authorName, false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_AUTHOR_EMAIL, authorEmail, false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_AUTHOR_URI, authorUri, false);
        writer.writeEndElement();
      }

      String summary = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_SUMMARY, data);
      appendAtomOptionalPart(writer, FormatXml.ATOM_SUMMARY, summary, true);

      String contributorName = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_CONTRIBUTORNAME, data);
      String contributorEmail = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_CONTRIBUTOREMAIL, data);
      String contributorUri = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_CONTRIBUTORURI, data);
      if (contributorEmail != null || contributorName != null || contributorUri != null) {
        writer.writeStartElement(FormatXml.ATOM_CONTRIBUTOR);
        appendAtomOptionalPart(writer, FormatXml.ATOM_CONTRIBUTOR_NAME, contributorName, false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_CONTRIBUTOR_EMAIL, contributorEmail, false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_CONTRIBUTOR_URI, contributorUri, false);
        writer.writeEndElement();
      }

      String rights = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_RIGHTS, data);
      appendAtomOptionalPart(writer, FormatXml.ATOM_RIGHTS, rights, true);
      String published = getTargetPathValue(eia, EdmTargetPath.SYNDICATION_PUBLISHED, data);
      appendAtomOptionalPart(writer, FormatXml.ATOM_PUBLISHED, published, false);

      String term = eia.getEntityType().getNamespace() + Edm.DELIMITER + eia.getEntityType().getName();
      writer.writeStartElement(FormatXml.ATOM_CATEGORY);
      writer.writeAttribute(FormatXml.ATOM_CATEGORY_TERM, term);
      writer.writeAttribute(FormatXml.ATOM_CATEGORY_SCHEME, Edm.NAMESPACE_SCHEME_2007_08);
      writer.writeEndElement();
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void appendAtomOptionalPart(final XMLStreamWriter writer, final String name, final String value, final boolean writeType) throws EntityProviderException {
    try {
      if (value != null) {
        writer.writeStartElement(name);
        if (writeType) {
          writer.writeAttribute(FormatXml.ATOM_TYPE, FormatXml.ATOM_TEXT);
        }
        writer.writeCharacters(value);
        writer.writeEndElement();
      }
    } catch (XMLStreamException e) {
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

  private void appendProperties(final XMLStreamWriter writer, final EntityInfoAggregator eia, final Map<String, Object> data) throws EntityProviderException {
    try {
      List<String> propertyNames = eia.getSelectedPropertyNames();
      if (!propertyNames.isEmpty()) {
        writer.writeStartElement(Edm.NAMESPACE_M_2007_08, FormatXml.M_PROPERTIES);

        for (String propertyName : propertyNames) {
          EntityPropertyInfo propertyInfo = eia.getPropertyInfo(propertyName);

          if (isNotMappedViaCustomMapping(propertyInfo)) {
            Object value = data.get(propertyName);
            XmlPropertyEntityProducer aps = new XmlPropertyEntityProducer();
            aps.append(writer, propertyInfo.getName(), propertyInfo, value);
          }
        }

        writer.writeEndElement();
      }
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private boolean isNotMappedViaCustomMapping(final EntityPropertyInfo propertyInfo) {
    EdmCustomizableFeedMappings customMapping = propertyInfo.getCustomMapping();
    if (customMapping != null && customMapping.isFcKeepInContent() != null) {
      return customMapping.isFcKeepInContent().booleanValue();
    }
    return true;
  }

  public String getETag() {
    return etag;
  }

  public String getLocation() {
    return location;
  }
}
