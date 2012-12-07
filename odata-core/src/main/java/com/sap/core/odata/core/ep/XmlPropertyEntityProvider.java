package com.sap.core.odata.core.ep;

import java.util.Collection;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;

public class XmlPropertyEntityProvider {

  public void append(XMLStreamWriter writer, EntityPropertyInfo propertyInfo, Object value, boolean isRootElement) throws EdmException, XMLStreamException, ODataEntityProviderException {
    String name = propertyInfo.getName();

    if (isRootElement) {
      writer.writeStartElement(name);
      writer.writeDefaultNamespace(Edm.NAMESPACE_D_2007_08);
      writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
    } else if (hasCustomNamespace(propertyInfo)) {
      writeStartElementWithCustomNamespace(writer, propertyInfo, name);
    } else {
      writer.writeStartElement(Edm.NAMESPACE_D_2007_08, name);
    }

    if (propertyInfo.isComplex()) {
      EntityComplexPropertyInfo ecp = (EntityComplexPropertyInfo) propertyInfo;
      appendProperty(writer, ecp, propertyInfo, value);
    } else {
      appendProperty(writer, propertyInfo, value);
    }

    writer.writeEndElement();
  }

  private void appendProperty(XMLStreamWriter writer, EntityComplexPropertyInfo type, EntityPropertyInfo prop, Object value) throws XMLStreamException, EdmException, ODataEntityProviderException {

    if (value == null) {
      writer.writeAttribute(Edm.NAMESPACE_M_2007_08, "null", "true");
    } else {
      Collection<EntityPropertyInfo> propertyInfos = type.getPropertyInfos();
      for (EntityPropertyInfo childPropertyInfo : propertyInfos) {
        Object childValue = extractChildValue(value, childPropertyInfo.getName());
        append(writer, childPropertyInfo, childValue, false);
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

  private void appendProperty(XMLStreamWriter writer, EntityPropertyInfo prop, Object value) throws XMLStreamException, EdmException {
    EdmLiteralKind literalKind = EdmLiteralKind.DEFAULT;
    EdmFacets facets = prop.getFacets();
    EdmSimpleType st = (EdmSimpleType) prop.getType();
    String valueAsString = st.valueToString(value, literalKind, facets);

    if (valueAsString == null) {
      writer.writeAttribute(Edm.NAMESPACE_M_2007_08, "null", "true");
    } else {
      writer.writeCharacters(valueAsString);
    }
  }

  private void writeStartElementWithCustomNamespace(XMLStreamWriter writer, EntityPropertyInfo prop, String name) throws XMLStreamException {
    EdmCustomizableFeedMappings mapping = prop.getCustomMapping();
    String nsPrefix = mapping.getFcNsPrefix();
    String nsUri = mapping.getFcNsUri();
    writer.writeStartElement(nsPrefix, name, nsUri);
    writer.writeNamespace(nsPrefix, nsUri);
  }

  private boolean hasCustomNamespace(EntityPropertyInfo prop) {
    if (prop.getCustomMapping() != null) {
      EdmCustomizableFeedMappings mapping = prop.getCustomMapping();
      return !(mapping.getFcNsPrefix() == null || mapping.getFcNsUri() == null);
    }
    return false;
  }

}
