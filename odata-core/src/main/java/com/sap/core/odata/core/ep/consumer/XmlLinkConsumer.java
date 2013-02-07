package com.sap.core.odata.core.ep.consumer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * 
 */
public class XmlLinkConsumer {

  private static final String TAG_URI = "uri";
  private static final String TAG_LINKS = "links";

  /**
   * Reads single link with format:
   * <code>
   * <pre>
   * {@code
   *  <uri>http://somelink</uri>
   * }
   * </pre>
   * </code>
   * 
   * @param reader
   * @param entitySet
   * @return link as string object
   * @throws EntityProviderException
   */
  public String readLink(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    try {
      String link = readLink(reader);

      return link;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private String readLink(XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    return readLink(reader, false);
  }
  
  private String readLink(XMLStreamReader reader, boolean started) throws EntityProviderException, XMLStreamException {
    //
    int eventType = reader.getEventType();
    if(!started) {
      eventType = reader.next();
    }
    
    if(eventType != XMLStreamReader.START_ELEMENT || !reader.getLocalName().equals(TAG_URI)) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE
          .addContent("Found no starting '" + TAG_URI + "' tag for link parsing."));
    }
    
    //
    String result = null;
    eventType = reader.next();
    if(XMLStreamReader.CHARACTERS == eventType) {
      result = reader.getText();
    } else {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE
          .addContent("Found no text for '" + TAG_URI + "' tag for link parsing."));      
    }
    
    //
    eventType = reader.next();
    if(eventType != XMLStreamReader.END_ELEMENT || !reader.getLocalName().equals(TAG_URI)) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE
          .addContent("Found no closing '" + TAG_URI + "' tag for link parsing."));
    } 
    return result;
  }

  /**
   * Reads multiple links with format 
   * <code>
   * <pre>
   * {@code
   * <links>
   *  <uri>http://somelink</uri>
   *  <uri>http://anotherLink</uri>
   *  <uri>http://somelink/yetAnotherLink</uri>
   * </links>
   * }
   * </pre>
   * </code>
   * 
   * @param reader
   * @param entitySet
   * @return list of string based links
   * @throws EntityProviderException
   */
  public List<String> readLinks(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    try {
      List<String > links = new ArrayList<String>();
      
      if(!startWithLinksTag(reader)) {
        throw new EntityProviderException(EntityProviderException.INVALID_STATE
            .addContent("Found no starting '" + TAG_LINKS + "' tag for link parsing."));
      }
      
      while(linksEndNotReached(reader)) {
        String link = readLink(reader, true);
        links.add(link);
      }

      return links;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private boolean startWithLinksTag(XMLStreamReader reader) throws XMLStreamException {
    int eventType = reader.next();
    boolean isStartLinks = eventType == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals(TAG_LINKS);
    return isStartLinks;
  }

  private boolean isEndLinksTag(XMLStreamReader reader, int eventType) {
    boolean isStartLinks = eventType != XMLStreamReader.END_ELEMENT || !reader.getLocalName().equals(TAG_LINKS);
    return isStartLinks;
  }

  private boolean linksEndNotReached(XMLStreamReader reader) throws XMLStreamException {
    int eventType = reader.next();
    return isEndLinksTag(reader, eventType);
  }
}
