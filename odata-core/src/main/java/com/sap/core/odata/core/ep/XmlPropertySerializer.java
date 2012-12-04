package com.sap.core.odata.core.ep;

import java.util.Collection;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.ep.ODataSerializationException;
import com.sap.core.odata.core.edm.EdmString;

public class XmlPropertySerializer {

  public void append(XMLStreamWriter writer, EdmProperty edmProperty, Object value, boolean isRootElement, AtomInfoAggregator aia) throws EdmException, XMLStreamException, ODataSerializationException {
    EdmType edmType = edmProperty.getType();

    String name = edmProperty.getName();

    if (isRootElement) {
      writer.writeStartElement(name);
      writer.writeDefaultNamespace(Edm.NAMESPACE_EDM_2008_09);
      writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_EDMX_2007_06);
    } else {
      writer.writeStartElement(Edm.NAMESPACE_EDM_2008_09, name);
    }

    if (edmType instanceof EdmSimpleType) {
      EdmSimpleType st = (EdmSimpleType) edmType;
      appendProperty(writer, st, edmProperty, value, aia);
    } else if (edmType instanceof EdmComplexType) {
      appendProperty(writer, (EdmComplexType) edmType, edmProperty, value, aia);
    } else {
      throw new ODataSerializationException(ODataSerializationException.UNSUPPORTED_PROPERTY_TYPE.addContent(edmType.getName()));
    }

    writer.writeEndElement();
  }

  private void appendProperty(XMLStreamWriter writer, EdmComplexType type, EdmProperty prop, Object value, AtomInfoAggregator aia) throws XMLStreamException, EdmException, ODataSerializationException {

    if (value == null) {
      writer.writeAttribute(Edm.NAMESPACE_EDMX_2007_06, "null", "true");
    } else {
      Collection<String> propNames = type.getPropertyNames();
      for (String pName : propNames) {
        EdmProperty internalProperty = (EdmProperty) type.getProperty(pName);
        Object childValue = extractChildValue(value, pName);
        append(writer, internalProperty, childValue, false, aia);
      }
    }
  }

  private Object extractChildValue(Object value, String name) {
    if (value instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) value;
      return map.get(name);
    }
    return String.valueOf(value);
  }

  private void appendProperty(XMLStreamWriter writer, EdmSimpleType st, EdmProperty prop, Object value, AtomInfoAggregator aia) throws XMLStreamException, EdmException {
    EdmLiteralKind literalKind = EdmLiteralKind.DEFAULT;
    EdmFacets facets = prop.getFacets();
    String valueAsString = st.valueToString(value, literalKind, facets);

    if (aia != null) {
      aia.addInfo(prop, valueAsString);
    }

    if (valueAsString == null) {
      writer.writeAttribute(Edm.NAMESPACE_EDMX_2007_06, "null", "true");
    } else {
      if (!(st instanceof EdmString)) {
        writer.writeAttribute(Edm.NAMESPACE_EDMX_2007_06, FormatXml.ATOM_TYPE, st.getNamespace() + "." + st.getName());
      }

      writer.writeCharacters(valueAsString);
    }
  }

}
