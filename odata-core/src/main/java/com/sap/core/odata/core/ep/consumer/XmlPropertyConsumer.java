package com.sap.core.odata.core.ep.consumer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.ep.EntityProviderException;

public class XmlPropertyConsumer {

  public Map<String, Object> readProperty(XMLStreamReader reader, EdmProperty property) throws EntityProviderException {
    try {
      Map<String, Object> result = new HashMap<String, Object>();
      reader.next();
      Object value = readStartedElement(reader, property);
      result.put(property.getName(), value);
      return result;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);      
    }
  }


  Object readStartedElement(XMLStreamReader reader, EdmProperty property) throws EntityProviderException, XMLStreamException, EdmException {
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
        EdmProperty childProperty = extractProperty(childName, property);
        
        Object value = readStartedElement(reader, childProperty);
        name2Value.put(childName, value);
      } else if(XMLStreamReader.CHARACTERS == eventType) {
        result = convert(property, reader.getText());
      }
    }
    
    return result;
  }

  private EdmProperty extractProperty(String name, EdmProperty property) throws EdmException, EntityProviderException {
    EdmType type = property.getType();
    if(type instanceof EdmSimpleType) {
      return property;
    } else if(type instanceof EdmComplexType) {
      EdmComplexType complex = (EdmComplexType) type;
      EdmProperty result = (EdmProperty) complex.getProperty(name);
      if(result == null) {
        throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(property.getName()));        
      }
      return result;
    }
    throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(property.getName()));
  }

  private Object convert(EdmProperty property, String text) throws EdmException, EntityProviderException {
    EdmType type = property.getType();
    if(type instanceof EdmSimpleType) {
      EdmSimpleType est = (EdmSimpleType) type;
      return est.valueOfString(text, EdmLiteralKind.DEFAULT, property.getFacets());
    }
    throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(property.getName()));
  }
}
