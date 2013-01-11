package com.sap.core.odata.core.ec;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;

public class XmlPropertyConsumer {

  public Map<String, Object> readProperty(XMLStreamReader reader, EdmProperty property) throws EntityConsumerException {
    try {
      Map<String, Object> result = new HashMap<String, Object>();
      reader.next();
      Object value = readStartedElement(reader, property);
      result.put(property.getName(), value);
      return result;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);      
    }
  }


  Object readStartedElement(XMLStreamReader reader, EdmProperty property) throws EntityConsumerException, XMLStreamException, EdmException {
    //
    int eventType = reader.getEventType();
    if(eventType != XMLStreamReader.START_ELEMENT) {
      throw new EntityConsumerException(EntityConsumerException.INVALID_STATE);
    }
    
    //
    Map<String, Object> name2Value = new HashMap<String, Object>();
    Object result = name2Value;

    while(eventType != XMLStreamReader.END_ELEMENT && eventType != XMLStreamReader.END_DOCUMENT) {
      eventType = reader.next();
      if(XMLStreamReader.START_ELEMENT == eventType) {
        String childName = reader.getLocalName();
        EdmProperty childProperty = extractProperty(childName, property);
        
        Object value = readStartedElement(reader, childProperty);
        name2Value.put(childName, value);
      } else if(XMLStreamReader.CHARACTERS == eventType) {
        result = convert(property, reader.getText());
      }
    }
    
    return result;
  }

  private EdmProperty extractProperty(String name, EdmProperty property) throws EdmException, EntityConsumerException {
    EdmType type = property.getType();
    if(type instanceof EdmSimpleType) {
      return property;
    } else if(type instanceof EdmComplexType) {
      EdmComplexType complex = (EdmComplexType) type;
      EdmProperty result = (EdmProperty) complex.getProperty(name);
      if(result == null) {
        throw new EntityConsumerException(EntityConsumerException.INVALID_PROPERTY.addContent(property.getName()));        
      }
      return result;
    }
    throw new EntityConsumerException(EntityConsumerException.INVALID_PROPERTY.addContent(property.getName()));
  }

  private Object convert(EdmProperty property, String text) throws EdmException, EntityConsumerException {
    EdmType type = property.getType();
    if(type instanceof EdmSimpleType) {
      EdmSimpleType est = (EdmSimpleType) type;
      return est.valueOfString(text, EdmLiteralKind.DEFAULT, property.getFacets());
    }
    throw new EntityConsumerException(EntityConsumerException.INVALID_PROPERTY.addContent(property.getName()));
  }
}
