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

  private static final String CONTENT_ATTRIBUTE_SRC = "src";
  private static final String CONTENT_ATTRIBUTE_TYPE = "type";
  private static final String TAG_PROPERTIES = "properties";
  private static final String TAG_CONTENT = "content";
  private static final String TAG_ID = "id";
  private static final String LINK_ATTRIBUTE_REL = "rel";
  private static final String LINK_ATTRIBUTE_HREF = "href";
  private static final String TAG_LINK = "link";

  final ReadEntryResultImpl readEntryResult;
  final Map<String, Object> properties;
  final MediaMetadataImpl mediaMetadata;
  final EntryMetadataImpl entryMetadata;
  
  public XmlEntryConsumer() {
    properties = new HashMap<String, Object>();
    mediaMetadata = new MediaMetadataImpl();
    entryMetadata = new EntryMetadataImpl();
    readEntryResult = new ReadEntryResultImpl(properties, mediaMetadata, entryMetadata);
  }
  
  public ReadEntryResult readEntry(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    try {
      int eventType;
      while((eventType = reader.next()) != XMLStreamReader.END_DOCUMENT) {
        if(eventType == XMLStreamReader.START_ELEMENT) {
          String tagName = reader.getLocalName();
          handleStartedTag(reader, tagName, entitySet);
        }
      }
      
      return readEntryResult;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
  
  /**
   * 
   * @param reader
   * @param tagName
   * @param entitySet
   * @throws EntityProviderException
   * @throws XMLStreamException
   * @throws EdmException
   */
  private void handleStartedTag(XMLStreamReader reader, String tagName, EdmEntitySet entitySet) throws EntityProviderException, XMLStreamException, EdmException  {
    if(TAG_ID.equals(tagName)) {
      readId(reader);
    } else if(TAG_LINK.equals(tagName)) {
      readLink(reader);
    } else if(TAG_CONTENT.equals(tagName)) {
      readContent(reader, entitySet);
    } else if(TAG_PROPERTIES.equals(tagName)) {
      readProperties(reader, entitySet);
    }
  }

  /**
   * 
   * @param reader
   * @throws EntityProviderException
   * @throws XMLStreamException
   */
  private void readLink(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, TAG_LINK);
    Map<String, String> attributes = readAttributes(reader);
    readAndValidateEndPosition(reader, TAG_LINK);
    
    String uri = attributes.get(LINK_ATTRIBUTE_HREF);
    String rel = attributes.get(LINK_ATTRIBUTE_REL);
    
    if(rel == null || uri == null) {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(
          "'" + LINK_ATTRIBUTE_HREF + "' and/or '" + LINK_ATTRIBUTE_REL + "' at tag '" + TAG_LINK + "'"));
    } else if(rel.startsWith(Edm.NAMESPACE_REL_2007_08)) {
      String navigationPropertyName = rel.substring(Edm.NAMESPACE_REL_2007_08.length());
      entryMetadata.putAssociationUri(navigationPropertyName, uri);
    } else if(rel.equals(Edm.LINK_REL_EDIT_MEDIA)) {
      mediaMetadata.setEditLink(uri);
    }
  }

  private void readContent(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException, XMLStreamException, EdmException  {
    validateStartPosition(reader, TAG_CONTENT);
    Map<String, String> attributes = readAttributes(reader);
    int nextEventType = reader.nextTag();

    if(XMLStreamReader.END_ELEMENT == nextEventType) {
      validateEndPosition(reader, TAG_CONTENT);
    } else if(XMLStreamReader.START_ELEMENT == nextEventType && reader.getLocalName().equals(TAG_PROPERTIES)) {
      readProperties(reader, entitySet);
    } else {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE
          .addContent("Expected closing 'content' or starting 'properties' but found '" + reader.getLocalName() + "'."));
    }
    //
    String contentType = attributes.get(CONTENT_ATTRIBUTE_TYPE);
    mediaMetadata.setContentType(contentType);
    String sourceLink = attributes.get(CONTENT_ATTRIBUTE_SRC);
    mediaMetadata.setSourceLink(sourceLink);
  }


  private void readId(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    validateStartPosition(reader, TAG_ID);
    int eventType = reader.next();
    String value = null;
    if(eventType == XMLStreamReader.CHARACTERS) {
      value = reader.getText();
    }
    readAndValidateEndPosition(reader, TAG_ID);
    
    entryMetadata.setId(value);
  }
  
  /**
   * 
   * @param reader
   * @param entitySet
   * @throws XMLStreamException
   * @throws EdmException
   * @throws EntityProviderException
   */
  private void readProperties(XMLStreamReader reader, EdmEntitySet entitySet) throws XMLStreamException, EdmException, EntityProviderException {
    //
    int nextTagEventType = reader.next();
    
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
        if(TAG_PROPERTIES.equals(name)) {
          run = false;
        }
      }
      nextTagEventType = reader.next();
    }
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
  
  /**
   * 
   * @param reader
   * @param tagName
   * @throws EntityProviderException
   */
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
