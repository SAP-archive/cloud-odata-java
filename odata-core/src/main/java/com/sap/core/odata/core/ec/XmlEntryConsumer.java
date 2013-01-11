package com.sap.core.odata.core.ec;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;

public class XmlEntryConsumer {

  public Map<String, Object> readEntry(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityConsumerException {
    try {
      Map<String, Object> resultMap = internalRead(reader, entitySet);
      return resultMap;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
    }
  }

  private Map<String, Object> internalRead(XMLStreamReader reader, EdmEntitySet entitySet) throws Exception {
    Map<String, Object> tagName2tagText = new HashMap<String, Object>();
    
    //
    int nextTagEventType = readTillTag(reader, "properties");
    //
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    while((nextTagEventType = reader.next()) != XMLStreamReader.END_DOCUMENT) {
      if(nextTagEventType == XMLStreamReader.START_ELEMENT) {
        String name = reader.getLocalName();
        EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty(name);
        Object value = xpc.readStartedElement(reader, property);
        tagName2tagText.put(name, value);
      }
    }
    
    return tagName2tagText;
  }
  

  private int readTillTag(XMLStreamReader xmlStreamReader, String breakTagLocalName) throws XMLStreamException {
    int nextTagEventType;
    while((nextTagEventType = xmlStreamReader.next()) != XMLStreamReader.END_DOCUMENT) {
      if(nextTagEventType == XMLStreamReader.START_ELEMENT) {
        String localName = xmlStreamReader.getName().getLocalPart();
        if(localName.equals(breakTagLocalName)) {
          break;
        }
      }
    }
    return nextTagEventType;
  }
}
