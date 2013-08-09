package com.sap.core.odata.core.ep.consumer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityTypeMapping;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * @author SAP AG
 */
public class XmlPropertyConsumer {

  protected static final String TRUE = "true";
  protected static final String FALSE = "false";

  public Map<String, Object> readProperty(final XMLStreamReader reader, final EdmProperty property, final boolean merge) throws EntityProviderException {
    return readProperty(reader, property, merge, null);
  }

  public Map<String, Object> readProperty(final XMLStreamReader reader, final EdmProperty property, final boolean merge, final Map<String, Object> typeMappings) throws EntityProviderException {
    EntityPropertyInfo eia = EntityInfoAggregator.create(property);

    try {
      reader.next();

      Object value = readStartedElement(reader, eia, EntityTypeMapping.create(typeMappings));

      if (eia.isComplex() && merge) {
        mergeWithDefaultValues(value, eia);
      }

      Map<String, Object> result = new HashMap<String, Object>();
      result.put(eia.getName(), value);
      return result;
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    }
  }

  @SuppressWarnings("unchecked")
  private void mergeWithDefaultValues(final Object value, final EntityPropertyInfo epi) throws EntityProviderException {
    if (!(value instanceof Map)) {
      throw new EntityProviderException(EntityProviderException.COMMON);
    }
    if (!epi.isComplex()) {
      throw new EntityProviderException(EntityProviderException.COMMON);
    }

    mergeComplexWithDefaultValues((Map<String, Object>) value, (EntityComplexPropertyInfo) epi);
  }

  private void mergeComplexWithDefaultValues(final Map<String, Object> complexValue, final EntityComplexPropertyInfo ecpi) {
    for (EntityPropertyInfo info : ecpi.getPropertyInfos()) {
      Object obj = complexValue.get(info.getName());
      if (obj == null) {
        if (info.isComplex()) {
          Map<String, Object> defaultValue = new HashMap<String, Object>();
          mergeComplexWithDefaultValues(defaultValue, ecpi);
          complexValue.put(info.getName(), defaultValue);
        } else {
          EdmFacets facets = info.getFacets();
          if (facets != null) {
            complexValue.put(info.getName(), facets.getDefaultValue());
          }
        }
      }
    }
  }

  protected Object readStartedElement(final XMLStreamReader reader, final EntityPropertyInfo propertyInfo, final EntityTypeMapping typeMappings) throws EntityProviderException, EdmException {
    final String name = propertyInfo.getName();
    Object result = null;

    try {
      reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_D_2007_08, name);
      final String nullAttribute = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, FormatXml.M_NULL);

      if (!(nullAttribute == null || TRUE.equals(nullAttribute) || FALSE.equals(nullAttribute)))
        throw new EntityProviderException(EntityProviderException.COMMON);

      if (TRUE.equals(nullAttribute)) {
        if (propertyInfo.isMandatory())
          throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY_VALUE.addContent(name));
        reader.nextTag();
      } else if (propertyInfo.isComplex()) {
        final String typeAttribute = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, FormatXml.M_TYPE);
        if (typeAttribute != null) {
          final String expectedTypeAttributeValue = propertyInfo.getType().getNamespace() + Edm.DELIMITER + propertyInfo.getType().getName();
          if (!expectedTypeAttributeValue.equals(typeAttribute))
            throw new EntityProviderException(EntityProviderException.INVALID_COMPLEX_TYPE.addContent(expectedTypeAttributeValue).addContent(typeAttribute));
        }

        reader.nextTag();
        Map<String, Object> name2Value = new HashMap<String, Object>();
        while (reader.hasNext() && !reader.isEndElement()) {
          final String childName = reader.getLocalName();
          final EntityPropertyInfo childProperty = ((EntityComplexPropertyInfo) propertyInfo).getPropertyInfo(childName);
          if (childProperty == null)
            throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(childName));
          final Object value = readStartedElement(reader, childProperty, typeMappings.getEntityTypeMapping(name));
          name2Value.put(childName, value);
          reader.nextTag();
        }
        result = name2Value.isEmpty() ? null : name2Value;
      } else {
        result = convert(propertyInfo, reader.getElementText(), typeMappings.getMappingClass(name));
      }
      reader.require(XMLStreamConstants.END_ELEMENT, Edm.NAMESPACE_D_2007_08, name);

      return result;
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    }
  }

  private Object convert(final EntityPropertyInfo property, final String value, final Class<?> typeMapping) throws EdmSimpleTypeException {
    final EdmSimpleType type = (EdmSimpleType) property.getType();
    return type.valueOfString(value, EdmLiteralKind.DEFAULT, property.getFacets(),
        typeMapping == null ? type.getDefaultType() : typeMapping);
  }
}
