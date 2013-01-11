package com.sap.core.odata.core.ec;

import java.util.HashMap;
import java.util.Map;

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
      //
      @SuppressWarnings("unchecked")
      Map<String, Object> tmp = (Map<String, Object>) readStartedElement(reader, reader.next(), property);
      return tmp;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);      
    }
  }


  private Object readStartedElement(XMLStreamReader reader, int eventType, EdmProperty rootProperty) throws EntityConsumerException {
    try {
      Map<String, Object> tmp = new HashMap<String, Object>();
      Object resultValue = null;
      //
      while(eventType != XMLStreamReader.END_ELEMENT && eventType != XMLStreamReader.END_DOCUMENT) {
        EdmProperty myProperty = null;
        if(XMLStreamReader.START_ELEMENT == eventType) {
          String name = reader.getLocalName();
//          System.out.println("Start with PropName: " + rootProperty.getName() + "; Name: " + name); 
          if(myProperty == null) {
            myProperty = extractProperty(name, rootProperty);
          }
          int next = reader.next();
//          String propName = myProperty.getName();
//          System.out.println("...continue Name: " + name + " and exPropName: " + propName); 
          Object value = readStartedElement(reader, next, myProperty);
//          System.out.println("...finished name: " + name + " with value: " + value); 
          tmp.put(name, value);
          resultValue = tmp;
        } else if(XMLStreamReader.CHARACTERS == eventType) {
            Object value = convert(rootProperty, reader.getText());
//            System.out.println("...finished ?? with value: " + value); 
            resultValue = value;
        } else {
//          System.out.println("ET: " + eventType);
          throw new EntityConsumerException(EntityConsumerException.INVALID_STATE);
        }
        
        eventType = reader.next();
      }
      return resultValue;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);      
    }
  }

  private EdmProperty extractProperty(String name, EdmProperty property) throws EdmException, EntityConsumerException {
    if(name.equals(property.getName())) {
//      System.out.println("Self call for " + name);
      return property;
    }
    
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
