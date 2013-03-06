package com.sap.core.odata.core.ep.consumer;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.testutil.fit.BaseTest;

public abstract class AbstractConsumerTest extends BaseTest {

  protected XMLStreamReader createReaderForTest(final String input) throws XMLStreamException {
    return createReaderForTest(input, false);
  }

  protected XMLStreamReader createReaderForTest(final String input, boolean namespaceAware) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, namespaceAware);

    XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(input));

    return streamReader;
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
  protected Map<String, Class<?>> createTypeMappings(Object ... firstKeyThenMappingClass) {
    Map<String, Class<?>> typeMappings = new HashMap<String, Class<?>>();
    if(firstKeyThenMappingClass.length % 2 != 0) {
      throw new IllegalArgumentException("Got odd number of parameters. Please read javadoc.");
    }
    for (int i = 0; i < firstKeyThenMappingClass.length; i+=2) {
      String key = (String) firstKeyThenMappingClass[i];
      Class<?> mappingClass = (Class<?>) firstKeyThenMappingClass[i+1];
      typeMappings.put(key, mappingClass);
    }
    return typeMappings;
  }

}
