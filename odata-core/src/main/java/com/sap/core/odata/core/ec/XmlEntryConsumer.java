package com.sap.core.odata.core.ec;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;

public class XmlEntryConsumer {

  public Map<String, Object> readEntry(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityConsumerException {
    try {
//      Map<String, Object> resultMap = readProperties(reader, entitySet);
      Map<String, Object> resultMap = readNextTag(reader, entitySet);
      return resultMap;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
    }
  }
  
  private Map<String, Object> readNextTag(XMLStreamReader reader, EdmEntitySet entitySet) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    
    int eventType;
    while((eventType = reader.next()) != XMLStreamReader.END_DOCUMENT) {
      if(eventType == XMLStreamReader.START_ELEMENT) {
        String tagName = reader.getLocalName();
        
        Object value = handleStartedTag(reader, tagName, entitySet);
        result.put(tagName, value);
      }
    }
    
    return result;
  }

  private Object handleStartedTag(XMLStreamReader reader, String tagName, EdmEntitySet entitySet) throws EntityConsumerException, XMLStreamException, EdmException  {
    if("id".equals(tagName)) {
      return readId(reader);
    } else if("title".equals(tagName)) {
      return readTitle(reader);
    } else if("updated".equals(tagName)) {
      return readUpdated(reader);
    } else if("category".equals(tagName)) {
      return readCategory(reader);
    } else if("link".equals(tagName)) {
      return readLink(reader);
    } else if("content".equals(tagName)) {
      return readContent(reader, entitySet);
    } else if("properties".equals(tagName)) {
      return readProperties(reader, entitySet);
    }
    
    return null;
  }

  private Object readLink(XMLStreamReader reader) throws EntityConsumerException, XMLStreamException {
    validateStartPosition(reader, "link");
    Map<String, String> attributes = readAttributes(reader);
    readAndValidateEndPosition(reader, "link");
    
    return attributes;
  }

  private void validateStartPosition(XMLStreamReader reader, String tagName) throws EntityConsumerException {
    validatePosition(reader, tagName, XMLStreamReader.START_ELEMENT);
  }

  private void validateEndPosition(XMLStreamReader reader, String tagName) throws EntityConsumerException {
    validatePosition(reader, tagName, XMLStreamReader.END_ELEMENT);
  }

  private void readAndValidateEndPosition(XMLStreamReader reader, String tagName) throws EntityConsumerException, XMLStreamException {
    readAndValidatePosition(reader, tagName, XMLStreamReader.END_ELEMENT);
  }

  private void readAndValidatePosition(XMLStreamReader reader, String tagName, int eventType) throws EntityConsumerException, XMLStreamException {
    if(eventType != reader.next() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
      		"'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityConsumerException(EntityConsumerException.INVALID_STATE.addContent(msg));
    }
  }
  
  private void validatePosition(XMLStreamReader reader, String tagName, int eventType) throws EntityConsumerException {
    if(eventType != reader.getEventType() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
          "'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityConsumerException(EntityConsumerException.INVALID_STATE.addContent(msg));
    }
  }

  private Object readContent(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityConsumerException, XMLStreamException, EdmException  {
    validateStartPosition(reader, "content");
    
    Map<String, String> attributes = readAttributes(reader);
    
    int nextEventType = reader.nextTag();
    
    if(XMLStreamReader.START_ELEMENT == nextEventType) {
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("attributes", attributes);
      result.put(reader.getLocalName(), readProperties(reader, entitySet));
      return result;
    } else if(XMLStreamReader.END_ELEMENT == nextEventType) {
      validateEndPosition(reader, "content");
      return attributes;
    }
    return null;
  }

  /**
   * Read all attributes for current element to map (key=AttributeName; value=AttributeValue).
   * 
   * @param reader
   * @return
   */
  private Map<String, String> readAttributes(XMLStreamReader reader) {
    int attributesCount = reader.getAttributeCount();
    
    Map<String, String> attributes = new HashMap<String, String>();
    for (int i = 0; i < attributesCount; i++) {
      String name = reader.getAttributeName(i).getLocalPart();
      String value = reader.getAttributeValue(i);
      attributes.put(name, value);
    }
    return attributes;
  }

  private Object readCategory(XMLStreamReader reader) throws EntityConsumerException, XMLStreamException {
    validateStartPosition(reader, "category");
    Map<String, String> attributes = readAttributes(reader);
    readAndValidateEndPosition(reader, "category");
    
    return attributes;
  }

  private Object readUpdated(XMLStreamReader reader) throws EntityConsumerException, XMLStreamException {
    validateStartPosition(reader, "updated");
    int eventType = reader.next();
    Object value = null;
    if(eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, "updated");
    return value;
  }

  private Object readTitle(XMLStreamReader reader) throws EntityConsumerException, XMLStreamException {
    validateStartPosition(reader, "title");
    int eventType = reader.next();
    Object value = null;
    if(eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, "title");
    return value;
  }

  private Object readId(XMLStreamReader reader) throws EntityConsumerException, XMLStreamException {
    validateStartPosition(reader, "id");
    int eventType = reader.next();
    Object value = null;
    if(eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, "id");
    return value;
  }

  private Map<String, Object> readProperties(XMLStreamReader reader, EdmEntitySet entitySet) throws XMLStreamException, EdmException, EntityConsumerException {
    Map<String, Object> tagName2tagText = new HashMap<String, Object>();
    
    //
    int nextTagEventType = reader.next();
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    boolean run = true;
    while(run) {
      if(nextTagEventType == XMLStreamReader.START_ELEMENT) {
        String name = reader.getLocalName();
        EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty(name);
        Object value = xpc.readStartedElement(reader, property);
        tagName2tagText.put(name, value);
      } else if(nextTagEventType == XMLStreamReader.END_ELEMENT) {
        String name = reader.getLocalName();
        if("properties".equals(name)) {
          run = false;
        }
      }
      nextTagEventType = reader.next();
    }
    
    return tagName2tagText;
  }
}
