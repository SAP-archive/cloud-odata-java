package com.sap.core.odata.core.ep.consumer;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;

public class XmlEntityConsumer extends EntityConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(XmlEntityConsumer.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

  public XmlEntityConsumer() throws EntityProviderException {
    super();
  }

  @Override
  public Map<String, Object> readEntry(EdmEntitySet entitySet, Object content) throws EntityProviderException {
    XMLStreamReader reader = null;
    
    try {
      XmlEntryConsumer xec = new XmlEntryConsumer();
      reader = createStaxReader(content);
      Map<String, Object> resultMap = xec.readEntry(reader, entitySet);
      return resultMap;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

  @Override
  public Map<String, Object> readProperty(EdmProperty edmProperty, Object content) throws EntityProviderException {
    XMLStreamReader reader = null;

    try {
      XmlPropertyConsumer xec = new XmlPropertyConsumer();
      reader = createStaxReader(content);
      Map<String, Object> result = xec.readProperty(reader, edmProperty);
      return result;
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }
  
  @Override
  public Map<String, Object> readLink(EdmEntitySet entitySet, Object content) throws EntityProviderException {
    XMLStreamReader reader = null;

    try {
      XmlLinkConsumer xlc = new XmlLinkConsumer();
      reader = createStaxReader(content);
      return xlc.readLink(reader, entitySet);
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

  @Override
  public List<Map<String, Object>> readLinks(EdmEntitySet entitySet, Object content) throws EntityProviderException {
    XMLStreamReader reader = null;

    try {
      XmlLinkConsumer xlc = new XmlLinkConsumer();
      reader = createStaxReader(content);
      return xlc.readLinks(reader, entitySet);
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

  private XMLStreamReader createStaxReader(Object content) throws XMLStreamException, EntityProviderException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);

      if(content == null) {
//        throw new EntityProviderException("Got not supported NULL object as content to de-serialize.");      
        throw new EntityProviderException(EntityProviderException.COMMON);      
      }
      
      if(content instanceof InputStream) {
        XMLStreamReader streamReader = factory.createXMLStreamReader((InputStream) content, DEFAULT_CHARSET);
        return streamReader;
      }
      throw new EntityProviderException(EntityProviderException.COMMON);      
//      throw new ODataException("Found not supported content of class '" + content.getClass() + "' to de-serialize.");

  }
}
