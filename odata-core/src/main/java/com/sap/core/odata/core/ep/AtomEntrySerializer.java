package com.sap.core.odata.core.ep;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringEscapeUtils;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.enums.MediaType;
import com.sap.core.odata.api.ep.ODataSerializationException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.ep.EntityInfoAggregator.EntityPropertyInfo;

public class AtomEntrySerializer {

  private ODataContext context;

  AtomEntrySerializer(ODataContext ctx) throws ODataSerializationException {
    this.context = ctx;
  }

  public void append(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data, boolean isRootElement, String mediaResourceMimeType) throws ODataSerializationException {
    try {
      writer.writeStartElement("entry");

      EntityInfoAggregator eia = EntityInfoAggregator.init(entitySet);
          
      if (isRootElement) {
        writer.writeDefaultNamespace(Edm.NAMESPACE_ATOM_2005);
        writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_EDMX_2007_06);
        writer.writeNamespace(Edm.PREFIX_D, Edm.NAMESPACE_EDM_2008_09);
        writer.writeAttribute(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998, "base", this.context.getUriInfo().getBaseUri().toASCIIString());
      }

      String etag = this.createETag(entitySet, data);
      if (etag != null) {
        writer.writeAttribute(Edm.NAMESPACE_EDMX_2007_06, FormatXml.M_ETAG, etag);
      }

      AtomInfoAggregator aia;
      if (entitySet.getEntityType().hasStream()) {
        aia = appendProperties(writer, entitySet, data);
        appendAtomContentPart(writer, entitySet, aia, data, mediaResourceMimeType);
        appendAtomContentLink(writer, entitySet, data, mediaResourceMimeType);
      } else {
        writer.writeStartElement(FormatXml.ATOM_CONTENT);
        writer.writeAttribute(FormatXml.ATOM_TYPE, MediaType.APPLICATION_XML.toString());
        aia = appendProperties(writer, entitySet, data);
        writer.writeEndElement();
      }
      

      appendAtomNavigationLinks(writer, entitySet, data);
      appendAtomEditLink(writer, entitySet, data);
      
      appendAtomMandatoryParts(writer, eia, data);
      
      appendAtomOptionalParts(writer, entitySet, aia);

      writer.writeEndElement();

      writer.flush();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private String createETag(EdmEntitySet entitySet, Map<String, Object> data) throws EdmException {
    String etag = null;
    
    for (String propertyName : entitySet.getEntityType().getPropertyNames()) {
      EdmTyped t = entitySet.getEntityType().getProperty(propertyName);
      if (t instanceof EdmProperty) {
        EdmProperty edmProperty = (EdmProperty) t;
        if (isConcurrencyModeFixed(edmProperty)) {
          if (etag == null) {
            EdmType edmType = edmProperty.getType();
            if (edmType instanceof EdmSimpleType) {
              EdmSimpleType edmSimpleType = (EdmSimpleType) edmType;
              etag = edmSimpleType.valueToString(data.get(propertyName), EdmLiteralKind.DEFAULT, edmProperty.getFacets());
            }
          } else {
            EdmType edmType = edmProperty.getType();
            if (edmType instanceof EdmSimpleType) {
              EdmSimpleType edmSimpleType = (EdmSimpleType) edmType;
              etag = etag + Edm.DELIMITER + edmSimpleType.valueToString(data.get(propertyName), EdmLiteralKind.DEFAULT, edmProperty.getFacets());
            }
          }
        }
      }
    }

    if (etag != null) {
      etag = StringEscapeUtils.escapeXml("W/\"" + etag + "\"");
    }

    return etag;
  }

  private boolean isConcurrencyModeFixed(EdmProperty edmProperty) throws EdmException {
    return edmProperty.getFacets() != null && edmProperty.getFacets().getConcurrencyMode() == EdmConcurrencyMode.Fixed;
  }

  private void appendAtomNavigationLinks(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data) throws EdmException, ODataSerializationException, URISyntaxException, XMLStreamException {
    for (String propertyName : entitySet.getEntityType().getNavigationPropertyNames()) {
      EdmTyped t = entitySet.getEntityType().getProperty(propertyName);
      if (t instanceof EdmNavigationProperty) {
        EdmNavigationProperty edmProperty = (EdmNavigationProperty) t;
        boolean isFeed = (edmProperty.getMultiplicity() == EdmMultiplicity.MANY);
        String self = this.createSelfLink(entitySet, data, propertyName);
        appendAtomNavigationLink(writer, self, propertyName, isFeed);
      }
    }
  }

  private void appendAtomNavigationLink(XMLStreamWriter writer, String self, String propertyName, boolean isFeed) throws XMLStreamException {
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
  }

  private void appendAtomEditLink(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      String self = createSelfLink(entitySet, data, null);

      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, "edit");
      writer.writeAttribute(FormatXml.ATOM_TITLE, entitySet.getEntityType().getName());
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private void appendAtomContentLink(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType) throws ODataSerializationException {
    try {
      String self = createSelfLink(entitySet, data, "$value");

      if (mediaResourceMimeType == null) {
        mediaResourceMimeType = MediaType.APPLICATION_OCTET_STREAM.toString();
      }

      writer.writeStartElement(FormatXml.ATOM_LINK);
      writer.writeAttribute(FormatXml.ATOM_HREF, self);
      writer.writeAttribute(FormatXml.ATOM_REL, "edit-media");
      writer.writeAttribute(FormatXml.ATOM_TYPE, mediaResourceMimeType);
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private void appendAtomContentPart(XMLStreamWriter writer, EdmEntitySet entitySet, AtomInfoAggregator aia, Map<String, Object> data, String mediaResourceMimeType) throws ODataSerializationException {
    try {
      String self = createSelfLink(entitySet, data, "$value");

      if (mediaResourceMimeType == null) {
        mediaResourceMimeType = MediaType.APPLICATION_OCTET_STREAM.toString();
      }

      writer.writeStartElement(FormatXml.ATOM_CONTENT);
      writer.writeAttribute(FormatXml.ATOM_TYPE, mediaResourceMimeType);
      writer.writeAttribute(FormatXml.ATOM_SRC, self);
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private String createSelfLink(EdmEntitySet entitySet, Map<String, Object> data, String extension) throws EdmException, ODataSerializationException, URISyntaxException {
    String path = entitySet.getName() + "(" + this.createEntryKey(entitySet, data) + ")" + (extension == null ? "" : "/" + extension);
    URI uri = new URI(null, null, null, -1, path, null, null);
    return uri.toASCIIString();
  }

  private void appendAtomMandatoryParts(XMLStreamWriter writer, EntityInfoAggregator eia, Map<String, Object> data) throws ODataSerializationException {
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
        if(updateDate != null) {
          updateFacets = updatedInfo.getFacets();
        }
      }
      if(updateDate == null) {
        updateDate = new Date();
      }
      writer.writeCharacters(EdmDateTimeOffset.getInstance().valueToString(updateDate, EdmLiteralKind.DEFAULT, updateFacets));
      
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private void appendAtomOptionalParts(XMLStreamWriter writer, EdmEntitySet entitySet, AtomInfoAggregator aia) throws ODataSerializationException {
    try { 
      if (aia.getAuthorEmail() != null || aia.getAuthorName() != null || aia.getAuthorUri() != null) {
        writer.writeStartElement(FormatXml.ATOM_AUTHOR);
        appendAtomOptionalPart(writer, FormatXml.ATOM_AUTHOR_NAME, aia.getAuthorName(), false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_AUTHOR_EMAIL, aia.getAuthorEmail(), false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_AUTHOR_URI, aia.getAuthorUri(), false);
        writer.writeEndElement();
      }
  
      appendAtomOptionalPart(writer, FormatXml.ATOM_SUMMARY, aia.getSummary(), true);
  
      if (aia.getContributorEmail() != null || aia.getContributorName() != null || aia.getContributorUri() != null) {
        writer.writeStartElement(FormatXml.ATOM_CONTRIBUTOR);
        appendAtomOptionalPart(writer, FormatXml.ATOM_CONTRIBUTOR_NAME, aia.getContributorName(), false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_CONTRIBUTOR_EMAIL, aia.getContributorEmail(), false);
        appendAtomOptionalPart(writer, FormatXml.ATOM_CONTRIBUTOR_URI, aia.getContributorUri(), false);
        writer.writeEndElement();
      }
  
      appendAtomOptionalPart(writer, FormatXml.ATOM_RIGHTS, aia.getRights(), true);
      appendAtomOptionalPart(writer, FormatXml.ATOM_PUBLISHED, aia.getPublished(), false);
  
      String term = entitySet.getEntityType().getNamespace() + Edm.DELIMITER + entitySet.getEntityType().getName();
      writer.writeStartElement(FormatXml.ATOM_CATEGORY);
      writer.writeAttribute(FormatXml.ATOM_CATEGORY_TERM, term);
      writer.writeAttribute(FormatXml.ATOM_CATEGORY_SCHEME, Edm.NAMESPACE_SCHEME_2007_08);
      writer.writeEndElement();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private void appendAtomOptionalPart(XMLStreamWriter writer, String name, String value, boolean writeType) throws XMLStreamException {
    if (value != null) {
      writer.writeStartElement(name);
      if (writeType) {
        writer.writeAttribute(FormatXml.ATOM_TYPE, FormatXml.ATOM_TEXT);
      }
      writer.writeCharacters(value);
      writer.writeEndElement();
    }
  }

  private String createAtomId(EntityInfoAggregator eia, String entryKey) throws ODataSerializationException {
    try {
//      EdmEntityContainer ec = eia.getEntityContainer();

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
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private String createEntryKey(EntityInfoAggregator eia, Map<String, Object> data) throws ODataSerializationException {
    try {
      List<EntityPropertyInfo> kp = eia.getKeyProperties();
      String keys = "";

      if (kp.size() == 1) {
        EdmSimpleType st = (EdmSimpleType) kp.get(0).getType();
        Object value = data.get(kp.get(0).getName());
        keys = st.valueToString(value, EdmLiteralKind.URI, kp.get(0).getFacets());
      }
      else {
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
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private String createEntryKey(EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      List<EdmProperty> kp = entitySet.getEntityType().getKeyProperties();
      String keys = "";

      if (kp.size() == 1) {
        EdmSimpleType st = (EdmSimpleType) kp.get(0).getType();
        Object value = data.get(kp.get(0).getName());
        keys = st.valueToString(value, EdmLiteralKind.URI, kp.get(0).getFacets());
      }
      else {
        int size = kp.size();
        for (int i = 0; i < size; i++) {
          EdmProperty keyp = kp.get(i);
          Object value = data.get(keyp.getName());

          EdmSimpleType st = (EdmSimpleType) kp.get(i).getType();
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
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private AtomInfoAggregator appendProperties(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data) throws EdmException, XMLStreamException, ODataSerializationException {
    writer.writeStartElement(Edm.NAMESPACE_EDMX_2007_06, FormatXml.M_PROPERTIES);
    Set<Entry<String, Object>> entries = data.entrySet();

    AtomInfoAggregator aia = new AtomInfoAggregator();

    for (Entry<String, Object> entry : entries) {
      String name = entry.getKey();
      EdmTyped property = entitySet.getEntityType().getProperty(name);

      if (property instanceof EdmProperty) {
        EdmProperty prop = (EdmProperty) property;
        Object value = entry.getValue();

        XmlPropertySerializer aps = new XmlPropertySerializer();
        aps.append(writer, prop, value, false, aia);
      }
    }

    writer.writeEndElement();

    return aia;
  }
}
