package com.sap.core.odata.core.ep;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.enums.MediaType;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.NavigationPropertyInfo;

public class AtomEntryEntityProvider {

  private ODataContext context;
  private String etag;

  AtomEntryEntityProvider(ODataContext ctx) throws ODataEntityProviderException {
    this.context = ctx;
  }

  public void append(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data, boolean isRootElement, String mediaResourceMimeType) throws ODataEntityProviderException {
    try {

      writer.writeStartElement("entry");

      if (isRootElement) {
        writer.writeDefaultNamespace(Edm.NAMESPACE_ATOM_2005);
        writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
        writer.writeNamespace(Edm.PREFIX_D, Edm.NAMESPACE_D_2007_08);
        writer.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, "base", this.context.getUriInfo().getBaseUri().toASCIIString());
      }

      etag = createETag(eia, data);
      if (etag != null) {
        writer.writeAttribute(Edm.NAMESPACE_M_2007_08, FormatXml.M_ETAG, etag);
      }

      appendAtomMandatoryParts(writer, eia, data);
      appendAtomOptionalParts(writer, eia, data);
      appendAtomEditLink(writer, eia, data);
      appendAtomNavigationLinks(writer, eia, data);

      appendCustomProperties(writer, eia, data);
      
      if (eia.isEntityTypeHasStream()) {
        appendAtomContentLink(writer, eia, data, mediaResourceMimeType);
        appendAtomContentPart(writer, eia, data, mediaResourceMimeType);
        appendProperties(writer, eia, data);
      } else {
        writer.writeStartElement(FormatXml.ATOM_CONTENT);
        writer.writeAttribute(FormatXml.ATOM_TYPE, MediaType.APPLICATION_XML.toString());
        appendProperties(writer, eia, data);
        writer.writeEndElement();
      }

      writer.writeEndElement();

      writer.flush();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendCustomProperties(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
    try {
      List<String> noneSyndicationTargetPaths = eia.getNoneSyndicationTargetPathNames();
      for (String tpName : noneSyndicationTargetPaths) {
        EntityPropertyInfo info = eia.getPropertyInfo(tpName);
        EdmCustomizableFeedMappings customMapping = info.getCustomMapping();
        if(!customMapping.isFcKeepInContent()) {
          XmlPropertyEntityProvider aps = new XmlPropertyEntityProvider();
          Object value = data.get(info.getName());
          aps.append(writer, info, value, false);
        }
      }
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private String createETag(EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
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
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomNavigationLinks(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
    try {
      for (NavigationPropertyInfo info : eia.getNavigationPropertyInfos()) {
        boolean isFeed = (info.getMultiplicity() == EdmMultiplicity.MANY);
        String self = this.createSelfLink(eia, data, info.getName());
        appendAtomNavigationLink(writer, self, info.getName(), isFeed);
      }
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomNavigationLink(XMLStreamWriter writer, String self, String propertyName, boolean isFeed) throws ODataEntityProviderException {
    try {
      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, Edm.NAMESPACE_REL_2007_08 + propertyName);
      writer.writeAttribute(FormatXml.ATOM_TITLE, propertyName);

      if (isFeed) {
        writer.writeAttribute(FormatXml.ATOM_TYPE, MediaType.APPLICATION_ATOM_XML_FEED.toString());
      } else {
        writer.writeAttribute(FormatXml.ATOM_TYPE, MediaType.APPLICATION_ATOM_XML_ENTRY.toString());
      }

      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomEditLink(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
    try {
      String self = createSelfLink(eia, data, null);

      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, "edit");
      writer.writeAttribute(FormatXml.ATOM_TITLE, eia.getEntityTypeName());
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomContentLink(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data, String mediaResourceMimeType) throws ODataEntityProviderException {
    try {
      String self = createSelfLink(eia, data, "$value");

      if (mediaResourceMimeType == null) {
        mediaResourceMimeType = MediaType.APPLICATION_OCTET_STREAM.toString();
      }

      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, "edit-media");
      writer.writeAttribute(FormatXml.ATOM_TYPE, mediaResourceMimeType);
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomContentPart(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data, String mediaResourceMimeType) throws ODataEntityProviderException {
    try {
      String self = createSelfLink(eia, data, "$value");

      if (mediaResourceMimeType == null) {
        mediaResourceMimeType = MediaType.APPLICATION_OCTET_STREAM.toString();
      }

      writer.writeStartElement(FormatXml.ATOM_CONTENT);
      writer.writeAttribute(FormatXml.ATOM_TYPE, mediaResourceMimeType);
      writer.writeAttribute(FormatXml.ATOM_SRC, self);
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private String createSelfLink(EntityInfoAggregator eia, Map<String, Object> data, String extension) throws ODataEntityProviderException {
    try {
      StringBuilder sb = new StringBuilder();
      if (!eia.isDefaultEntityContainer()) {
        sb.append(eia.getEntityContainerName()).append(Edm.DELIMITER);
      }
      sb.append(eia.getEntitySetName()).append("(").append(this.createEntryKey(eia, data)).append(")").append((extension == null ? "" : "/" + extension));
      URI uri = new URI(null, null, null, -1, sb.toString(), null, null);
      return uri.toASCIIString();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomMandatoryParts(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
    try {
      writer.writeStartElement(FormatXml.ATOM_ID);
      String entryKey = this.createEntryKey(eia, data);
      writer.writeCharacters(createAtomId(eia, entryKey));
      writer.writeEndElement();

      writer.writeStartElement(FormatXml.ATOM_TITLE);
      writer.writeAttribute(FormatXml.M_TYPE, "text");
      EntityPropertyInfo titleInfo = eia.getTargetPathInfo(EdmTargetPath.SYNDICATION_TITLE);
      if (titleInfo != null) {
        EdmSimpleType st = (EdmSimpleType) titleInfo.getType();
        Object object = data.get(titleInfo.getName());
        String title = st.valueToString(object, EdmLiteralKind.DEFAULT, titleInfo.getFacets());
        writer.writeCharacters(title);
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
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private String getTargetPathValue(EntityInfoAggregator eia, String targetPath, Map<String, Object> data) throws ODataEntityProviderException {
    try {
      EntityPropertyInfo info = eia.getTargetPathInfo(targetPath);
      if (info != null) {
        EdmSimpleType type = (EdmSimpleType) info.getType();
        Object value = data.get(info.getName());
        return type.valueToString(value, EdmLiteralKind.DEFAULT, info.getFacets());
      }
      return null;
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomOptionalParts(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
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

      String term = eia.getEntityTypeNamespace() + Edm.DELIMITER + eia.getEntityTypeName();
      writer.writeStartElement(FormatXml.ATOM_CATEGORY);
      writer.writeAttribute(FormatXml.ATOM_CATEGORY_TERM, term);
      writer.writeAttribute(FormatXml.ATOM_CATEGORY_SCHEME, Edm.NAMESPACE_SCHEME_2007_08);
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendAtomOptionalPart(XMLStreamWriter writer, String name, String value, boolean writeType) throws ODataEntityProviderException {
    try {
      if (value != null) {
        writer.writeStartElement(name);
        if (writeType) {
          writer.writeAttribute(FormatXml.ATOM_TYPE, FormatXml.ATOM_TEXT);
        }
        writer.writeCharacters(value);
        writer.writeEndElement();
      }
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private String createAtomId(EntityInfoAggregator eia, String entryKey) throws ODataEntityProviderException {
    try {
      String id = "";

      if (!eia.isDefaultEntityContainer()) {
        id = id + eia.getEntityContainerName() + ".";
      }
      id = id + eia.getEntitySetName();
      id = id + "(" + entryKey + ")";

      URI baseUri = this.context.getUriInfo().getBaseUri();
      String scheme = baseUri.getScheme();
      String userInfo = baseUri.getUserInfo();
      String host = baseUri.getHost();
      int port = baseUri.getPort();
      String path = baseUri.getPath() + id;
      String query = baseUri.getQuery();
      String fragment = baseUri.getFragment();
      URI uri = new URI(scheme, userInfo, host, port, path, query, fragment);

      return uri.toASCIIString();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private String createEntryKey(EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
    try {
      List<EntityPropertyInfo> kp = eia.getKeyPropertyInfos();
      String keys = "";

      if (kp.size() == 1) {
        EdmSimpleType st = (EdmSimpleType) kp.get(0).getType();
        Object value = data.get(kp.get(0).getName());
        keys = st.valueToString(value, EdmLiteralKind.URI, kp.get(0).getFacets());
      } else {
        int size = kp.size();
        for (int i = 0; i < size; i++) {
          EntityPropertyInfo keyp = kp.get(i);
          Object value = data.get(keyp.getName());

          EdmSimpleType st = (EdmSimpleType) keyp.getType();
          keys = keys + keyp.getName() + "=";
          String strValue = st.valueToString(value, EdmLiteralKind.URI, kp.get(i).getFacets());
          keys = keys + strValue;
          if (i < size - 1) {
            keys = keys + ",";
          }
        }
      }
      return keys;
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private void appendProperties(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data) throws ODataEntityProviderException {
    try {
      writer.writeStartElement(Edm.NAMESPACE_M_2007_08, FormatXml.M_PROPERTIES);
      Set<Entry<String, Object>> entries = data.entrySet();

      for (Entry<String, Object> entry : entries) {
        String name = entry.getKey();
        EntityPropertyInfo propertyInfo = eia.getPropertyInfo(name);

        if(propertyInfo != null && isNotMappedViaCustomMapping(propertyInfo)) {
          Object value = entry.getValue();
          XmlPropertyEntityProvider aps = new XmlPropertyEntityProvider();
          aps.append(writer, propertyInfo, value, false);
        }
      }

      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }

  private boolean isNotMappedViaCustomMapping(EntityPropertyInfo propertyInfo) {
    EdmCustomizableFeedMappings customMapping = propertyInfo.getCustomMapping();
    if(customMapping != null && customMapping.isFcKeepInContent() != null) {
      return customMapping.isFcKeepInContent().booleanValue();
    }
    return true;
  }

  public String getETag() {
    return etag;
  }
}
