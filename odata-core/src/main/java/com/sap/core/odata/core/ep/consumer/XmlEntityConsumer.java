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
import com.sap.core.odata.api.ep.ReadEntryResult;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;

/**
 * Xml entity (content type dependent) consumer for reading input (from <code>content</code>).
 * 
 * @author SAP AG
 */
public class XmlEntityConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(XmlEntityConsumer.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

  public XmlEntityConsumer() throws EntityProviderException {
    super();
  }

  public ReadEntryResult readEntry(EdmEntitySet entitySet, Object content) throws EntityProviderException {
    XMLStreamReader reader = null;
    
    try {
      XmlEntryConsumer xec = new XmlEntryConsumer();
      reader = createStaxReader(content);
      
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      ReadEntryResult result = xec.readEntry(reader, entitySet, eia);
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
  
  public Object readPropertyValue(EdmProperty edmProperty, Object content) throws EntityProviderException {
    try {
      Map<String, Object> result = readProperty(edmProperty, content);
      return result.get(edmProperty.getName());
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  
  public String readLink(EdmEntitySet entitySet, Object content) throws EntityProviderException {
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

  public List<String> readLinks(EdmEntitySet entitySet, Object content) throws EntityProviderException {
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
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT
          .addContent("Got not supported NULL object as content to de-serialize."));      
    }
    
    if(content instanceof InputStream) {
      XMLStreamReader streamReader = factory.createXMLStreamReader((InputStream) content, DEFAULT_CHARSET);
      return streamReader;
    }
    throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT
        .addContent("Found not supported content of class '" + content.getClass() + "' to de-serialize."));      
  }
}
