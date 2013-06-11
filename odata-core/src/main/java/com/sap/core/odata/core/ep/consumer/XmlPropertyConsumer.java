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
    Map<String, Object> name2Value = new HashMap<String, Object>();
    Object result = null;

    try {
      reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_D_2007_08, propertyInfo.getName());
      final String nullAttribute = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, FormatXml.M_NULL);

      if ("true".equals(nullAttribute)) {
        reader.nextTag();
      } else if (propertyInfo.isComplex()) {
        reader.nextTag();
        while (reader.hasNext() && !reader.isEndElement()) {
          String childName = reader.getLocalName();
          EntityPropertyInfo childProperty = getChildProperty(childName, propertyInfo);

          Object value = readStartedElement(reader, childProperty, typeMappings.getEntityTypeMapping(propertyInfo.getName()));
          name2Value.put(childName, value);
          reader.nextTag();
        }
      } else {
        String value = null;
        while (!reader.isEndElement() && reader.hasNext()) {
          reader.next();
          if (reader.isCharacters()) {
            if (value == null) {
              value = reader.getText();
            } else {
              value = value + reader.getText();
            }
          }
          //TODO: should we throw exceptions for events: COMMENT, CDATA
          //TODO: JUnit Test
        }

        if (value != null) {
          Class<?> mapping = typeMappings.getMappingClass(propertyInfo.getName());
          result = convert(propertyInfo, value, mapping);
        }
      }
      reader.require(XMLStreamConstants.END_ELEMENT, Edm.NAMESPACE_D_2007_08, propertyInfo.getName());

      // if reading finished check which result must be returned
      if (result != null) {
        return result;
      } else if (!name2Value.isEmpty()) {
        return name2Value;
      }
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    }
    return null;
  }

  private EntityPropertyInfo getChildProperty(final String childPropertyName, final EntityPropertyInfo property) throws EdmException, EntityProviderException {
    if (property.isComplex()) {
      EntityComplexPropertyInfo complex = (EntityComplexPropertyInfo) property;
      return complex.getPropertyInfo(childPropertyName);
    }
    throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(
        "Expected complex property but found simple for property with name '" + property.getName() + "'"));
  }

  private Object convert(final EntityPropertyInfo property, final String value, final Class<?> typeMapping) throws EdmException, EntityProviderException {
    if (!property.isComplex()) {
      EdmSimpleType type = (EdmSimpleType) property.getType();
      return type.valueOfString(value, EdmLiteralKind.DEFAULT, property.getFacets(),
          typeMapping == null ? type.getDefaultType() : typeMapping);
    }
    throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(
        "Expected simple property but found complex for property with name '" + property.getName() + "'"));
  }
}
