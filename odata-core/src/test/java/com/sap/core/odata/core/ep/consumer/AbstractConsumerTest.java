package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.ep.entry.ODataFeed;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

public abstract class AbstractConsumerTest extends BaseTest {
  
  protected static final EntityProviderReadProperties DEFAULT_PROPERTIES = EntityProviderReadProperties.init().mergeSemantic(false).build();

  protected XMLStreamReader createReaderForTest(final String input) throws XMLStreamException {
    return createReaderForTest(input, false);
  }

  protected XMLStreamReader createReaderForTest(final String input, final boolean namespaceAware) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, namespaceAware);

    XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(input));

    return streamReader;
  }

  protected Map<String, Object> createTypeMappings(final String key, final Object value) {
    Map<String, Object> typeMappings = new HashMap<String, Object>();
    typeMappings.put(key, value);
    return typeMappings;
  }

  protected String readFile(final String filename) throws IOException {
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    if (in == null) {
      throw new IOException("Requested file '" + filename + "' was not found.");
    }

    byte[] tmp = new byte[8192];
    int count = in.read(tmp);
    StringBuffer b = new StringBuffer();
    while (count >= 0) {
      b.append(new String(tmp, 0, count));
      count = in.read(tmp);
    }

    return b.toString();
  }

  /**
   * Create a map with a 'String' to 'Class<?>' mapping based on given parameters.
   * Therefore parameters MUST be a set of such pairs.
   * As example an correct method call would be:
   * <p>
   * <code>
   *    createTypeMappings("someKey", Integer.class, "anotherKey", Long.class);
   * </code>
   * </p>
   * 
   * @param firstKeyThenMappingClass
   * @return
   */
  protected Map<String, Object> createTypeMappings(final Object... firstKeyThenMappingClass) {
    Map<String, Object> typeMappings = new HashMap<String, Object>();
    if (firstKeyThenMappingClass.length % 2 != 0) {
      throw new IllegalArgumentException("Got odd number of parameters. Please read javadoc.");
    }
    for (int i = 0; i < firstKeyThenMappingClass.length; i += 2) {
      String key = (String) firstKeyThenMappingClass[i];
      Class<?> mappingClass = (Class<?>) firstKeyThenMappingClass[i + 1];
      typeMappings.put(key, mappingClass);
    }
    return typeMappings;
  }

  protected InputStream createContentAsStream(final String content) throws UnsupportedEncodingException {
    return new ByteArrayInputStream(content.getBytes("UTF-8"));
  }

  /**
   * 
   * @param content
   * @param replaceWhitespaces if <code>true</code> all XML not necessary whitespaces between tags are
   * @return
   * @throws UnsupportedEncodingException
   */
  protected InputStream createContentAsStream(final String content, final boolean replaceWhitespaces) throws UnsupportedEncodingException {
    String contentForStream = content;
    if (replaceWhitespaces) {
      contentForStream = content.replaceAll(">\\s.<", "><");
    }

    return new ByteArrayInputStream(contentForStream.getBytes("UTF-8"));
  }

  protected ODataEntry prepareAndExecuteEntry(final String fileName, final String entitySetName, EntityProviderReadProperties readProperties) throws IOException, EdmException, ODataException, UnsupportedEncodingException, EntityProviderException {
    //prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet(entitySetName);
    String content =  readFile(fileName);
    assertNotNull(content);
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, readProperties);
    assertNotNull(result);
    return result;
  }
  
  protected ODataFeed prepareAndExecuteFeed(final String fileName, final String entitySetName, EntityProviderReadProperties readProperties) throws IOException, EdmException, ODataException, UnsupportedEncodingException, EntityProviderException {
    //prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet(entitySetName);
    String content =  readFile(fileName);
    assertNotNull(content);
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataFeed result = xec.readFeed(entitySet, contentBody, readProperties);
    assertNotNull(result);
    return result;
  }

}
