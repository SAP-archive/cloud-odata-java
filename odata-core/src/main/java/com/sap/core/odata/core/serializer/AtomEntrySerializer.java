package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;

public class AtomEntrySerializer extends ODataSerializer {

  private static final String TAG_PROPERTIES = "properties";
  public static final String NS_DATASERVICES = "http://schemas.microsoft.com/ado/2007/08/dataservices";
  public static final String NS_DATASERVICES_METADATA = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  public static final String NS_ATOM = "http://www.w3.org/2005/Atom";
  public static final String NS_XML = "http://www.w3.org/XML/1998/namespace";

  @Override
  public InputStream serialize() throws ODataSerializationException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out, "utf-8");

      writer.writeStartElement("entry");
      writer.writeDefaultNamespace(NS_ATOM);
      writer.writeNamespace("m", NS_DATASERVICES_METADATA);
      writer.writeNamespace("d", NS_DATASERVICES);
      writer.writeAttribute(NS_XML, "base", this.getContext().getUriInfo().getBaseUri().toASCIIString());

      handleAtomParts(writer);

      handleEntity(writer, this.getData(), this.getEdmEntitySet());
      
      writer.writeEndElement();

      writer.flush();

      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private void handleEntity(XMLStreamWriter writer, Map<String, Object> data, EdmEntitySet edm) throws EdmException, XMLStreamException {
    writer.writeStartElement(NS_DATASERVICES_METADATA, TAG_PROPERTIES);
    Set<Entry<String, Object>> entries = data.entrySet();

    for (Entry<String, Object> entry : entries) {
      String name = entry.getKey();
      EdmTyped property = edm.getEntityType().getProperty(name);

      if(property instanceof EdmProperty) {
        EdmProperty prop = (EdmProperty) property;
        EdmType type = prop.getType();
        
        if(type instanceof EdmSimpleType) {
          EdmSimpleType st = (EdmSimpleType) type;
          Object value = entry.getValue();
          EdmLiteralKind literalKind = EdmLiteralKind.DEFAULT;
          EdmFacets facets = prop.getFacets();
          String valueAsString = st.valueToString(value, literalKind, facets);
          
          writer.writeStartElement(NS_DATASERVICES, name);
          writer.writeCharacters(valueAsString);
          writer.writeEndElement();
        }
      }
    }

    writer.writeEndElement();
  }

  private void handleAtomParts(XMLStreamWriter writer) throws XMLStreamException, ODataSerializationException, EdmException {
    writer.writeStartElement("id");
    writer.writeCharacters(this.createIdUri());
    writer.writeEndElement();

    writer.writeStartElement("title");
    writer.writeAttribute("type", this.createTitleType());
    writer.writeCharacters("Walter Winter");
    writer.writeEndElement();
  }

  private String createTitleType() throws EdmException {
    String title = "text";
    
    return title;
  }

  private String createIdUri() throws ODataSerializationException {
    try {
      ODataContext ctx = this.getContext();
      Map<String, Object> data = this.getData();

      EdmEntitySet es = this.getEdmEntitySet();
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
