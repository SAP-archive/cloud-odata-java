package com.sap.core.odata.core.ep.consumer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.EntityProviderReadProperties.EntityProviderReadPropertiesBuilder;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;

/**
 * Xml entity (content type dependent) consumer for reading input (from <code>content</code>).
 * 
 * @author SAP AG
 */
public class XmlEntityConsumer {

  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

  public XmlEntityConsumer() throws EntityProviderException {
    super();
  }

  public ODataFeed readFeed(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    XMLStreamReader reader = null;
    EntityProviderException cachedException = null;

    try {
      reader = createStaxReader(content);

      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      XmlFeedConsumer xfc = new XmlFeedConsumer();
      return xfc.readFeed(reader, eia, properties);
    } catch (EntityProviderException e) {
      cachedException = e;
      throw cachedException;
    } catch (XMLStreamException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  public ODataEntry readEntry(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    XMLStreamReader reader = null;
    EntityProviderException cachedException = null;

    try {
      XmlEntryConsumer xec = new XmlEntryConsumer();
      reader = createStaxReader(content);

      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      ODataEntry result = xec.readEntry(reader, eia, properties);
      return result;
    } catch (EntityProviderException e) {
      cachedException = e;
      throw cachedException;
    } catch (XMLStreamException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  public Map<String, Object> readProperty(final EdmProperty edmProperty, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    XMLStreamReader reader = null;
    EntityProviderException cachedException = null;

    try {
      XmlPropertyConsumer xec = new XmlPropertyConsumer();
      reader = createStaxReader(content);
      Map<String, Object> result = xec.readProperty(reader, edmProperty, properties.getMergeSemantic(), properties.getTypeMappings());
      return result;
    } catch (Exception e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  public Object readPropertyValue(final EdmProperty edmProperty, final InputStream content) throws EntityProviderException {
    return readPropertyValue(edmProperty, content, null);
  }

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
    EntityProviderException cachedException = null;

    try {
      XmlLinkConsumer xlc = new XmlLinkConsumer();
      reader = createStaxReader(content);
      return xlc.readLink(reader, entitySet);
    } catch (Exception e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  public List<String> readLinks(final EdmEntitySet entitySet, final Object content) throws EntityProviderException {
    XMLStreamReader reader = null;
    EntityProviderException cachedException = null;

    try {
      XmlLinkConsumer xlc = new XmlLinkConsumer();
      reader = createStaxReader(content);
      return xlc.readLinks(reader, entitySet);
    } catch (Exception e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (XMLStreamException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
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
      // verify charset encoding set in content is supported (if not set UTF-8 is used as defined in 'http://www.w3.org/TR/2008/REC-xml-20081126/')
      String characterEncodingInContent = streamReader.getCharacterEncodingScheme();
      if (characterEncodingInContent != null && !DEFAULT_CHARSET.equalsIgnoreCase(characterEncodingInContent)) {
        throw new EntityProviderException(EntityProviderException.UNSUPPORTED_CHARACTER_ENCODING.addContent(characterEncodingInContent));
      }
      return streamReader;
    }
    throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT
        .addContent("Found not supported content of class '" + content.getClass() + "' to de-serialize."));
  }
}
