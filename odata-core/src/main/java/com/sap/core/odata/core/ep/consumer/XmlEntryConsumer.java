package com.sap.core.odata.core.ep.consumer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.ReadEntryResult;
import com.sap.core.odata.core.ep.EntryMetadataImpl;
import com.sap.core.odata.core.ep.MediaMetadataImpl;
import com.sap.core.odata.core.ep.ReadEntryResultImpl;

public class XmlEntryConsumer {

  final ReadEntryResultImpl result;
  final Map<String, Object> properties;
  final MediaMetadataImpl mediaMetadata;
  final EntryMetadataImpl entryMetadata;
  
  public XmlEntryConsumer() {
    properties = new HashMap<String, Object>();
    mediaMetadata = new MediaMetadataImpl();
    entryMetadata = new EntryMetadataImpl();
    result = new ReadEntryResultImpl(properties, mediaMetadata, entryMetadata);
  }
  
  public ReadEntryResult readEntry(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    try {
      ReadEntryResult resultMap = readNextTag(reader, entitySet);
      return resultMap;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
  
  
  private ReadEntryResult readNextTag(XMLStreamReader reader, EdmEntitySet entitySet) throws Exception {
    
    int eventType;
    while((eventType = reader.next()) != XMLStreamReader.END_DOCUMENT) {
      if(eventType == XMLStreamReader.START_ELEMENT) {
        String tagName = reader.getLocalName();
        
        handleStartedTag(reader, tagName, entitySet);
      }
    }
    
    return result;
  }

  private void handleStartedTag(XMLStreamReader reader, String tagName, EdmEntitySet entitySet) throws EntityProviderException, XMLStreamException, EdmException  {
    if("id".equals(tagName)) {
      readId(reader);
    } else if("title".equals(tagName)) {
      readTitle(reader);
    } else if("updated".equals(tagName)) {
      readUpdated(reader);
    } else if("category".equals(tagName)) {
      readCategory(reader);
    } else if("link".equals(tagName)) {
      readLink(reader);
    } else if("content".equals(tagName)) {
      readContent(reader, entitySet);
    } else if("properties".equals(tagName)) {
      readProperties(reader, entitySet);
    }
  }

  private void readLink(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, "link");
    Map<String, String> attributes = readAttributes(reader);
    readAndValidateEndPosition(reader, "link");
    
    String uri = attributes.get("href");
    String rel = attributes.get("rel");
    
    if(rel == null || uri == null) {
      // TODO: create message reference
      throw new EntityProviderException(EntityProviderException.COMMON);
    } else if(rel.startsWith(Edm.NAMESPACE_REL_2007_08)) {
      String navigationPropertyName = rel.substring(Edm.NAMESPACE_REL_2007_08.length());
      entryMetadata.putAssociationUri(navigationPropertyName, uri);
    } else if(rel.equals("edit-media")) {
      mediaMetadata.setEditLink(uri);
    }
  }

  private void readContent(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException, XMLStreamException, EdmException  {
    validateStartPosition(reader, "content");
    Map<String, String> attributes = readAttributes(reader);
    int nextEventType = reader.nextTag();

    if(XMLStreamReader.END_ELEMENT == nextEventType) {
      validateEndPosition(reader, "content");
    } else if(XMLStreamReader.START_ELEMENT == nextEventType && reader.getLocalName().equals("properties")) {
      readProperties(reader, entitySet);
    } else {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE
          .addContent("Expected closing 'content' or starting 'properties' but found '" + reader.getLocalName() + "'."));
    }
    //
    String contentType = attributes.get("type");
    mediaMetadata.setContentType(contentType);
    String sourceLink = attributes.get("src");
    mediaMetadata.setSourceLink(sourceLink);
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

  private Object readCategory(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, "category");
    Map<String, String> attributes = readAttributes(reader);
    readAndValidateEndPosition(reader, "category");
    
    return attributes;
  }

  private Object readUpdated(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, "updated");
    int eventType = reader.next();
    Object value = null;
    if(eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, "updated");
    return value;
  }

  private Object readTitle(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, "title");
    int eventType = reader.next();
    Object value = null;
    if(eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, "title");
    return value;
  }

  private void readId(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, "id");
    int eventType = reader.next();
    String value = null;
    if(eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, "id");
    
    entryMetadata.setId(value);
  }

  private void readProperties(XMLStreamReader reader, EdmEntitySet entitySet) throws XMLStreamException, EdmException, EntityProviderException {
    readProperties(reader, entitySet, false);
  }
  
  private void readProperties(XMLStreamReader reader, EdmEntitySet entitySet, boolean skipFirstRead) throws XMLStreamException, EdmException, EntityProviderException {
    //
    int nextTagEventType;
    if(skipFirstRead) {
      nextTagEventType = reader.getEventType();
    } else {
      nextTagEventType = reader.next();
    }
    
    XmlPropertyConsumer xpc = new XmlPropertyConsumer();
    boolean run = true;
    while(run) {
      if(nextTagEventType == XMLStreamReader.START_ELEMENT) {
        String name = reader.getLocalName();
        EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty(name);
        Object value = xpc.readStartedElement(reader, property);
        properties.put(name, value);
      } else if(nextTagEventType == XMLStreamReader.END_ELEMENT) {
        String name = reader.getLocalName();
        if("properties".equals(name)) {
          run = false;
        }
      }
      nextTagEventType = reader.next();
    }
  }
  
  private void validateStartPosition(XMLStreamReader reader, String tagName) throws EntityProviderException {
    validatePosition(reader, tagName, XMLStreamReader.START_ELEMENT);
  }

  private void validateEndPosition(XMLStreamReader reader, String tagName) throws EntityProviderException {
    validatePosition(reader, tagName, XMLStreamReader.END_ELEMENT);
  }

  private void readAndValidateEndPosition(XMLStreamReader reader, String tagName) throws EntityProviderException, XMLStreamException {
    readAndValidatePosition(reader, tagName, XMLStreamReader.END_ELEMENT);
  }

  private void readAndValidatePosition(XMLStreamReader reader, String tagName, int eventType) throws EntityProviderException, XMLStreamException {
    if(eventType != reader.next() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
          "'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(msg));
    }
  }
  
  private void validatePosition(XMLStreamReader reader, String tagName, int eventType) throws EntityProviderException {
    if(eventType != reader.getEventType() || !reader.getLocalName().equals(tagName)) {
      String msg = "Invalid position for expected name=" + tagName + " event='" + eventType +
          "'; found name='" + reader.getLocalName() + "' event='" + reader.getEventType() + "'.";
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(msg));
    }
  }
}
