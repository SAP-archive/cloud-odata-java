package com.sap.core.odata.core.ec;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ec.EntityConsumer;
import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataRequest;

public class XmlEntityConsumer extends EntityConsumer {

  protected XmlEntityConsumer() throws EntityConsumerException {
    super();
  }

  public Map<String, Object> readEntry(String input) {
    
    try {
      return internalRead(input);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private Map<String, Object> internalRead(String input) throws Exception {
    XMLStreamReader xsr = createStaxReader(input);
    Map<String, Object> tagName2tagText = new HashMap<String, Object>();
    
    //
    int nextTagEventType = readTillTag(xsr, "properties");
    //
    String currentName = null;
    while((nextTagEventType = xsr.next()) != XMLStreamReader.END_DOCUMENT) {
      
      switch (nextTagEventType) {
        case XMLStreamReader.START_ELEMENT:
          currentName = xsr.getName().getLocalPart();
//          System.out.println(nextTagEventType + " Name: " + xsr.getName());
          break;
        case XMLStreamReader.CHARACTERS:
          String tagtext = xsr.getText();
//          System.out.println("Text: " + tagtext);
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

  private XMLStreamReader createStaxReader(String input) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);

    XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(input));
    
    return streamReader;
  }

  @Override
  public Map<String, Object> readEntry(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException {
    try {
      String input = getContent(request);
      Map<String, Object> resultMap = internalRead(input);
      return resultMap;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
    }
  }

  private String getContent(ODataRequest request) throws EntityConsumerException {
    try {
      InputStream inputStream = request.getContent();
      StringBuilder content = new StringBuilder();
      
      String charset = "utf-8";
      InputStreamReader isr = new InputStreamReader(inputStream, charset);

      int sign;
      while((sign = isr.read()) != -1) {
        content.append((char) sign);
      }
      return content.toString();
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
    }
  }

  @Override
  public Map<String, Object> readLink(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException {
    return null;
  }

  @Override
  public Object readProperty(EdmProperty edmProperty, ODataRequest request) throws EntityConsumerException {
    return null;
  }

  @Override
  public List<Map<String, Object>> readLinks(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException {
    return null;
  }
}
