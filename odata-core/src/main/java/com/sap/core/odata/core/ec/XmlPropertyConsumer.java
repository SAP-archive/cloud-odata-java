package com.sap.core.odata.core.ec;

import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;

public class XmlPropertyConsumer {

  public static class Property {
    final String name;
    final Object value;
    
    public Property(String name, Object value) {
      this.name = name;
      this.value = value;
    }
  }
  
  public Property readProperty(XMLStreamReader reader, EdmProperty property) throws EntityConsumerException {
    try {
      String name;
      Object value;
      //
      int eventType = reader.next();
      if(XMLStreamReader.START_ELEMENT == eventType) {
        name = reader.getLocalName();
      } else {
        throw new EntityConsumerException(EntityConsumerException.INVALID_STATE);
      }
      //
      eventType = reader.next();
      if(XMLStreamReader.CHARACTERS == eventType) {
        value = convert(property, reader.getText());
      } else {
        throw new EntityConsumerException(EntityConsumerException.INVALID_STATE);
      }
      
      return new Property(name, value);
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);      
    }
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
