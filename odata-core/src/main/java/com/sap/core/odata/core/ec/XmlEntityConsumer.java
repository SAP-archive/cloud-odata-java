package com.sap.core.odata.core.ec;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.ec.EntityConsumer;
import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataRequest;

public class XmlEntityConsumer extends EntityConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(XmlEntityConsumer.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

  protected XmlEntityConsumer() throws EntityConsumerException {
    super();
  }

  @Override
  public Map<String, Object> readEntry(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException {
    XMLStreamReader reader = null;
    
    try {
      XmlEntryConsumer xec = new XmlEntryConsumer();
      reader = createStaxReader(request);
      Map<String, Object> resultMap = xec.readEntry(reader, entitySet);
      return resultMap;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
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
  public Map<String, Object> readProperty(EdmProperty edmProperty, ODataRequest request) throws EntityConsumerException {
    XMLStreamReader reader = null;

    try {
      XmlPropertyConsumer xec = new XmlPropertyConsumer();
      reader = createStaxReader(request);
      Map<String, Object> result = xec.readProperty(reader, edmProperty);
      return result;
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
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
  public Map<String, Object> readLink(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException {
    XMLStreamReader reader = null;

    try {
      XmlLinkConsumer xlc = new XmlLinkConsumer();
      reader = createStaxReader(request);
      return xlc.readLink(reader, entitySet);
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
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
  public List<Map<String, Object>> readLinks(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException {
    XMLStreamReader reader = null;

    try {
      XmlLinkConsumer xlc = new XmlLinkConsumer();
      reader = createStaxReader(request);
      return xlc.readLinks(reader, entitySet);
    } catch (Exception e) {
      throw new EntityConsumerException(EntityConsumerException.COMMON, e);
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

  private XMLStreamReader createStaxReader(ODataRequest request) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);

    XMLStreamReader streamReader = factory.createXMLStreamReader(request.getContent(), DEFAULT_CHARSET);

    return streamReader;
  }
}
