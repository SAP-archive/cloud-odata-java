package com.sap.core.odata.core.serializer;

import java.util.Collection;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;

public class AtomPropertySerializer {

  public void appendTo(XMLStreamWriter writer, EdmProperty prop, Object value) throws EdmException, XMLStreamException {
    EdmType type = prop.getType();

    String name = prop.getName();
    if(type instanceof EdmSimpleType) {
      EdmSimpleType st = (EdmSimpleType) type;
      appendEntity(writer, st, prop, value, name);
    } else if(type instanceof EdmComplexType) {
      appendEntity(writer, (EdmComplexType) type, prop, value, name);
    } else {
      writer.writeStartElement(AtomEntrySerializer.NS_DATASERVICES, name);
      writer.writeAttribute(AtomEntrySerializer.NS_DATASERVICES_METADATA, "type", type.getClass().getSimpleName());
      writer.writeEndElement();
    }
  }

  private void appendEntity(XMLStreamWriter writer, EdmComplexType type, EdmProperty prop, Object value, String name) throws XMLStreamException, EdmException {
    writer.writeStartElement(AtomEntrySerializer.NS_DATASERVICES, name);
    
    // TODO: mibo_121129: check if nullable
    if(value == null) {
      writer.writeAttribute(AtomEntrySerializer.NS_DATASERVICES_METADATA, "null", "true");
    } else {
      Collection<String> propNames = type.getPropertyNames();
      for (String pName : propNames) {
        EdmProperty internalProperty = (EdmProperty) type.getProperty(pName);
        Object childValue = extractChildValue(value, pName);
        appendTo(writer, internalProperty, childValue);
      }
    }
    writer.writeEndElement();
  }

  private Object extractChildValue(Object value, String name) {
    if(value instanceof Map) {
//      Map<String, Object> map = (Map<String, Object>) value;
      Map<?, ?> map = (Map<?, ?>) value;
      return map.get(name);
    }
    return String.valueOf(value);
  }

  private void appendEntity(XMLStreamWriter writer, EdmSimpleType st, EdmProperty prop, Object value, String name) throws XMLStreamException, EdmException {
    EdmLiteralKind literalKind = EdmLiteralKind.DEFAULT;
    EdmFacets facets = prop.getFacets();
    String valueAsString = st.valueToString(value, literalKind, facets);
    
    writer.writeStartElement(AtomEntrySerializer.NS_DATASERVICES, name);
    if(valueAsString == null) {
      writer.writeAttribute(AtomEntrySerializer.NS_DATASERVICES_METADATA, "null", "true");
    } else {
      writer.writeCharacters(valueAsString);
    }
    writer.writeEndElement();
  }

}
