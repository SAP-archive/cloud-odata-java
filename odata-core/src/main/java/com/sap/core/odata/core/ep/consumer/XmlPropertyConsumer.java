package com.sap.core.odata.core.ep.consumer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;

public class XmlPropertyConsumer {

  public Map<String, Object> readProperty(XMLStreamReader reader, EdmProperty property) throws EntityProviderException {
    try {
      EntityPropertyInfo eia = EntityInfoAggregator.create(property);
      reader.next();

      Object value = readStartedElement(reader, eia);
      
      Map<String, Object> result = new HashMap<String, Object>();
      result.put(property.getName(), value);
      return result;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);      
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
    Object result = name2Value;

    while(eventType != XMLStreamReader.END_ELEMENT && eventType != XMLStreamReader.END_DOCUMENT) {
      eventType = reader.next();
      if(XMLStreamReader.START_ELEMENT == eventType) {
        String childName = reader.getLocalName();
        EntityPropertyInfo childProperty = getChildProperty(childName, propertyInfo);
        
        Object value = readStartedElement(reader, childProperty);
        name2Value.put(childName, value);
      } else if(XMLStreamReader.CHARACTERS == eventType) {
        result = convert(propertyInfo, reader.getText());
      }
    }
    
    return result;
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
      EdmSimpleType est = (EdmSimpleType) property.getType();
      return est.valueOfString(text, EdmLiteralKind.DEFAULT, property.getFacets());
    }
    throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(
        "Expected simple property but found complex for property with name '" + property.getName() + "'"));
  }
}
