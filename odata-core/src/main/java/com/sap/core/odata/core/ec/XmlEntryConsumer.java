package com.sap.core.odata.core.ec;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmEntitySet;

public class XmlEntryConsumer {

  public Map<String, Object> readEntry(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityConsumerException {
    try {
      Map<String, Object> resultMap = internalRead(reader);
      return resultMap;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
    }
  }

  private Map<String, Object> internalRead(XMLStreamReader xsr) throws Exception {
    Map<String, Object> tagName2tagText = new HashMap<String, Object>();
    
    //
    int nextTagEventType = readTillTag(xsr, "properties");
    //
    String currentName = null;
    while((nextTagEventType = xsr.next()) != XMLStreamReader.END_DOCUMENT) {
      
      switch (nextTagEventType) {
        case XMLStreamReader.START_ELEMENT:
          currentName = xsr.getName().getLocalPart();
          break;
        case XMLStreamReader.CHARACTERS:
          String tagtext = xsr.getText();
          tagName2tagText.put(currentName, tagtext);
          break;
        default:
          break;
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
