package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.EdmContentKind;
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
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;

public class AtomEntrySerializer extends ODataSerializer {

  private static final String TAG_PROPERTIES = "properties";
  public static final String NS_DATASERVICES = "http://schemas.microsoft.com/ado/2007/08/dataservices";
  public static final String NS_DATASERVICES_METADATA = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  public static final String NS_ATOM = "http://www.w3.org/2005/Atom";
  public static final String NS_XML = "http://www.w3.org/XML/1998/namespace";

  AtomEntrySerializer(ODataContext ctx) throws ODataSerializationException {
    super(ctx);
  }

  public void serializeInto(OutputStream stream, EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stream, "utf-8");
      appendTo(writer, entitySet, data);
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  @Override
  public InputStream serializeEntry(EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      this.serializeInto(out, entitySet, data);

      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  public void appendTo(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      writer.writeStartElement("entry");
      writer.writeDefaultNamespace(NS_ATOM);
      writer.writeNamespace("m", NS_DATASERVICES_METADATA);
      writer.writeNamespace("d", NS_DATASERVICES);
      writer.writeAttribute(NS_XML, "base", getContext().getUriInfo().getBaseUri().toASCIIString());

      handleAtomParts(writer, entitySet, data);

      handleEntity(writer, entitySet, data);

      writer.writeEndElement();

      writer.flush();
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private void handleEntity(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data) throws EdmException, XMLStreamException {
    writer.writeStartElement(NS_DATASERVICES_METADATA, TAG_PROPERTIES);
    Set<Entry<String, Object>> entries = data.entrySet();

    for (Entry<String, Object> entry : entries) {
      String name = entry.getKey();
      EdmTyped property = entitySet.getEntityType().getProperty(name);

      if (property instanceof EdmProperty) {
        EdmProperty prop = (EdmProperty) property;
        Object value = entry.getValue();

        AtomPropertySerializer aps = new AtomPropertySerializer();
        aps.appendTo(writer, prop, value);
      }
    }

    writer.writeEndElement();
  }

  private void handleAtomParts(XMLStreamWriter writer, EdmEntitySet entitySet, Map<String, Object> data) throws XMLStreamException, ODataSerializationException, EdmException {
    AtomHelper atomHelper = AtomHelper.init(entitySet);
    writer.writeStartElement("id");
    writer.writeCharacters(this.createIdUri(entitySet, data));
    writer.writeEndElement();

    writer.writeStartElement("title");
    writer.writeAttribute("type", this.createTitleType(atomHelper));
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
    EdmProperty titleProperty = atomHelper.getSyndicationProperty(EdmTargetPath.SYNDICATION_TITLE);

    if (titleProperty != null) {
      return (String) data.get(titleProperty.getName());
    }

    throw new EdmException(EdmException.COMMON);
  }

  private String createTitleType(AtomHelper atomHelper) throws EdmException, ODataSerializationException {
    EdmProperty titleProperty = atomHelper.getSyndicationProperty(EdmTargetPath.SYNDICATION_TITLE);

    if (titleProperty != null) {
      switch (titleProperty.getType().getKind()) {
      case SIMPLE:
        EdmContentKind contentKind = titleProperty.getCustomizableFeedMappings().getFcContentKind();
        if (contentKind != null) {
          return contentKind.name();
        } else {
          throw new ODataSerializationException(ODataSerializationException.ATOM_TITLE);
        }
      case COMPLEX:
      case NAVIGATION:
      default:
      }
    }

    throw new ODataSerializationException(ODataSerializationException.ATOM_TITLE);
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

  private String createIdUri(EdmEntitySet edmEntitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      ODataContext ctx = getContext();

      EdmEntitySet es = edmEntitySet;
      EdmEntityContainer ec = es.getEntityContainer();
      List<EdmProperty> kp = es.getEntityType().getKeyProperties();

      String id = ctx.getUriInfo().getBaseUri().toASCIIString();
      if (!ec.isDefaultEntityContainer()) {
        id = id + ec.getName() + ".";
      }
      String keys = "";
      if (kp.size() == 1) {
        EdmSimpleType st = (EdmSimpleType) kp.get(0).getType();
        Object value = data.get(kp.get(0).getName());
        String strValue = st.valueToString(value, EdmLiteralKind.URI, kp.get(0).getFacets());
        keys = keys + strValue;
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
      id = id + es.getName() + "(" + keys + ")";
      return id;
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }
}
