package com.sap.core.odata.core.ep.consumer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;

public class XmlPropertyConsumer {

  public Map<String, Object> readProperty(XMLStreamReader reader, EdmProperty property, boolean merge) throws EntityProviderException {
    try {
      EntityPropertyInfo eia = EntityInfoAggregator.create(property);
      reader.next();

      Object value = readStartedElement(reader, eia);
      
      if(eia.isComplex() && merge) {
        mergeWithDefaultValues(value, eia);
      }
      
      Map<String, Object> result = new HashMap<String, Object>();
      result.put(property.getName(), value);
      return result;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);      
    }
  }

  @SuppressWarnings("unchecked")
  private void mergeWithDefaultValues(Object value, EntityPropertyInfo epi) throws EntityProviderException {
    if(!(value instanceof Map)) {
      throw new EntityProviderException(EntityProviderException.COMMON);      
    }
    if(!epi.isComplex()) {
      throw new EntityProviderException(EntityProviderException.COMMON);            
    }
    
    mergeComplexWithDefaultValues((Map<String, Object>) value, (EntityComplexPropertyInfo) epi);
  }
  
  private void mergeComplexWithDefaultValues(Map<String, Object> complexValue, EntityComplexPropertyInfo ecpi) {
    for(EntityPropertyInfo info: ecpi.getPropertyInfos()) {
      Object obj = complexValue.get(info.getName());
      if(obj == null) {
        if(info.isComplex()) {
          Map<String, Object> defaultValue = new HashMap<String, Object>();
          mergeComplexWithDefaultValues(defaultValue, (EntityComplexPropertyInfo) ecpi);
          complexValue.put(info.getName(), defaultValue);
        } else {
          EdmFacets facets = info.getFacets();
          if(facets != null) {
            complexValue.put(info.getName(), facets.getDefaultValue());
          }
        }
      }
    }
  }

  Object readStartedElement(XMLStreamReader reader, EntityPropertyInfo propertyInfo) throws EntityProviderException, XMLStreamException, EdmException {
    //
    int eventType = reader.getEventType();
    if(eventType != XMLStreamReader.START_ELEMENT) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE);
    }
    
    //
    Map<String, Object> name2Value = new HashMap<String, Object>();
    Object result = null;

    while(eventType != XMLStreamReader.END_ELEMENT && eventType != XMLStreamReader.END_DOCUMENT) {
      eventType = reader.next();
      if(XMLStreamReader.START_ELEMENT == eventType && propertyInfo.isComplex()) {
        String childName = reader.getLocalName();
        EntityPropertyInfo childProperty = getChildProperty(childName, propertyInfo);
        
        Object value = readStartedElement(reader, childProperty);
        name2Value.put(childName, value);
      } else if(XMLStreamReader.CHARACTERS == eventType && !propertyInfo.isComplex()) {
        result = convert(propertyInfo, reader.getText());
      }
    }
    
    // if reading finished check which result must be returned
    if(result != null) {
      return result;
    } else if(!name2Value.isEmpty()) {
      return name2Value;
    }
    return null;
  }

  private EntityPropertyInfo getChildProperty(String childPropertyName, EntityPropertyInfo property) throws EdmException, EntityProviderException {
    if(property.isComplex()) {
      EntityComplexPropertyInfo complex = (EntityComplexPropertyInfo) property;
      return complex.getPropertyInfo(childPropertyName);
    }
    throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(
        "Expected complex property but found simple for property with name '" + property.getName() + "'"));
  }

  private Object convert(EntityPropertyInfo property, String text) throws EdmException, EntityProviderException {
    if(!property.isComplex()) {
      EdmSimpleType type = (EdmSimpleType) property.getType();
      return type.valueOfString(text, EdmLiteralKind.DEFAULT, property.getFacets(), type.getDefaultType());
    }
    throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(
        "Expected simple property but found complex for property with name '" + property.getName() + "'"));
  }
}
