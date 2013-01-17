package com.sap.core.odata.core.ep.consumer;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.testutil.fit.BaseTest;

public abstract class AbstractConsumerTest extends BaseTest {

  protected XMLStreamReader createReaderForTest(String input) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);

    XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(input));
    
    return streamReader;
  }

}
