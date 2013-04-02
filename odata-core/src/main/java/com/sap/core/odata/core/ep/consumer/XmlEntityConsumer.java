package com.sap.core.odata.core.ep.consumer;

import java.io.InputStream;
import java.util.HashMap;
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
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.EntityProviderReadProperties.EntityProviderReadPropertiesBuilder;
import com.sap.core.odata.api.ep.entry.ODataEntry;
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

//  public ODataEntry readEntry(final EdmEntitySet entitySet, final Object content, final boolean merge) throws EntityProviderException {
//    EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(merge).build();
//    return readEntry(entitySet, content, properties);
//  }
//
//  public ODataEntry readEntry(final EdmEntitySet entitySet, final Object content, final boolean merge, final Map<String, Object> typeMappings) throws EntityProviderException {
//    EntityProviderReadProperties properties = EntityProviderReadProperties.init()
//        .mergeSemantic(merge).addTypeMappings(typeMappings)
//        .build();
//    return readEntry(entitySet, content, properties);
//  }

  public ODataEntry readEntry(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    XMLStreamReader reader = null;

    try {
      XmlEntryConsumer xec = new XmlEntryConsumer();
      reader = createStaxReader(content);

      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      ODataEntry result = xec.readEntry(reader, eia, properties);
      return result;
    } catch (EntityProviderException e) {
      throw e;
    } catch (XMLStreamException e) {
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

//  Map<String, Object> readProperty(final EdmProperty edmProperty, final InputStream content, final boolean merge) throws EntityProviderException {
//    return readProperty(edmProperty, content, EntityProviderReadProperties.init().mergeSemantic(merge));
//  }

  public Map<String, Object> readProperty(final EdmProperty edmProperty, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    XMLStreamReader reader = null;

    try {
      XmlPropertyConsumer xec = new XmlPropertyConsumer();
      reader = createStaxReader(content);
      Map<String, Object> result = xec.readProperty(reader, edmProperty, properties.getMergeSemantic(), properties.getTypeMappings());
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

//  public Object readPropertyValue(final EdmProperty edmProperty, final InputStream content) throws EntityProviderException {
//    return readPropertyValue(edmProperty, content, null);
//  }

  public Object readPropertyValue(final EdmProperty edmProperty, final InputStream content, final Class<?> typeMapping) throws EntityProviderException {
    try {
      final Map<String, Object> result;
      EntityProviderReadPropertiesBuilder propertiesBuilder = EntityProviderReadProperties.init().mergeSemantic(false);
      if (typeMapping == null) {
        result = readProperty(edmProperty, content, propertiesBuilder.build());
      } else {
        Map<String, Object> typeMappings = new HashMap<String, Object>();
        typeMappings.put(edmProperty.getName(), typeMapping);
        result = readProperty(edmProperty, content, propertiesBuilder.addTypeMappings(typeMappings).build());
      }
      return result.get(edmProperty.getName());
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  public String readLink(final EdmEntitySet entitySet, final Object content) throws EntityProviderException {
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

  public List<String> readLinks(final EdmEntitySet entitySet, final Object content) throws EntityProviderException {
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

  private XMLStreamReader createStaxReader(final Object content) throws XMLStreamException, EntityProviderException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);

    if (content == null) {
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT
          .addContent("Got not supported NULL object as content to de-serialize."));
    }

    if (content instanceof InputStream) {
      XMLStreamReader streamReader = factory.createXMLStreamReader((InputStream) content, DEFAULT_CHARSET);
      return streamReader;
    }
    throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT
        .addContent("Found not supported content of class '" + content.getClass() + "' to de-serialize."));
  }
}
