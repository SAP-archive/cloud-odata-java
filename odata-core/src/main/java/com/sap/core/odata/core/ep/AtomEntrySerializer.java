package com.sap.core.odata.core.ep;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.ep.ODataSerializationException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataPathSegment;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;

public class AtomEntrySerializer {

  private static final String TAG_PROPERTIES = "properties";
  public static final String NS_DATASERVICES = "http://schemas.microsoft.com/ado/2007/08/dataservices";
  public static final String NS_DATASERVICES_METADATA = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  public static final String NS_ATOM = "http://www.w3.org/2005/Atom";
  public static final String NS_XML = "http://www.w3.org/XML/1998/namespace";
  private ODataContext context;

  AtomEntrySerializer(ODataContext ctx) throws ODataSerializationException {
    this.context = ctx;
  }

  public void append(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data, boolean isRootElement) throws ODataSerializationException {
    try {
      writer.writeStartElement("entry");

      if (isRootElement) {
        writer.writeDefaultNamespace(NS_ATOM);
        writer.writeNamespace("m", NS_DATASERVICES_METADATA);
        writer.writeNamespace("d", NS_DATASERVICES);
        writer.writeAttribute("xml", NS_XML, "base", this.context.getUriInfo().getBaseUri().toASCIIString());
      }

      AtomInfoAggregator aia = appendProperties(writer, entitySet, data);
      appendAtomParts(writer, entitySet, aia, data);

      writer.writeEndElement();

      writer.flush();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private void appendAtomParts(XMLStreamWriter writer, EdmEntitySet entitySet, AtomInfoAggregator aia, Map<String, Object> data) throws ODataSerializationException {
    try {
      writer.writeStartElement(FormatXml.ATOM_ID);
      String entryKey = this.createEntryKey(entitySet, data);
      writer.writeCharacters(createAtomId(entitySet, entryKey));
      writer.writeEndElement();

      writer.writeStartElement(FormatXml.ATOM_TITLE);
      writer.writeAttribute("type", "text");
      if (aia.getTitle() != null) {
        writer.writeCharacters(aia.getTitle());
      } else {
        writer.writeCharacters(entitySet.getName());
      }
      writer.writeEndElement();

      writer.writeStartElement(FormatXml.ATOM_UPDATED);
      if (aia.getUpdated() != null) {
        String propertyName = aia.getUpdatedPropertyName();
        Object obj = data.get(propertyName);
        EdmProperty p = (EdmProperty) entitySet.getEntityType().getProperty(propertyName);
        writer.writeCharacters(EdmDateTimeOffset.getInstance().valueToString(obj, EdmLiteralKind.DEFAULT, p.getFacets()));
      } else {
        writer.writeCharacters(EdmDateTimeOffset.getInstance().valueToString(new Date(), EdmLiteralKind.DEFAULT, null));
      }
      writer.writeEndElement();

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

  private String createAtomId(EdmEntitySet entitySet, String entryKey) throws ODataSerializationException {
    try {
      EdmEntityContainer ec = entitySet.getEntityContainer();

      String id = "";
      
      if (!ec.isDefaultEntityContainer()) {
        id = id + ec.getName() + ".";
      }
      id = id + entitySet.getName();
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
    writer.writeStartElement(NS_DATASERVICES_METADATA, TAG_PROPERTIES);
    Set<Entry<String, Object>> entries = data.entrySet();

    AtomInfoAggregator aia = new AtomInfoAggregator(entitySet.getEntityType().getKeyPropertyNames());

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

  private void handleAtomParts(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data) throws XMLStreamException, ODataSerializationException, EdmException {
    AtomHelper atomHelper = AtomHelper.init(entitySet);

    writer.writeStartElement("title");
    writer.writeAttribute("type", "text");
    writer.writeCharacters(createTitleText(atomHelper, data));
    writer.writeEndElement();

    writer.writeStartElement("updated");
    writer.writeCharacters(createUpdatedText(atomHelper, data));
    writer.writeEndElement();
  }

  private String createUpdatedText(AtomHelper atomHelper, Map<String, Object> data) throws EdmException {
    EdmProperty updatedProperty = atomHelper.getSyndicationProperty(EdmTargetPath.SYNDICATION_UPDATED);

    if (!isSyndicationUpdatedAvailable(updatedProperty, data)) {
      Object obj = data.get(updatedProperty.getName());
      return EdmDateTimeOffset.getInstance().valueToString(obj, EdmLiteralKind.DEFAULT, updatedProperty.getFacets());
    } else {
      // for the case that no 'syndication updated' is given it is specified to take current time
      return EdmDateTimeOffset.getInstance().valueToString(new Date(), EdmLiteralKind.DEFAULT, null);
    }
  }

  private boolean isSyndicationUpdatedAvailable(EdmProperty updatedProperty, Map<String, Object> data) throws EdmException {
    return updatedProperty == null || data.get(updatedProperty.getName()) == null;
  }

  private String createTitleText(AtomHelper atomHelper, Map<String, Object> data) throws EdmException {
    String result = "";
    EdmProperty titleProperty = atomHelper.getSyndicationProperty(EdmTargetPath.SYNDICATION_TITLE);

    if (titleProperty != null) {
      result = (String) data.get(titleProperty.getName());
    }

    return result;
  }

  private static class AtomHelper {
    Map<String, EdmProperty> target2Property = new HashMap<String, EdmProperty>();

    private AtomHelper(EdmEntitySet edmEntitySet) throws EdmException {
      EdmEntityType entityType = edmEntitySet.getEntityType();
      Collection<String> propertyNames = entityType.getPropertyNames();
      for (String propertyName : propertyNames) {
        // XXX: 121127_mibo: check this cast(s)
        EdmProperty property = (EdmProperty) entityType.getProperty(propertyName);
        EdmCustomizableFeedMappings customizableFeedMappings = property.getCustomizableFeedMappings();
        if (customizableFeedMappings != null) {
          String path = map2TargetPath(customizableFeedMappings.getFcTargetPath());

          target2Property.put(path, property);
        }
      }
    }

    public static AtomHelper init(EdmEntitySet edmEntitySet) throws EdmException {
      AtomHelper helper = new AtomHelper(edmEntitySet);

      return helper;
    }

    public EdmProperty getSyndicationProperty(String syndicationName) {
      return target2Property.get(syndicationName);
    }

    private static String map2TargetPath(String fcTargetPath) {
      return fcTargetPath;
    }
  }

}
